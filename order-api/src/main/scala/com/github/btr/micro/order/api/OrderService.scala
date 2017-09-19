//package com.github.btr.micro.order.api
//
//import akka.{Done, NotUsed}
//import com.lightbend.lagom.scaladsl.api.transport.Method
//import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
//
///**
//	* 订单接口定义
//	*/
//trait OrderService extends Service
//{
//	def info(id: String): ServiceCall[NotUsed, Create]
//
//	def creation: ServiceCall[NotUsed, Done]
//
//	import Service._
//
//	def descriptor = named("orders").withCalls(
//		restCall(Method.GET, "/api/orders/v1/:id", info _),
//		restCall(Method.POST, "/api/orders/v1", creation)
//	).withAutoAcl(true)
//}
