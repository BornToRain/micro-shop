package com.github.btr.micro.elasticsearch

import play.api.libs.json.{Format, Json}

/**
	* ElasticSearch搜索结果
	*/
case class RootHits(hits: Hits)

object RootHits
{
	implicit val format: Format[RootHits] = Json.format
}

case class Hits(hits: Seq[HitResponse], total: Int)

object Hits
{
	implicit val format: Format[Hits] = Json.format
}

//数据列表
case class HitResponse(_source: IndexedProduct)

object HitResponse
{
	implicit val format: Format[HitResponse] = Json.format
}

