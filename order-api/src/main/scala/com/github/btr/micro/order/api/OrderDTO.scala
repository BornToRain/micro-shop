package com.github.btr.micro.order.api

/**
	* 订单DTO
	*/
sealed trait OrderDTO

//下单
case class Create(name:String)