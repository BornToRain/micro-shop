package com.github.btr.micro.search.api

import com.github.btr.micro.tool.ServiceDescriptor
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
	* 搜索接口定义
	*/
trait SearchService extends Service
{
	val sd = ServiceDescriptor("search", "v1")

	//关键词搜索商品
	def search(pageNo: Int, pageSize: Int): ServiceCall[SearchRequest, SearchResponse]

	import Service._

	def descriptor = named(sd.name).withCalls(
		restCall(Method.GET, sd.versionURI("?pageNo&pageSize"), search _)
	).withAutoAcl(true)
}
