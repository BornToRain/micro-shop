package com.github.btr.micro.order.api

import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
	* 订单接口定义
	*/
trait OrderService extends Service
{
	def creation:ServiceCall
}
