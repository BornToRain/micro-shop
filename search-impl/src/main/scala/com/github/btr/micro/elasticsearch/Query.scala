package com.github.btr.micro.elasticsearch

import play.api.libs.json.{Format, Json}

/**
	* ElasticSearch查询条件构建
	*/
case class RootQuery
(
	from: Int,
	size: Int,
	query: Query
)

object RootQuery
{
	implicit val format: Format[RootQuery] = Json.format
}

/**
	* 查询条件构建
	*/
sealed trait Query

/** **************多字段查询 ****************/
//多字段查询
case class MultiQuery
(
	multi_match: MultiQueryFilter
) extends Query

object MultiQuery
{
	implicit val format: Format[MultiQuery] = Json.format
}

//多字段查询条件
case class MultiQueryFilter
(
	//查询内容
	query: String,
	//查询字段
	fields: Seq[String]
) extends Query

object MultiQueryFilter
{
	implicit val format: Format[MultiQueryFilter] = Json.format
}
/** **************多字段查询 ****************/
