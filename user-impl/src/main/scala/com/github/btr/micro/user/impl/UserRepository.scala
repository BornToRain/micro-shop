package com.github.btr.micro.user.impl


import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, ReadSideProcessor}

import scala.concurrent.{ExecutionContext, Future}

/**
	* 用户仓储 - 读边
	*/
private[impl] class UserRepository(session: CassandraSession)(implicit ex: ExecutionContext)
{

}

/**
	* 用户事件处理
	*/
private[impl] class UserReadSide(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
extends ReadSideProcessor[UserEvt]
{

	private var insertPre: PreparedStatement = _
	private var updatePre: PreparedStatement = _
	private var deletePre: PreparedStatement = _

	override def aggregateTags = UserEvt.tag.allTags

	override def buildHandler() = readSide.builder[UserEvt]("userEvtOffSet")
	.setGlobalPrepare(() => createTable)
	.setPrepare(_ => fSQL)
	.setEventHandler[Created](e => insertUser(e.event))
	.setEventHandler[Updated](e => updateUser(e.event))
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
					name text,
		 			birthday date,
		 			create_time timestamp,
					update_time timestamp
				) WITH CLUSTERING ORDER BY (create_time DESC)
			""")
	} yield Done

	//SQL
	private def fSQL = for
	{
		insert <- session.prepare("INSERT INTO user(id,mobile,name,birthday,create_time,update_time) VALUES(?,?,?,?,?,?)")
		update <- session.prepare("UPDATE user SET mobile = ?,name = ?,birthday=?,update_time=? WHERE id = ?")
		delete <- session.prepare("DELETE FROM user WHERE id = ?")
	} yield
			{
				insertPre = insert
				updatePre = update
				deletePre = delete
				Done
			}

	//插入用户
	private def insertUser(evt: Created) = Future
	.successful(List(insertPre.bind(evt.cmd.id, evt.cmd.mobile, evt.cmd.name, evt.cmd.birthday, evt.cmd.createTime, evt.cmd.updateTime)))

	//更新用户
	private def updateUser(evt: Updated) = Future
	.successful(List(updatePre.bind(evt.cmd.mobile, evt.cmd.name, evt.cmd.birthday, evt.cmd.updateTime, evt.cmd.id)))

	//删除用户
	private def deleteUser(evt: EventStreamElement[Deleted.type]) = Future
	.successful(List(deletePre.bind(evt.entityId)))
}
