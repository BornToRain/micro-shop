package com.github.btr.micro.search.impl

import com.github.btr.micro.elasticsearch._
import com.github.btr.micro.search.api.{SearchProduct, SearchResponse, SearchService}
import com.lightbend.lagom.scaladsl.api.ServiceCall

/**
	* 搜索服务实现
	*/
class SearchServiceImpl(indexedStore: IndexedStore[RootHits]) extends SearchService
{
	override def search(pageNo: Int, pageSize: Int) = ServiceCall
	{
		request =>
			//多字段查询条件构建
		request.keywords
		.map(MultiQueryFilter(_, Seq("name", "price", "type")))
		.map(MultiQuery(_))
		.map(RootQuery(pageNo, pageSize, _))
		//ElasticSearch库搜索
		.map(indexedStore.search)
		.map(_.map
		{
			response =>
			val data = response.hits.hits.map(_._source).collect
			{
				//库中索引商品数据转成api接口商品数据
				case IndexedProduct(id, Some(sellId), Some(name), Some(price), Some(t)) => SearchProduct(id, sellId, name, price, Some(t))
			}
			//搜索响应
			SearchResponse(data, pageNo, pageSize, response.hits.total)
		})
		.orNull
	}
}
