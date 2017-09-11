package com.github.btr.micro.product.impl

import play.api.libs.json.{Format, Json}

/**
	* 商品状态
	*/
object ProductStatus extends Enumeration
{
	type Status = Value
	//不存在 下架 上架 售罄
	val Nonexistence, Hide, Show, Finish = Value

	import com.github.btr.micro.tool.JSONTool.enumFormat

	implicit val format: Format[Status] = enumFormat(ProductStatus)
}

case class ProductState
(
	data: Option[Product],
	status: ProductStatus.Status
)
{
	//状态变更
	def changeStatus(status: ProductStatus.Status) = copy(status = status)
}

object ProductState
{
	implicit val format: Format[ProductState] = Json.format
	//不存在
	lazy     val nonexistence                 = ProductState(None, ProductStatus.Nonexistence)

	//创建
	def create(data: Product) = ProductState(Some(data), ProductStatus.Hide)
}
