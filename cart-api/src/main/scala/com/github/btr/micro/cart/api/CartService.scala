package com.github.btr.micro.cart.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
	* 购物车接口定义
	*/
trait CartService extends Service
{
	//用户个人购物车
	def cartByUser(userId: String): ServiceCall[NotUsed, UserCart]


	def descriptor =
	{
		import Service._

		named("carts").withCalls(
			restCall(Method.GET, "/api/v1/carts/:id", cartByUser _)
		).withAutoAcl(true)
	}
}
