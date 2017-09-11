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
	def info(id: String): ServiceCall[NotUsed, Product]

	//创建
	def create: ServiceCall[Product, String]

	//更新
	def update(id: String): ServiceCall[Product, Done]

	//删除
	def delete(id: String): ServiceCall[NotUsed, Done]


	import Service._

	def descriptor = named("products").withCalls(
		restCall(Method.GET, "/api/v1/products/:id", info _),
		restCall(Method.POST, "/api/v2/products", create),
		restCall(Method.PUT, "/api/v2/products/:id", update _),
		restCall(Method.DELETE, "/api/v2/products/:id", delete _)
	).withAutoAcl(true)
}
