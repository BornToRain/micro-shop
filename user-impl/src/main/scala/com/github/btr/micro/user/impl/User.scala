package com.github.btr.micro.user.impl

import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}

/**
	* 用户聚合根
	*/
case class User
(
	//唯一标识
	id: String,
	//手机号
	mobile: String,
	//姓
	name: Option[Name],
	//年龄
	age: Option[Int],
	//收货地址
	addresses: Seq[Address],
	createTime: DateTime,
	updateTime: DateTime
)
{
	//添加收货地址
	def addAddress(evt: CreatedAddress) =
	{
		val data = Address(evt.cmd.province, evt.cmd.city, evt.cmd.district, evt.cmd.zipCode, evt.cmd.street, AddressStatus.Use, evt.cmd.addressType)
		copy(addresses = addresses :+ data, updateTime = evt.cmd.updateTime)
	}

}

object User
{
	implicit val format: Format[User] = Json.format
}