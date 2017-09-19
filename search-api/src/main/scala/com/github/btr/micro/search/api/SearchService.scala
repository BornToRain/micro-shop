package com.github.btr.micro.search.api

import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
	* 搜索接口定义
	*/
trait SearchService extends Service
{
	//关键词搜索商品
	def search(pageNo: Int, pageSize: Int): ServiceCall[SearchRequest, SearchResponse]

	import Service._

	def descriptor = named("search").withCalls(
		restCall(Method.GET, "/api/search/v1?pageNo&pageSize", search _)
	).withAutoAcl(true)
}
