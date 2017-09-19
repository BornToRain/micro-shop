package com.github.btr.micro.user.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
	* 用户接口定义
	*/
trait UserService extends Service
{
	def info(id: String): ServiceCall[NotUsed, Info]

	def creation: ServiceCall[Creation, Done]

	def deletion(id: String): ServiceCall[NotUsed, Done]

	def address(userId: String): ServiceCall[NotUsed, Map[String, AddressInfo]]

	def addressCreation(userId: String): ServiceCall[AddressCreation, Done]

	def addressInfo(userId: String, id: String): ServiceCall[NotUsed, AddressInfo]

	def addressUpdate(userId: String, id: String): ServiceCall[AddressInfo, Done]

	import Service._

	def descriptor = named("users").withCalls(
		//个人信息
		restCall(Method.GET, "/api/users/v1/:id", info _),
		//创建用户
		restCall(Method.POST, "/api/users/v1", creation),
		//删除用户
		restCall(Method.DELETE, "/api/users/v1/:id", deletion _),
		//用户收货地址列表
		restCall(Method.GET, "/api/users/v1/:userId/addresses", address _),
		//创建用户收货地址
		restCall(Method.POST, "/api/users/v1/:userId/addresses", addressCreation _),
		//用户收货地址信息
		restCall(Method.GET, "/api/users/v1/:userId/addresses/:id", addressInfo _),
		//更新用户收货地址
		restCall(Method.POST, "/api/users/v1/:userId/addresses/:id", addressUpdate _)
	).withAutoAcl(true)
}



