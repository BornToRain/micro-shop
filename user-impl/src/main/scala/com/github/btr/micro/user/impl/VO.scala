package com.github.btr.micro.user.impl

import com.github.btr.micro.tool.JSONTool._
import com.github.btr.micro.user.impl.AddressStatus.AddressStatus
import com.github.btr.micro.user.impl.AddressType.AddressType
import play.api.libs.json._

/**
	* 领域值对象
	*/
//姓名
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

//收货地址
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
	status: AddressStatus,
	//类型 家 公司 学校。。。。。。
	`type`: AddressType
)

object Address
{
	implicit val format: Format[Address] = Json.format
}

//收货地址状态
object AddressStatus extends Enumeration
{
	type AddressStatus = Value
	//停用、启用
	val Stop, Use = Value

	implicit val format: Format[AddressStatus] = enumFormat(AddressStatus)
}

//收货地址类型
object AddressType extends Enumeration
{
	type AddressType = Value
	//家 公司 学校
	val Home, Company, School = Value

	implicit val format: Format[AddressType] = enumFormat(AddressType)
}

