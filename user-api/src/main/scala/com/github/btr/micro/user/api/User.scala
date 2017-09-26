package com.github.btr.micro.user.api

import com.github.btr.micro.user.api
import org.joda.time.DateTime
import play.api.libs.json._

//用户创建DTO
case class Creation(mobile: String, name: Option[api.Name], age: Option[Int])

object Creation
{
	implicit val format: Format[Creation] = Json.format
}

//用户详情DTO
case class Info(id: String, mobile: String, name: Option[api.Name], age: Option[Int],addresses:Map[String,AddressInfo],create_time:DateTime)

object Info
{
	implicit val format: Format[Info] = Json.format
}
