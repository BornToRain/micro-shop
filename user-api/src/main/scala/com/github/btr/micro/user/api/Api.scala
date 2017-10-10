package com.github.btr.micro.user.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
	* 用户接口定义
	*/
trait UserService extends Service
{
	def getUsers: ServiceCall[NotUsed, Seq[User]]
	def getUser(id: String): ServiceCall[NotUsed, User]
	def createUser: ServiceCall[CreateUser, Done]
	def deleteUser(id: String): ServiceCall[NotUsed, Done]
	def getAddresses(userId: String): ServiceCall[NotUsed, Map[String, Address]]
	def getAddress(userId: String, id: String): ServiceCall[NotUsed, Address]
	def createAddress(userId: String): ServiceCall[CreateAddress, Done]
	def updateAddress(userId: String, id: String): ServiceCall[Address, Done]
	def deleteAddress(userId: String, id: String): ServiceCall[NotUsed, Done]

	import Service._

	def descriptor = named("users").withCalls(
		//用户列表
		restCall(Method.GET, "/users", getUsers),
		//个人信息
		restCall(Method.GET, "/users/:id", getUser _),
		//创建用户
		restCall(Method.POST, "/users", createUser),
		//删除用户
		restCall(Method.DELETE, "/users/:id", deleteUser _),
		//用户收货地址列表
		restCall(Method.GET, "/users/:userId/addresses", getAddresses _),
		//用户收货地址信息
		restCall(Method.GET, "/users/:userId/addresses/:id", getAddress _),
		//添加用户收货地址
		restCall(Method.POST, "/users/:userId/addresses", createAddress _),
		//更新用户收货地址
		restCall(Method.POST, "/users/:userId/addresses/:id", updateAddress _),
		//删除用户收货地址
		restCall(Method.DELETE, "/users/:userId/addresses/:id", deleteAddress _)
	).withAutoAcl(true)
}
