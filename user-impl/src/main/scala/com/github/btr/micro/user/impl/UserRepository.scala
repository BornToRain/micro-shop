package com.github.btr.micro.user.impl


import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, ReadSideProcessor}

import scala.concurrent.{ExecutionContext, Future}

/**
	* 用户仓储 - 读边
	*/
class UserRepository(session: CassandraSession)(implicit ex: ExecutionContext)
{

}

/**
	* 用户事件处理
	*/
class UserReadSideProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
extends ReadSideProcessor[UserEvt]
{

	private var insertUserPre   : PreparedStatement = _
	private var deleteUserPre   : PreparedStatement = _
	private var updateAddressPre: PreparedStatement = _

	override def aggregateTags = UserEvt.tag.allTags

	override def buildHandler() = readSide.builder[UserEvt]("userEvtOffSet")
	.setGlobalPrepare(() => createTable)
	.setPrepare(_ => fSQL)
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
		//收货地址表
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
	private def fSQL = for
	{
	//插入用户
//		insertUser <- session.prepare(
//			"""
//				INSERT INTO user(id,mobile,name,age,create_time,update_time)
//				VALUES(?,?,{first_name:?,last_name:?,en_name:?},?,?,?)
//			""")
		insertUser <- session.prepare("INSERT INTO user(id,mobile,name,age,addresses,create_time,update_time) VALUES(?,?,?,?,?,?,?)")
		//删除用户
		deleteUser <- session.prepare("DELETE FROM user WHERE id = ?")
		//更新用户收货地址
//		updateAddress <- session
//		.prepare(
//			"UPDATE user SET addresses = address + {'?':{province:'?',city:'?',district:'?',zip_code:'?',street:'?',status:'USE',type:'?'}},update_time = ? WHERE id =" +
//			" ?")
	} yield
			{
				insertUserPre = insertUser
				deleteUserPre = deleteUser
//				updateAddressPre = updateAddress
				Done
			}

//	UPDATE mykeyspace.users SET addresses = addresses + {'home': { street: '191 Rue St. Charles', city: 'Paris', zip_code: 75015, phones: {'33 6 78 90 12 34'}}} WHERE id=62c36092-82a1-3a00-93d1-46196ee77204;

	//插入用户
	private def insertUser(evt: EventStreamElement[Created]) =
	{
		println("创建用户")
		val cmd = evt.event.cmd
		println(s"$cmd")

		insertUserPre.bind(cmd.id,cmd.mobile,)

//		Future.successful(List(insertUserPre.bind(cmd.id,cmd.mobile,)))
		Future.successful(List(insertUserPre.bind(cmd.id, cmd.mobile, cmd.name, cmd.age, cmd.createTime, cmd.updateTime)))

	}

	//删除用户
	private def deleteUser(evt: EventStreamElement[Deleted.type]) = Future
	.successful(List(deleteUserPre.bind(evt.entityId)))

	//更新用户收货地址
	private def updateAddress(evt: EventStreamElement[CreatedAddress]) =
	{
		println("创建用户收货地址")

		val cmd = evt.event.cmd
		println(s"$cmd")

		Future
		.successful(List(updateAddressPre.bind(cmd.id, cmd.province, cmd.city, cmd.district, cmd.zipCode, cmd.street, cmd.addressType, cmd.updateTime,
			cmd.userId)))
	}

}
