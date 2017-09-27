package com.github.btr.micro.product.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
	* 商品接口定义
	*/
trait ProductService extends Service
{
	def get(id: String): ServiceCall[NotUsed, Info]
	def create: ServiceCall[Create, Done]
	def update(id: String): ServiceCall[Info, Done]
	def delete(id: String): ServiceCall[NotUsed, Done]

	import Service._

	def descriptor = named("products").withCalls(
		//商品信息
		restCall(Method.GET, "/api/products/v1/:id", get _),
		//商品创建
		restCall(Method.POST, "/api/products/v1", create),
		//商品更新
		restCall(Method.PUT, "/api/products/v1/:id", update _),
		//商品删除
		restCall(Method.DELETE, "/api/products/v1/:id", delete _)
	).withAutoAcl(true)
}
