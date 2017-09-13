package com.github.btr.micro.user.impl


import com.github.btr.micro.tool.JSONTool._
import play.api.libs.json.{Format, Json}

/**
	* 用户状态
	*/
object UserStatus extends Enumeration
{
	type Status = Value
	//不存在 注册 正常 冻结 删除
	val Nonexistence, Registration, Normal, Freeze, Deletion = Value

	implicit val format: Format[Status] = enumFormat(UserStatus)
}

case class UserState
(
	data: Option[User],
	status: UserStatus.Status
)
{
	//状态变更
	def changeStatus(status: UserStatus.Status) = copy(status = status)
}

object UserState
{
	implicit val format: Format[UserState] = Json.format
	//不存在
	lazy     val nonexistence              = UserState(None, UserStatus.Nonexistence)

	//创建
	def create(data: User) = UserState(Some(data), UserStatus.Normal)
}

/**
	* 收货地址状态
	*/
object AddressStatus extends Enumeration
{
	//停用、启用
	type Status = Value
	val Stop, Use = Value

	implicit val format: Format[Status] = enumFormat(AddressStatus)
}
