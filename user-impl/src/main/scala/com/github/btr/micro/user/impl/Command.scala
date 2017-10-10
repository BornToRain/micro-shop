package com.github.btr.micro.user.impl

import akka.Done
import com.github.btr.micro.tool.JSONTool.singletonFormat
import com.github.btr.micro.user.impl.AddressType.AddressType
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}

/**
	* 领域命令集
	*/
sealed trait UserCmd[R] extends ReplyType[R]

//创建
case class CreateUser(id: String, mobile: String, name: Option[Name], age: Option[Int],
	createTime: DateTime = DateTime.now,
	updateTime: DateTime = DateTime.now) extends UserCmd[Done]

object CreateUser
{
	implicit val format: Format[CreateUser] = Json.format
}

//获取
case object GetUser extends UserCmd[Option[User]]
{
	implicit val format: Format[GetUser.type] = singletonFormat(GetUser)
}

//删除
case object DeleteUser extends UserCmd[Done]
{
	implicit val format: Format[DeleteUser.type] = singletonFormat(DeleteUser)
}

//创建用户收货地址
case class CreateAddress(userId: String, id: String, province: String, city: String, district: String, zipCode: Option[Int], street: String,
	addressType: AddressType,
	updateTime: DateTime = DateTime.now) extends UserCmd[Done]

object CreateAddress
{
	implicit val format: Format[CreateAddress] = Json.format
}

//获取用户收货地址列表
case object GetAddresses extends UserCmd[Map[String, Address]]
{
	implicit val format: Format[GetAddresses.type] = singletonFormat(GetAddresses)
}

//获取用户收货地址信息
case class GetAddress(id: String) extends UserCmd[Option[Address]]

object GetAddress
{
	implicit val format: Format[GetAddress] = Json.format
}

//更新用户收货地址
case class UpdateAddress(userId: String, id: String, province: String, city: String, district: String, zipCode: Option[Int], street: String,
	addressType: AddressType, updateTime: DateTime = DateTime.now) extends UserCmd[Done]

object UpdateAddress
{
	implicit val format: Format[UpdateAddress] = Json.format
}

//删除用户收货地址
case class DeleteAddress(id: String) extends UserCmd[Done]

object DeleteAddress
{
	implicit val format: Format[DeleteAddress] = Json.format
}
