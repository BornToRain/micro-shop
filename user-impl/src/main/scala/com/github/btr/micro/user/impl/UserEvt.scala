package com.github.btr.micro.user.impl

import com.github.btr.micro.tool.JSONTool._
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}
import play.api.libs.json.{Format, Json}

/**
	* 用户事件
	*/
sealed trait UserEvt extends AggregateEvent[UserEvt]
{
	def aggregateTag = UserEvt.tag
}

object UserEvt
{
	//按事件数分片
	val numberShared = 3
	val tag          = AggregateEventTag.sharded[UserEvt](numberShared)
}

//创建
case class Created(cmd: Create) extends UserEvt

object Created
{
	implicit val format: Format[Created] = Json.format
}

//删除
case object Deleted extends UserEvt
{
	implicit val format: Format[Deleted.type] = singletonFormat(Deleted)
}

//创建收货地址
case class CreatedAddress(cmd: CreateAddress) extends UserEvt

object CreatedAddress
{
	implicit val format: Format[CreatedAddress] = Json.format
}

