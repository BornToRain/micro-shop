package com.github.btr.micro.inventory.api

import akka.Done
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.Method

/**
	* 库存接口定义
	*/
trait InventoryService
{
	//增加商品库存
	def increase: ServiceCall[Inventory, Done]

	//减少商品库存
	def decrease: ServiceCall[Inventory, Done]

	import com.lightbend.lagom.scaladsl.api.Service._

	def descriptor = named("inventories").withCalls(
		restCall(Method.POST, "/api/inventories/v1/increase", increase),
		restCall(Method.POST, "/api/inventories/v1/decrease", decrease)
	).withAutoAcl(true)
}
