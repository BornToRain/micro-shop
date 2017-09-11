package com.github.btr.micro.user.impl

import org.joda.time.{DateTime, LocalDate}
import play.api.libs.json.{Format, Json}

/**
	* 用户聚合根
	*/
case class User
(
	id: String,
	mobile: String,
	name: Option[String],
	birthday: Option[LocalDate],
	createTime: DateTime,
	updateTime: DateTime
)
{
	//更新
	def update(evt: Updated) = copy(
		mobile = evt.cmd.mobile,
		name = evt.cmd.name,
		birthday = evt.cmd.birthday,
		updateTime = evt.cmd.updateTime
	)
}

object User
{
	implicit val format: Format[User] = Json.format
}