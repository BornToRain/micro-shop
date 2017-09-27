package com.github.btr.micro.cart.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
	* 购物车接口定义
	*/
trait CartService extends Service
{

	import com.github.btr.micro.product.{api => product}

	def get(id: String): ServiceCall[NotUsed, Info]
	def delete(id:String):ServiceCall[NotUsed,Done]
	def addProduct(id: String): ServiceCall[product.Info, Done]
	def deleteProduct(id:String):ServiceCall[NotUsed,Done]
	def getUserCart(userId: String): ServiceCall[NotUsed, Seq[Info]]

	import Service._

	def descriptor = named("carts").withCalls(
		//购物车信息
		restCall(Method.GET, "/api/carts/v1/:id", get _),
		//删除购物车
		restCall(Method.DELETE,"/api/carts/v1/:id", delete _),
		//用户个人购物车
		restCall(Method.GET, "/api/carts/v1?userId", getUserCart _),
		//添加商品到购物车
		restCall(Method.POST, "/api/carts/v1/cart-products/:id", addProduct _),
		//从购物车移除商品
		restCall(Method.DELETE, "/api/carts/v1/cart-products/:id",deleteProduct _)
	).withAutoAcl(true)
}
