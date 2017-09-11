package com.github.btr.micro.user.api

import org.joda.time.LocalDate
import play.api.libs.json.{Format, Json}

sealed trait UserDTO

case class User(id:String,mobile:String,name:Option[String],birthday:Option[LocalDate]) extends UserDTO

object User
{
	implicit val format:Format[User] = Json.format
}