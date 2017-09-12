package com.github.btr.micro.search.impl

import akka.Done
import com.github.btr.micro.elasticsearch.{IndexedProduct, RootQuery}

import scala.concurrent.Future

/**
	* 索引服务接口
	*/
trait IndexedStore[T]
{
	def store(document: IndexedProduct): Future[Done]

	def search(query: RootQuery): Future[T]
}
