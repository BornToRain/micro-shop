package com.github.btr.micro.product.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
	* 商品接口定义
	*/
trait ProductService extends Service
{
	//商品信息
	def info(id: String): ServiceCall[NotUsed, Info]

	//创建
	def creation: ServiceCall[Create, Done]

	//更新
	def update(id: String): ServiceCall[Info, Done]

	//删除
	def deletion(id: String): ServiceCall[NotUsed, Done]


	import Service._

	def descriptor = named("products").withCalls(
		restCall(Method.GET, "/api/products/v1/:id", info _),
		restCall(Method.POST, "/api/products/v1", creation),
		restCall(Method.PUT, "/api/products/v1/:id", update _),
		restCall(Method.DELETE, "/api/products/v1/:id", deletion _)
	).withAutoAcl(true)
}
