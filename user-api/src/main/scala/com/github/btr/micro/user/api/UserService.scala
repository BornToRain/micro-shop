package com.github.btr.micro.user.api

import akka.{Done, NotUsed}
import com.github.btr.micro.tool.JSONTool._
import com.github.btr.micro.user.api
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

/**
	* 用户接口定义
	*/
trait UserService extends Service
{
	//个人信息
	def info(id: String): ServiceCall[NotUsed, Info]

	//创建用户
	def creation: ServiceCall[Creation, Done]

	//删除用户
	def deletion(id: String): ServiceCall[NotUsed, Done]

	//创建收货地址
	def addressCreation(userId: String): ServiceCall[AddressCreation, Done]

	import Service._

	def descriptor = named(serviceDescriptor._1).withCalls(
		restCall(Method.GET, serviceDescriptor._2 + "/:id", info _),
		restCall(Method.POST, serviceDescriptor._2, creation),
		restCall(Method.DELETE, serviceDescriptor._2 + "/:id", deletion _),
		restCall(Method.POST, serviceDescriptor._2 + "/:userId/addresses", addressCreation _)
	).withAutoAcl(true)
}

//姓名值对象
case class Name(firstName: String, lastName: String, enName: Option[String])

object Name
{
	implicit val format: Format[Name] = Json.format
}

//用户创建DTO
case class Creation(mobile: String, name: Option[api.Name], age: Option[Int])

object Creation
{
	implicit val format: Format[Creation] = Json.format
}

//用户详情DTO
case class Info(id: String, mobile: String, name: Option[api.Name], age: Option[Int])

object Info
{
	implicit val format: Format[Info] = Json.format
}

//用户地址创建DTO
case class AddressCreation(userId: String, province: String, city: String, district: String, zipCode: Option[Int], street: String,
	`type`: api.AddressType.Type)

object AddressCreation
{
	implicit val format: Format[AddressCreation] = Json.format
}

//收货地址状态
object AddressStatus extends Enumeration
{
	//停用、启用
	type Status = Value
	val Stop, Use = Value

	implicit val format: Format[Status] = enumFormat(AddressStatus)
}

//收货地址类型
object AddressType extends Enumeration
{
	type Type = Value
	//家 公司 学校
	val Home, Company, School = Value

	implicit val format: Format[Type] = enumFormat(AddressType)
}
