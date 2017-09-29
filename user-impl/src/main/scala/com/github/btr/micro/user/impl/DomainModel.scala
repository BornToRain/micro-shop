package com.github.btr.micro.user.impl

import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}

/**
	* 领域模型
	*/
//用户聚合根
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
	//用户收货地址
	addresses: Map[String, Address],
	createTime: DateTime,
	updateTime: DateTime
)
{
	//添加收货地址
	def addAddress(evt: CreatedAddress) =
	{
		val data = Address(evt.cmd.province, evt.cmd.city, evt.cmd.district, evt.cmd.zipCode, evt.cmd.street, AddressStatus.Use, evt.cmd.addressType)

		copy(
			addresses = Map(evt.cmd.id -> data),
			updateTime = evt.cmd.updateTime
		)
	}

	//更新收货地址
	def updateAddress(evt: UpdatedAddress) =
	{
		//当前地址信息
		val address = addresses(evt.cmd.id)

		//更新地址
		val data = address.copy(
			province = evt.cmd.province,
			city = evt.cmd.city,
			district = evt.cmd.district,
			zipCode = evt.cmd.zipCode,
			street = evt.cmd.street,
			`type` = evt.cmd.addressType
		)

		copy(addresses = addresses + (evt.cmd.id -> data), updateTime = evt.cmd.updateTime)
	}

	//删除收货地址
	def deleteAddress(evt:DeletedAddress) =
	{
		//删除地址
		addresses - evt.id
	}
}

object User
{
	implicit val format: Format[User] = Json.format
}