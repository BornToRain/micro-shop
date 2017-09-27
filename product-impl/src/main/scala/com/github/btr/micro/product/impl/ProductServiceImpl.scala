package com.github.btr.micro.product.impl

import com.github.btr.micro.product.api
import com.github.btr.micro.product.api.ProductService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext

/**
	* 商品接口实现
	*/
class ProductServiceImpl(registry: PersistentEntityRegistry)(implicit ec: ExecutionContext) extends ProductService
{
	override def get(id: String) = ServiceCall
	{
		_ =>
			//查询命令
		refFor(id).ask(Get).map
		{
			//转换接口响应DTO
			case Some(d) => api.Info(d.id, d.sellId, d.name, d.price, d.illustrations, d.`type`)
			//404 NotFound
			case _ => throw NotFound(s"ID为${id }的商品不存在")
		}
	}
	override def create = ???
	override def update(id: String) = ???
	override def delete(id: String) = ???

	private def refFor(id: String) = registry.refFor[ProductEntity](id)
}
