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

	private var insertPre: PreparedStatement = _
	private var deletePre: PreparedStatement = _

	override def aggregateTags = UserEvt.tag.allTags

	override def buildHandler() = readSide.builder[UserEvt]("userEvtOffSet")
	.setGlobalPrepare(() => createTable)
	.setPrepare(_ => fSQL)
	.setEventHandler[Created](e => insertUser(e.event))
	.setEventHandler[Deleted.type](deleteUser)
	.build


	//数据库表创建
	private def createTable = for
	{
	//用户表
		_ <- session.executeCreateTable(
			"""
				CREATE TABLE IF NOT EXISTS user
				(
					id text PRIMARY KEY,
					mobile text,
					first_name text,
		      last_name text,
		 			age int,
					addresses map<text,frozen<address>>
		 			create_time timestamp,
					update_time timestamp,
          PRIMARY KEY (id)
				) WITH CLUSTERING ORDER BY (create_time DESC)
			""")
		//收货地址表
		_ <- session.executeCreateTable(
			"""
				CREATE TYPE IF NOT EXISTS address
				(
					province text,
		      city text,
					district text,
		      zip_code text,
					street text,
		      status text,
					type text
				)
			"""
		)
	} yield Done

	//SQL
	private def fSQL = for
	{
		insert <- session.prepare("INSERT INTO user(id,mobile,name,birthday,create_time,update_time) VALUES(?,?,?,?,?,?)")
		delete <- session.prepare("DELETE FROM user WHERE id = ?")
	} yield
			{
				insertPre = insert
				deletePre = delete
				Done
			}

	//插入用户
	private def insertUser(evt: Created) = Future
	.successful(List(insertPre.bind(evt.cmd.id, evt.cmd.mobile, evt.cmd.firstName,evt.cmd.lastName, evt.cmd.age, evt.cmd.createTime, evt.cmd.updateTime)))

	//删除用户
	private def deleteUser(evt: EventStreamElement[Deleted.type]) = Future
	.successful(List(deletePre.bind(evt.entityId)))
}
