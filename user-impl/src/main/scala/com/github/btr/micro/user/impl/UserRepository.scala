package com.github.btr.micro.user.impl


import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}

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
				)
			""")
	} yield Done

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

	private def insertUser(evt: Created) = Future.successful(insertPre.bind(evt.cmd.id, evt.cmd.mobile, evt.cmd.name, evt.cmd.birthday))
}
