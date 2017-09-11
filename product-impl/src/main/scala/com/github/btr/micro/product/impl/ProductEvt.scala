package com.github.btr.micro.product.impl

import com.github.btr.micro.tool.JSONTool.singletonFormat
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}
import play.api.libs.json.{Format, Json}

/**
	* 商品事件
	*/
sealed trait ProductEvt extends AggregateEvent[ProductEvt]
{
	def aggregateTag = ProductEvt.tag
}

object ProductEvt
{
	//按事件数分片
	val numberShared = 3
	val tag          = AggregateEventTag.sharded[ProductEvt](numberShared)
}


//创建
case class Created(cmd: Create) extends ProductEvt

object Created
{
	implicit val format: Format[Created] = Json.format
}

//更新
case class Updated(cmd: Update) extends ProductEvt

object Updated
{
	implicit val format: Format[Updated] = Json.format
}

//删除
case object Deleted extends ProductEvt
{
	implicit val format: Format[Deleted.type] = singletonFormat(Deleted)
}

//下架
case object Hid extends ProductEvt
{
	implicit val format: Format[Hid.type] = singletonFormat(Hid)
}

//上架
case object Showed extends ProductEvt
{
	implicit val format: Format[Showed.type] = singletonFormat(Showed)
}
