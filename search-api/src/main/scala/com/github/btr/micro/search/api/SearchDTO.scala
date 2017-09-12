package com.github.btr.micro.search.api

import play.api.libs.json.{Format, Json}

/**
	* 搜索DTO
	*/
sealed trait SearchDTO

//关键词搜索请求
case class SearchRequest(keywords: Option[String]) extends SearchDTO

object SearchRequest
{
	implicit val format: Format[SearchRequest] = Json.format
}

//商品
case class SearchProduct(id: String, sellId: String, name: String, price: Double, `type`: Option[String]) extends SearchDTO

object SearchProduct
{
	implicit val format: Format[SearchProduct] = Json.format
}

//关键词搜索响应
case class SearchResponse(data: Seq[SearchProduct], pageNo: Int, pageSize: Int, numResults: Int)

object SearchResponse
{
	implicit val format: Format[SearchResponse] = Json.format
}
