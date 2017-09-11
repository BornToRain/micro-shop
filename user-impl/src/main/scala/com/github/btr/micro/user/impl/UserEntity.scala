package com.github.btr.micro.user.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

/**
	* 用户持久化
	*/
class UserEntity extends PersistentEntity
{
	override type Command = UserCmd[_]
	override type Event = UserEvt
	override type State = UserState

	//初始状态 不存在
	override def initialState = UserState.nonexistence

	//状态行为
	override def behavior =
	{
		//不存在状态
		case UserState.nonexistence => get orElse nonexistence
		//正常状态
		case UserState(_, UserStatus.Normal) => get orElse existence orElse normal
		//冻结状态
		case UserState(_, UserStatus.Freeze) => get orElse existence orElse normal
	}

	//用户信息
	def get = Actions().onReadOnlyCommand[Get.type, Option[User]]
	{
		case (Get, ctx, state) => ctx.reply(state.data)
	}

	//已存在状态下无效的命令
	def existence = Actions()
	//创建命令
	.onReadOnlyCommand[Create, String]
	{
		case (_: Created, ctx, _) => ctx.invalidCommand("用户已存在!")
	}

	//不存在状态下操作
	def nonexistence = Actions()
	//处理创建命令
	.onCommand[Create, String]
	{
		//持久化创建事件回复创建Id
		case (cmd: Create, ctx, _) => ctx.thenPersist(Created(cmd))(e => ctx.reply(e.cmd.id))
	}
	//处理事件
	.onEvent
	{
		//创建聚合根
		case (Created(cmd), _) => UserState.create(User(cmd.id, cmd.mobile, cmd.name, cmd.birthday, cmd.createTime, cmd.updateTime))
	}

	//正常状态下操作
	def normal = Actions()
	//处理更新命令
	.onCommand[Update, Done]
	{
		//持久化更新事件回复完成
		case (cmd: Update, ctx, _) => ctx.thenPersist(Updated(cmd))(_ => ctx.reply(Done))
	}
	//处理事件
	.onEvent
	{
		case (e: Updated, state) =>
			//更新聚合根
		state.data.map(_.update(e))
		state
	}
}
