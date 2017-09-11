package com.github.btr.micro.user.impl

import com.github.btr.micro.tool.IdWorker
import com.github.btr.micro.user.api.UserService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext

/**
	* 用户接口实现
	*/
class UserServiceImpl(registry: PersistentEntityRegistry)(implicit ec:ExecutionContext) extends UserService
{

	import com.github.btr.micro.user.api

	override def info(id: String) = ServiceCall
	{
		_ =>
			//查询命令
		refFor(id).ask(Get).map
		{
			//转换接口响应DTO
			case Some(d) => api.User(d.id, d.mobile, d.name, d.birthday)
			//404 NotFound
			case _ => throw NotFound(s"ID为${id }的用户不存在")
		}
	}

	override def create = ServiceCall
	{
		d =>
		val id = IdWorker.getFlowIdWorkerInstance.nextSId
		//创建命令
		refFor(id).ask(Create(id, d.mobile, d.name, d.birthday))
	}

	override def update(id: String) = ServiceCall
	{
		//更新命令
		d => refFor(id).ask(Update(d.mobile, d.name, d.birthday))
	}

	override def delete(id: String) = ServiceCall
	{
		//删除命令
		_ => refFor(id).ask(Delete)
	}

	private def refFor(id: String) = registry.refFor[UserEntity](id)
}
