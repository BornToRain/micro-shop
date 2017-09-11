package com.github.btr.micro.product.impl

import akka.Done
import com.github.btr.micro.tool.JSONTool.singletonFormat
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}

/**
	* 商品命令
	*/
sealed trait ProductCmd[R] extends ReplyType[R]

//创建
case class Create(id: String, sellId: String, name: String, price: Double, illustrations: Option[String], `type`: Option[String])
extends ProductCmd[String]

object Create
{
	implicit val format: Format[Create] = Json.format
}

//获取
case object Get extends ProductCmd[Option[Product]]
{
	implicit val format: Format[Get.type] = singletonFormat(Get)
}

//更新
case class Update(sellId: String, name: String, price: Double, illustrations: Option[String], `type`: Option[String]) extends ProductCmd[Done]

object Update
{
	implicit val format: Format[Update] = Json.format
}

//删除
case object Delete extends ProductCmd[Done]
{
	implicit val format: Format[Delete.type] = singletonFormat(Delete)
}

//下架
case object Hide extends ProductCmd[Done]
{
	implicit val format: Format[Hide.type] = singletonFormat(Hide)
}

//上架
case object Show extends ProductCmd[Done]
{
	implicit val format: Format[Show.type] = singletonFormat(Show)
}