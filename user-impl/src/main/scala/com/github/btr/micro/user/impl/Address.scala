package com.github.btr.micro.user.impl

import com.github.btr.micro.tool.JSONTool.enumFormat
import play.api.libs.json.{Format, Json}

/**
	* 用户收货地址值对象
	*/
case class Address
(
	//省
	province: String,
	//市
	city: String,
	//区
	district: String,
	//邮政编码
	zipCode: Option[Int],
	//街道
	street: String,
	//状态
	status: AddressStatus.Status,
	//类型 家 公司 学校。。。。。。
	`type`: AddressType.Type
)

object Address
{
	implicit val format: Format[Address] = Json.format
}


/**
	* 收货地址状态
	*/
object AddressStatus extends Enumeration
{
	//停用、启用
	type Status = Value
	val Stop, Use = Value

	implicit val format: Format[Status] = enumFormat(AddressStatus)
}

/**
	* 收货地址类型
	*/
object AddressType extends Enumeration
{
	type Type = Value
	//家 公司 学校
	val Home, Company, School = Value

	implicit val format: Format[Type] = enumFormat(AddressType)
}
