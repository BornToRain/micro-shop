package com.github.btr.micro.cart.api

import play.api.libs.json.{Format, Json}

//购物车DTO
case class Info(id: String, userId: String)

object Info
{
	implicit val format: Format[Info] = Json.format
}