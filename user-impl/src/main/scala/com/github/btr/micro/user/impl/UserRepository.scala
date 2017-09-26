package com.github.btr.micro.user.impl


import akka.Done
import com.datastax.driver.core.{PreparedStatement, TypeTokens}
import com.google.common.reflect.TypeToken
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, ReadSideProcessor}
import org.joda.time.DateTime
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future, Promise}

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
			val fullName = Name(name.getString("first_name"), name.getString("last_name"), Option(name.getString("en_name")))
			val addresses = d.getMap("addresses",TypeToken.of(classOf[String]),TypeToken.of(classOf[Address]))
			println(s"addresses$addresses")
			User(d.getString("id"), d.getString("mobile"), Some(fullName), Some(d.getInt("age")), Map.empty, new DateTime(d.getTimestamp("create_time")),
				new DateTime(d.getTimestamp("update_time")))
		}))
	}
}


/**
	* 用户事件处理
	*/
class UserEventProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
extends ReadSideProcessor[UserEvt]
{
	private val log = LoggerFactory.getLogger(classOf[UserEventProcessor])

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
		      status int,
					type int
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
		val insertUser = session.prepare("INSERT INTO user(id,mobile,name,age,create_time,update_time) VALUES(?,?,?,?,?,?)")
		insertUserPro.completeWith(insertUser)
		//删除用户
		val deleteUser = session.prepare("DELETE FROM user WHERE id = ?")
		deleteUserPro.completeWith(deleteUser)
		//更新用户收货地址
		val updateAddress = session.prepare("UPDATE user SET addresses = addresses+?,update_time = ? WHERE id = ?")
		updateAddressPro.completeWith(updateAddress)

		Future(Done)
	}

	//插入用户
	private def insertUser(evt: EventStreamElement[Created]) =
	{
		log.info("持久化用户到读边")
		val cmd = evt.event.cmd
		for
		{
			nameUDT <- getUDTValue("full_name")
			list <- insertUserPro.future.map(ps =>
			{
				val data = ps.bind
				data.setString("id", cmd.id)
				data.setString("mobile", cmd.mobile)
				cmd.name.map(d =>
				{
					nameUDT.setString("first_name", d.firstName)
					nameUDT.setString("last_name", d.lastName)
					nameUDT.setString("en_name", d.enName.orNull)
				})
				data.setUDTValue("name", nameUDT)
				data.setInt("age", cmd.age.getOrElse(0))
				data.setTimestamp("create_time", cmd.createTime.toDate)
				data.setTimestamp("update_time", cmd.updateTime.toDate)

				List(data)
			})
		} yield list
	}

	//删除用户
	private def deleteUser(evt: EventStreamElement[Deleted.type]) =
	{
		log.info("从读边删除用户")
		deleteUserPro.future.map(ps => List(ps.bind(evt.entityId)))
	}

	//更新用户收货地址
	private def updateAddress(evt: EventStreamElement[CreatedAddress]) =
	{
		log.info("持久化用户收货地址")
		val cmd = evt.event.cmd
		for
		{
			addressUDT <- getUDTValue("address")
			list <- updateAddressPro.future.map(ps =>
			{
				val data = ps.bind

				addressUDT.setString("province", cmd.province)
				addressUDT.setString("city", cmd.city)
				addressUDT.setString("district", cmd.district)
				addressUDT.setInt("zip_code", cmd.zipCode.getOrElse(0))
				addressUDT.setString("street", cmd.street)
				addressUDT.setInt("type", cmd.addressType.id)

				data.setMap("addresses",Map(cmd.id -> addressUDT))
				data.setTimestamp("update_time", cmd.updateTime.toDate)
				data.setString("id", cmd.userId)

				List(data)
			})
		} yield list
	}

	//获取UDT
	private def getUDTValue(name: String) = session.underlying.map(_.getCluster.getMetadata.getKeyspace("user").getUserType(name).newValue)

}
