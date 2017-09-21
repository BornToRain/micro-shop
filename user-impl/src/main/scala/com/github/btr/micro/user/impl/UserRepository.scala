package com.github.btr.micro.user.impl


import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.github.btr.micro.user.api.Info
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, ReadSideProcessor}
import org.joda.time.DateTime

import scala.concurrent.{ExecutionContext, Future, Promise}

import scala.collection.JavaConversions._
import scala.collection._
/**
	* 用户仓储 - 读边
	*/
class UserRepository(session: CassandraSession)(implicit ex: ExecutionContext)
{
	def getUsers =
	{
		session.selectAll(
			"""
				SELECT * FROM user
			""").map(_.map(d =>
		{
			val name = d.getUDTValue("name")
			val fullName = Name(name.getString("first_name"),name.getString("last_name"),Option(name.getString("en_name")))
			Info(d.getString("id"),d.getString("mobile"),Some(fullName),Some(name.getInt("age")),addresses)
		}))
	}
}


/**
	* 用户事件处理
	*/
class UserReadSideProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
extends ReadSideProcessor[UserEvt]
{
	private val deleteUserPro    = Promise[PreparedStatement]
	private val insertUserPro    = Promise[PreparedStatement]
	private val updateAddressPro = Promise[PreparedStatement]

	override def aggregateTags = UserEvt.tag.allTags

	override def buildHandler = readSide.builder[UserEvt]("userEvtOffSet")
	.setGlobalPrepare(() => createTable)
	.setPrepare(_ => fSQLs)
	.setEventHandler[Created](insertUser)
	.setEventHandler[Deleted.type](deleteUser)
	.setEventHandler[CreatedAddress](updateAddress)
	.build


	//数据库表创建
	private def createTable = for
	{
	//用户姓名表
		_ <- session.executeCreateTable(
			"""
			CREATE TYPE IF NOT EXISTS full_name
	    (
		    first_name text,
				last_name text,
				en_name text
	    )
		"""
		)
		//用户收货地址表
		_ <- session.executeCreateTable(
			"""
				CREATE TYPE IF NOT EXISTS address
				(
					province text,
		      city text,
					district text,
		      zip_code int,
					street text,
		      status text,
					type text
				)
			"""
		)
		//用户表
		_ <- session.executeCreateTable(
			"""
				CREATE TABLE IF NOT EXISTS user
				(
					id text PRIMARY KEY,
					mobile text,
		      name frozen <full_name>,
		 			age int,
					addresses map<text,frozen <address>>,
		 			create_time timestamp,
					update_time timestamp
				)
			""")
	} yield Done

	//SQL
	private def fSQLs =
	{
		//插入用户
		val insertUser = session.prepare("INSERT INTO user(id,mobile,name,age,addresses,create_time,update_time) VALUES(?,?,?,?,?,?,?)")
		insertUserPro.completeWith(insertUser)
		//删除用户
		val deleteUser = session.prepare("DELETE FROM user WHERE id = ? IF EXISTS")
		deleteUserPro.completeWith(deleteUser)
		//更新用户收货地址
		val updateAddress = session.prepare("UPDATE user SET addresses = address+?")
		updateAddressPro.completeWith(updateAddress)

		Future(Done)
	}

	//插入用户
	private def insertUser(evt: EventStreamElement[Created]) =
	{
		println("创建用户")
		val cmd = evt.event.cmd
		for
		{
			session <- session.underlying().map(_.getCluster.getMetadata.getKeyspace("user"))
			nameUDT <- Future(session.getUserType("full_name").newValue())
			list <- insertUserPro.future.map(ps =>
			{
				val data = ps.bind
				data.setString("id", cmd.id)
				data.setString("mobile", cmd.mobile)
				cmd.name.map(d =>
				{
					nameUDT.setString("first_name",d.firstName)
					nameUDT.setString("last_name",d.lastName)
					nameUDT.setString("en_name",d.enName.orNull)
				})
				data.setUDTValue("name", nameUDT)
				data.setInt("age", cmd.age.getOrElse(0))
				data.setMap[String,Address]("addresses",Map[String,Address]())
				data.setTimestamp("create_time", cmd.createTime.toDate)
				data.setTimestamp("update_time", cmd.updateTime.toDate)

				List(data)
			})
		} yield list
	}

	//删除用户
	private def deleteUser(evt: EventStreamElement[Deleted.type]) =
	{
		println("删除用户")
		deleteUserPro.future.map(ps => List(ps.bind(evt.entityId)))
	}

	//更新用户收货地址
	private def updateAddress(evt: EventStreamElement[CreatedAddress]) =
	{
		println("创建用户收货地址")
		val cmd = evt.event.cmd
		updateAddressPro.future.map(ps =>
		{
			val data = ps.bind
			data.setString("id", cmd.id)
			data.setString("province", cmd.province)
			data.setString("city", cmd.city)
			data.setString("district", cmd.district)
			data.setInt("zipCode", cmd.zipCode.getOrElse(0))
			data.setString("street", cmd.street)
			data.setInt("type", cmd.addressType.id)
			data.set("update_time", cmd.updateTime, classOf[DateTime])

			List(data)
		})
	}

}
