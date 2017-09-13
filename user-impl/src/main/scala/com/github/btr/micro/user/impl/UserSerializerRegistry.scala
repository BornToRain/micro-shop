package com.github.btr.micro.user.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

/**
	* 序列化
	*/
object UserSerializerRegistry extends JsonSerializerRegistry
{
	def serializers = Vector(
		//命令
		JsonSerializer[Create],
		JsonSerializer[Get.type],
		JsonSerializer[Delete.type ],
		//事件
		JsonSerializer[Created],
		JsonSerializer[Deleted.type],
		//状态
		JsonSerializer[UserState]
	)

}
