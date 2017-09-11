package com.github.btr.micro.product.api

import play.api.libs.json.{Format, Json}

/**
	* 商品DTO
	*/
sealed trait ProductDTO

case class Product(id: String, sellId: String, name: String, price: Double, illustrations: Option[String], `type`: Option[String]) extends ProductDTO

object Product
{
	implicit val format: Format[Product] = Json.format
}