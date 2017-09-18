package com.github.btr.micro.order.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
	* 订单接口定义
	*/
trait OrderService extends Service
{
	def info(id: String): ServiceCall[NotUsed, Create]

	def creation: ServiceCall[NotUsed, Done]

	import Service._

	def descriptor = named(serviceDescriptor._1).withCalls(
		restCall(Method.GET, serviceDescriptor._2 + "/:id", info _),
		restCall(Method.POST, serviceDescriptor._2, creation)
//		restCall(Method.DELETE, serviceDescriptor._2 + "/:id", deletion _),
//		restCall(Method.POST, serviceDescriptor._2 + "/:userId/addresses", addressCreation _)
	).withAutoAcl(true)
}
