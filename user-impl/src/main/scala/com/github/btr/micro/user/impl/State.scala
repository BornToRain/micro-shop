package com.github.btr.micro.user.impl

import com.github.btr.micro.tool.JSONTool.enumFormat
import play.api.libs.json.{Format, Json}

/**
	* 领域状态
	*/
//用户状态
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

