package com.github.btr.micro.user.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

/**
	* 序列化
	*/
object SerializerRegistry extends JsonSerializerRegistry
{
	def serializers = Vector(
		//用户命令
		JsonSerializer[CreateUser],
		JsonSerializer[GetUser.type],
		JsonSerializer[DeleteUser.type],
		//用户收货地址命令
		JsonSerializer[CreateAddress],
		JsonSerializer[UpdateAddress],
		JsonSerializer[GetAddress],
		JsonSerializer[GetAddresses.type],
		//用户事件
		JsonSerializer[CreatedUser],
		JsonSerializer[DeletedUser.type],
		//用户收货地址事件
		JsonSerializer[CreatedAddress],
		JsonSerializer[UpdatedAddress],
		//状态
		JsonSerializer[UserState]
	)

}
