package com.github.btr.micro.elasticsearch

import play.api.libs.json.{Format, Json}

/**
	* 在ElasticSearch有索引的商品
	*/
case class IndexedProduct(id: String, sellId: Option[String], name: Option[String], price: Option[Double], `type`: Option[String])

object IndexedProduct
{
	implicit val format: Format[IndexedProduct] = Json.format
}