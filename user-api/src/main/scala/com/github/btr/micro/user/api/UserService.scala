package com.github.btr.micro.user.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
	* 用户接口定义
	*/
trait UserService extends Service
{
	def getUsers: ServiceCall[NotUsed, Seq[Info]]
	def getUser(id: String): ServiceCall[NotUsed, Info]
	def createUser: ServiceCall[Creation, Done]
	def deleteUser(id: String): ServiceCall[NotUsed, Done]
	def getAddresses(userId: String): ServiceCall[NotUsed, Map[String, AddressInfo]]
	def getAddress(id: String): ServiceCall[NotUsed, AddressInfo]
	def createAddresses: ServiceCall[AddressCreation, Done]
	def updateAddress(id: String): ServiceCall[AddressInfo, Done]
	def deleteAddress(id: String): ServiceCall[NotUsed, Done]

	import Service._

	def descriptor = named("users").withCalls(
		//用户列表
		restCall(Method.GET, "/api/users/v1", getUsers),
		//个人信息
		restCall(Method.GET, "/api/users/v1/:id", getUser _),
		//创建用户
		restCall(Method.POST, "/api/users/v1", createUser),
		//删除用户
		restCall(Method.DELETE, "/api/users/v1/:id", deleteUser _),
		//用户收货地址列表
		restCall(Method.GET, "/api/users/v1/user-addresses?userId", getAddresses _),
		//用户收货地址信息
		restCall(Method.GET, "/api/users/v1/user-addresses/:id", getAddress _),
		//添加用户收货地址
		restCall(Method.POST, "/api/users/v1/user-addresses", createAddresses),
		//更新用户收货地址
		restCall(Method.POST, "/api/users/v1/user-addresses/:id", updateAddress _),
		//删除用户收货地址
		restCall(Method.DELETE, "/api/users/v1/user-addresses/:id", deleteAddress _)
	).withAutoAcl(true)
}



