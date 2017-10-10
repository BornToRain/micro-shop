package com.github.btr.micro.user.impl

import akka.Done
import com.github.btr.micro.tool.{Api, IdWorker, Restful}
import com.github.btr.micro.user.api
import com.github.btr.micro.user.api.UserService
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.server.ServerServiceCall

import scala.concurrent.{ExecutionContext, Future}

/**
	* 用户接口实现
	*/
class UserServiceImpl(registry: PersistentEntityRegistry, repository: UserRepository)(implicit ec: ExecutionContext) extends UserService with Api
{
	override def getUsers = v1(ServerServiceCall
	{
		_ => //查询命令
		for
		{
			list <- repository.gets
			data <- Future(list.map(_.toApi[api.User]))
		} yield data
	})

	override def getUser(id: String) = v1(ServerServiceCall
	{
		_ => //查询命令
		refFor(id).ask(GetUser).map
		{
			//转换接口响应DTO
			case Some(data) => data.toApi
			//404 NotFound
			case _ => throw NotFound(s"ID为${id }的用户不存在")
		}
	})

	override def createUser = v1(ServerServiceCall
	{
		(request, d) => //创建命令
		val id = IdWorker.getFlowIdWorkerInstance.nextSId
		refFor(id).ask(CreateUser(id, d.mobile, d.name.toDomain, d.age))
		.map(_ => Restful.created(request)(id))
	})

	override def deleteUser(id: String) = v1(ServerServiceCall
	{
		(_, _) => //删除命令
		refFor(id).ask(DeleteUser)
		.map(_ => Restful.noContent)
	})

	override def getAddresses(userId: String) = v1(ServerServiceCall
	{
		_ => //查询用户收货地址列表
		refFor(userId).ask(GetAddresses).map(_.toApi[Map[String, api.Address]])
	})

	override def getAddress(userId: String, id: String) = v1(ServerServiceCall
	{
		_ => //查询用户收货地址信息
		refFor(userId).ask(GetAddress(id)).map
		{
			//转换接口响应DTO
			case Some(data) => data.toApi
			//404 NotFound
			case _ => throw NotFound(s"ID为${id }的收货地址不存在")
		}
	})

	override def createAddress(userId: String) = v1(ServerServiceCall
	{
		(request, d) => //创建收货地址命令
		val id = IdWorker.getFlowIdWorkerInstance.nextSId
		refFor(userId).ask(CreateAddress(userId, id, d.province, d.city, d.district, d.zipCode, d.street, d.`type`.toDomain))
		.map(_ => Restful.created(request)(id))
	})

	override def updateAddress(userId: String, id: String) = v1(ServerServiceCall
	{
		d => //更新用户收货地址
		refFor(userId).ask(UpdateAddress(userId, id, d.province, d.city, d.district, d.zipCode, d.street, d.`type`.toDomain))
		.map(_ => Done)
	})

	override def deleteAddress(userId: String, id: String) = v1(ServerServiceCall
	{
		(_, _) => //删除用户收货地址
		refFor(userId).ask(DeleteAddress(id))
		.map(_ => Restful.noContent)
	})

	private def refFor(id: String) = registry.refFor[UserEntity](id)
}
