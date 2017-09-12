package com.github.btr.micro.product.api

import akka.{Done, NotUsed}
import com.github.btr.micro.tool.ServiceDescriptor
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
	* 商品接口定义
	*/
trait ProductService extends Service
{
	val sd = ServiceDescriptor("products", "v1")

	//商品信息
	def info(id: String): ServiceCall[NotUsed, Product]

	//创建
	def create: ServiceCall[Product, String]

	//更新
	def update(id: String): ServiceCall[Product, Done]

	//删除
	def delete(id: String): ServiceCall[NotUsed, Done]


	import Service._

	def descriptor = named(sd.name).withCalls(
		restCall(Method.GET, sd.versionURI("/:id"), info _),
		restCall(Method.POST, sd.versionURI(), create),
		restCall(Method.PUT, sd.versionURI("/:id"), update _),
		restCall(Method.DELETE, sd.versionURI("/:id"), delete _)
	).withAutoAcl(true)
}
