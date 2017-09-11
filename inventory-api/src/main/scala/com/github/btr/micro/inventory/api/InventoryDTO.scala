package com.github.btr.micro.inventory.api

import play.api.libs.json.{Format, Json}

/**
	* 库存DTO
	*/
sealed trait InventoryDTO

//库存更新
case class Inventory(productId: String, amount: Int) extends InventoryDTO

object Inventory
{
	implicit val format: Format[Inventory] = Json.format
}