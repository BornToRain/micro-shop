package com.github.btr.micro.user.api

import akka.{Done, NotUsed}
import com.github.btr.micro.tool.ServiceDescriptor
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
	* 用户接口定义
	*/
trait UserService extends Service
{
	val sd = ServiceDescriptor("users", "v1")

	//个人信息
	def info(id: String): ServiceCall[NotUsed, User]

	//创建用户
	def create: ServiceCall[User, String]

	//更新用户
	def update(id: String): ServiceCall[User, Done]

	//删除用户
	def delete(id: String): ServiceCall[NotUsed, Done]

	import Service._

	def descriptor = named(sd.name).withCalls(
		restCall(Method.GET, sd.versionURI("/:id"), info _),
		restCall(Method.POST, sd.versionURI(), create),
		restCall(Method.PUT, sd.versionURI("/:id"), update _),
		restCall(Method.DELETE, sd.versionURI("/:id"), delete _)
	).withAutoAcl(true)
}
