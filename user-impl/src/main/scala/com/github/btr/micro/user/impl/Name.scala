package com.github.btr.micro.user.impl

import play.api.libs.json.{Format, Json}

/**
	* 用户姓名值对象
	*/
case class Name
(
	//姓
	firstName: String,
	//名
	lastName: String,
	//英文名
	enName: Option[String]
)

object Name
{
	implicit val format: Format[Name] = Json.format
}
