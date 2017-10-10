package com.github.btr.micro.user.impl

import com.github.btr.micro.tool.JSONTool.singletonFormat
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}
import play.api.libs.json.{Format, Json}

/**
	* 领域事件集
	*/
sealed trait UserEvt extends AggregateEvent[UserEvt]
{
	def aggregateTag = UserEvt.tag
}

object UserEvt
{
	//按事件数分片
	val numberShared = 5
	val tag          = AggregateEventTag.sharded[UserEvt](numberShared)
}

//创建
case class CreatedUser(cmd: CreateUser) extends UserEvt

object CreatedUser
{
	implicit val format: Format[CreatedUser] = Json.format
}

//删除
case object DeletedUser extends UserEvt
{
	implicit val format: Format[DeletedUser.type] = singletonFormat(DeletedUser)
}

//创建用户收货地址
case class CreatedAddress(cmd: CreateAddress) extends UserEvt

object CreatedAddress
{
	implicit val format: Format[CreatedAddress] = Json.format
}

//更新用户收货地址
case class UpdatedAddress(cmd: UpdateAddress) extends UserEvt

object UpdatedAddress
{
	implicit val format: Format[UpdatedAddress] = Json.format
}

//删除用户收货地址
case class DeletedAddress(id:String) extends UserEvt

object DeletedAddress
{
	implicit val format: Format[DeletedAddress] = Json.format
}
