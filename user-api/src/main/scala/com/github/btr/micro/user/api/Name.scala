package com.github.btr.micro.user.api

import play.api.libs.json.{Format, Json}

//姓名值对象
case class Name(firstName: String, lastName: String, enName: Option[String])

object Name
{
	implicit val format: Format[Name] = Json.format
}

