package com.github.btr.micro.user.impl

import akka.Done
import com.github.btr.micro.tool.{Api, IdWorker, Restful}
import com.github.btr.micro.user.api.UserService
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.server.ServerServiceCall

import scala.concurrent.ExecutionContext

/**
	* 用户接口实现
	*/
class UserServiceImpl(registry: PersistentEntityRegistry)(implicit ec: ExecutionContext) extends UserService with Api
{

	import com.github.btr.micro.user.api

	override def info(id: String) = logged(ServerServiceCall
	{
		_ => //查询命令
		refFor(id).ask(Get).map
		{
			//转换接口响应DTO
			case Some(d) => api.Info(d.id, d.mobile, d.name, d.age,d.addresses)
			//404 NotFound
			case _ => throw NotFound(s"ID为${id }的用户不存在")
		}
	})

	override def creation = logged(ServerServiceCall
	{
		(request, d) => //创建命令
		val id = IdWorker.getFlowIdWorkerInstance.nextSId
		refFor(id).ask(Create(id, d.mobile, d.name, d.age))
		.map(_ => (Restful.created(request)(id), Done))
	})

	override def deletion(id: String) = logged(ServerServiceCall
	{
		(_, _) => //删除命令
		refFor(id).ask(Delete)
		.map(_ => (Restful.noContent, Done))
	})

	override def addressCreation(userId: String) = logged(ServerServiceCall
	{
		(request, d) => //创建收货地址命令
		val id = IdWorker.getFlowIdWorkerInstance.nextSId
		refFor(userId).ask(CreateAddress(userId, id, d.province, d.city, d.district, d.zipCode, d.street, d.`type`))
		.map(_ => (Restful.created(request)(id), Done))
	})

	override def address(userId: String) = logged(ServerServiceCall
	{
		_ => //查询用户收货地址列表
		refFor(userId).ask(GetAddresses).map(toApiAddresses)
	})

	override def addressInfo(userId: String, id: String) = logged(ServerServiceCall
	{
		_ => //查询用户收货地址信息
		refFor(userId).ask(GetAddress(id)).map
		{
			//转换接口响应DTO
			case Some(d) => toApiAddress(d)
			//404 NotFound
			case _ => throw NotFound(s"ID为${id }的收货地址不存在")
		}
	})

	override def addressUpdate(userId: String, id: String) = logged(ServerServiceCall
	{
		d => //更新用户收货地址
		refFor(userId).ask(UpdateAddress(userId, id, d.province, d.city, d.district, d.zipCode, d.street, d.`type`))
		.map(_ => Done)
	})

	private def refFor(id: String) = registry.refFor[UserEntity](id)
}
