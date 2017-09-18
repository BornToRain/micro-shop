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
	.onReadOnlyCommand[Create, Done]
	{
		case (_: Created, ctx, _) => ctx.invalidCommand("用户已存在!")
	}

	//不存在状态下操作
	def nonexistence = Actions()
	//处理创建命令
	.onCommand[Create, Done]
	{
		//持久化创建事件回复创建Id
		case (cmd: Create, ctx, _) => ctx.thenPersist(Created(cmd))(_ => ctx.reply(Done))
	}
	//处理事件
	.onEvent
	{
		//创建聚合根
		case (Created(cmd), _) => UserState.create(User(cmd.id, cmd.mobile, cmd.name, cmd.age, Nil, cmd.createTime, cmd.updateTime))
	}

	//正常状态下操作
	def normal = Actions()
	//处理删除命令
	.onCommand[Delete.type, Done]
	{
		//持久化删除事件回复完成
		case (Delete, ctx, _) => ctx.thenPersist(Deleted)(_ => ctx.reply(Done))
	}
	//处理创建收货地址命令
	.onCommand[CreateAddress, Done]
	{
		//持久化创建收货地址事件回复完成
		case (cmd: CreateAddress, ctx, _) => ctx.thenPersist(CreatedAddress(cmd))(_ => ctx.reply(Done))
	}
	//处理事件
	.onEvent
	{
		//删除事件
		case (Deleted, state) => state.changeStatus(UserStatus.Deletion)
		//创建收货地址事件
		case (evt: CreatedAddress, state) =>
			//添加收货地址
		state.data.map(_.addAddress(evt))
		state
	}
}
