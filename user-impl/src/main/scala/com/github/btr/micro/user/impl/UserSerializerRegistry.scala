package com.github.btr.micro.user.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

/**
	* 序列化
	*/
object UserSerializerRegistry extends JsonSerializerRegistry
{
	def serializers = Vector(
		//用户命令
		JsonSerializer[Create],
		JsonSerializer[Get.type],
		JsonSerializer[Delete.type ],
		//用户收货地址命令
		JsonSerializer[CreateAddress],
		JsonSerializer[UpdateAddress],
		JsonSerializer[GetAddress],
		JsonSerializer[GetAddresses.type ],
		//用户事件
		JsonSerializer[Created],
		JsonSerializer[Deleted.type],
		//用户收货地址事件
		JsonSerializer[CreatedAddress],
		JsonSerializer[UpdatedAddress],
		//状态
		JsonSerializer[UserState]
	)

}
