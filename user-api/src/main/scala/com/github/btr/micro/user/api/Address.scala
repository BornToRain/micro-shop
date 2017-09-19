package com.github.btr.micro.user.api

import com.github.btr.micro.tool.JSONTool.enumFormat
import com.github.btr.micro.user.api
import play.api.libs.json.{Format, Json}

//用户收货地址创建DTO
case class AddressCreation(province: String, city: String, district: String, zipCode: Option[Int], street: String,
	`type`: api.AddressType.Type)

object AddressCreation
{
	implicit val format: Format[AddressCreation] = Json.format
}

//用户收货地址详情DTO
case class AddressInfo(province: String, city: String, district: String, zipCode: Option[Int], street: String,
	status: AddressStatus.Status,
	`type`: api.AddressType.Type)

object AddressInfo
{
	implicit val format: Format[AddressInfo] = Json.format
}

//用户收货地址状态
object AddressStatus extends Enumeration
{
	//停用、启用
	type Status = Value
	val Stop, Use = Value

	implicit val format: Format[Status] = enumFormat(AddressStatus)
}

//用户收货地址类型
object AddressType extends Enumeration
{
	type Type = Value
	//家 公司 学校
	val Home, Company, School = Value

	implicit val format: Format[Type] = enumFormat(AddressType)
}
