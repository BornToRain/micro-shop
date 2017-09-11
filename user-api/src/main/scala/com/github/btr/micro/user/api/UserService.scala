package com.github.btr.micro.user.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
	* 用户接口定义
	*/
trait UserService extends Service
{
	//个人信息
	def info(id: String): ServiceCall[NotUsed, User]

	//创建用户
	def create: ServiceCall[User, String]

	//更新用户
	def update(id: String): ServiceCall[User, Done]

	//删除用户
	def delete(id: String): ServiceCall[NotUsed, Done]

	import Service._

	def descriptor = named("users").withCalls(
		restCall(Method.GET, "/api/v1/users/:id", info _),
		restCall(Method.POST, "/api/v1/users", create),
		restCall(Method.PUT, "/api/v1/users/:id", update _),
		restCall(Method.DELETE, "/api/v1/users/:id", delete _)
	).withAutoAcl(true)
}
