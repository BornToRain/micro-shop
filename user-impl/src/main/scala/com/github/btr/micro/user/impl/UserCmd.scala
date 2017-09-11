package com.github.btr.micro.user.impl

import akka.Done
import com.github.btr.micro.tool.JSONTool._
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import org.joda.time.{DateTime, LocalDate}
import play.api.libs.json.{Format, Json}

/**
	* 用户命令
	*/
sealed trait UserCmd[R] extends ReplyType[R]

//创建
case class Create(id: String, mobile: String, name: Option[String], birthday: Option[LocalDate], createTime: DateTime = DateTime.now,
	updateTime: DateTime = DateTime.now) extends UserCmd[String]

object Create
{
	implicit val format: Format[Create] = Json.format
}

//获取
case object Get extends UserCmd[Option[User]]
{
	implicit val format: Format[Get.type] = singletonFormat(Get)
}

//更新
case class Update(mobile: String, name: Option[String], birthday: Option[LocalDate], updateTime: DateTime = DateTime.now) extends UserCmd[Done]

object Update
{
	implicit val format: Format[Update] = Json.format
}

//删除
case object Delete extends UserCmd[Done]
{
	implicit val format: Format[Delete.type] = singletonFormat(Delete)
}