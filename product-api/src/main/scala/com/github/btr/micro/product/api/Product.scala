package com.github.btr.micro.product.api

import play.api.libs.json.{Format, Json}

//商品创建DTO
case class Create(sellId: String, name: String, price: Double, illustrations: Option[String], `type`: Option[String])

object Create
{
	implicit val format: Format[Create] = Json.format
}

//商品详情DTO
case class Info(id: String, sellId: String, name: String, price: Double, illustrations: Option[String], `type`: Option[String])

object Info
{
	implicit val format: Format[Info] = Json.format
}