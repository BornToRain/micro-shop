package com.github.btr.micro.product.impl

import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}

/**
	* 商品聚合根
	*/
case class Product
(
	id: String,
	sellId: String,
	name: String,
	price: Double,
	illustrations: Option[String],
	`type`: Option[String],
	createTime: DateTime = DateTime.now,
	updateTime: DateTime = DateTime.now
)

object Product
{
	implicit val format: Format[Product] = Json.format
}
