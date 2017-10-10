package com.github.btr.micro.user.api

import com.github.btr.micro.tool.JSONTool.enumFormat
import com.github.btr.micro.user.api.AddressStatus.AddressStatus
import com.github.btr.micro.user.api.AddressType.AddressType
import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}

/**
	* 接口层DTO对象
	*/
//用户创建DTO
case class CreateUser(mobile: String, name: Option[Name], age: Option[Int])

object CreateUser
{
	implicit val format: Format[CreateUser] = Json.format
}

//用户详情DTO
case class User(id: String, mobile: String, name: Option[Name], age: Option[Int],addresses:Map[String,Address],create_time:DateTime)

object User
{
	implicit val format: Format[User] = Json.format
}

//姓名值对象
case class Name(firstName: String, lastName: String, enName: Option[String])

object Name
{
	implicit val format: Format[Name] = Json.format
}

//用户收货地址创建DTO
case class CreateAddress(province: String, city: String, district: String, zipCode: Option[Int], street: String,
	`type`: AddressType)

object CreateAddress
{
	implicit val format: Format[CreateAddress] = Json.format
}

//用户收货地址详情DTO
case class Address(province: String, city: String, district: String, zipCode: Option[Int], street: String,
	status: AddressStatus,
	`type`: AddressType)

object Address
{
	implicit val format: Format[Address] = Json.format
}

//用户收货地址状态
object AddressStatus extends Enumeration
{
	//停用、启用
	type AddressStatus = Value
	val Stop, Use = Value

	implicit val format: Format[AddressStatus] = enumFormat(AddressStatus)
}

//用户收货地址类型
object AddressType extends Enumeration
{
	type AddressType = Value
	//家 公司 学校
	val Home, Company, School = Value

	implicit val format: Format[AddressType] = enumFormat(AddressType)
}
