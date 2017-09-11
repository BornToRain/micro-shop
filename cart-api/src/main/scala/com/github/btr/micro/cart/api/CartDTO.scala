package com.github.btr.micro.cart.api

import play.api.libs.json.{Format, Json}

/**
	* 购物车DTO
	*/
sealed trait CartDTO

//用户个人购物车
case class UserCart(userId: String) extends CartDTO

object UserCart
{
	implicit val format: Format[UserCart] = Json.format
}