package com.github.btr.micro.product.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

/**
	* 商品序列化
	*/
object ProductSerializerRegistry extends JsonSerializerRegistry
{
	def serializers = Vector(
		//命令
		JsonSerializer[Create],
		JsonSerializer[Get.type],
		JsonSerializer[Update],
		JsonSerializer[Delete.type],
		JsonSerializer[Show.type],
		JsonSerializer[Hide.type],
		//事件
		JsonSerializer[Created],
		JsonSerializer[Updated],
		JsonSerializer[Deleted.type],
		JsonSerializer[Showed.type],
		JsonSerializer[Hid.type],
		//状态
		JsonSerializer[ProductState]
	)
}
