package com.github.btr.micro.product.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

/**
	* 商品持久化
	*/
class ProductEntity extends PersistentEntity
{
	override type Command = ProductCmd[_]
	override type Event = ProductEvt
	override type State = ProductState

	//初始状态
	override def initialState = ProductState.nonexistence

	override def behavior =
	{
		//不存在状态
		case ProductState.nonexistence => get orElse nonexistence
		//下架状态
		case ProductState(_, ProductStatus.Hide) => get orElse existence orElse hide
		//上架状态
		case ProductState(_, ProductStatus.Show) => get orElse existence orElse show
	}

	//商品信息
	def get = Actions().onReadOnlyCommand[Get.type, Option[Product]]
	{
		case (Get, ctx, state) => ctx.reply(state.data)
	}

	//已存在状态下无效的命令
	def existence = Actions()
	//创建命令
	.onReadOnlyCommand[Create, String]
	{
		case (_: Created, ctx, _) => ctx.invalidCommand("商品已存在!")
	}

	//不存在状态下的操作
	def nonexistence = Actions()
	//处理创建命令
	.onCommand[Create, String]
	{
		//持久化创建事件回复Id
		case (cmd: Create, ctx, _) => ctx.thenPersist(Created(cmd))(e => ctx.reply(e.cmd.id))
	}
	//处理事件
	.onEvent
	{
		//创建聚合根
		case (Created(cmd), _) => ProductState.create(Product(cmd.id, cmd.sellId, cmd.name, cmd.price, cmd.illustrations, cmd.`type`))
	}

	//下架状态下的操作
	def hide = Actions()
	//处理上架命令
	.onCommand[Show.type, Done]
	{
		//持久化上架事件回复完成
		case (_, ctx, _) => ctx.thenPersist(Showed)(_ => ctx.reply(Done))
	}
	//处理事件
	.onEvent
	{
		case (_, state) =>
			//上架
		state.changeStatus(ProductStatus.Show)
		state
	}

	//上架状态下的操作
	def show = Actions()
	//处理下架命令
	.onCommand[Hide.type, Done]
	{
		//持久化下架事件回复完成
		case (_, ctx, _) => ctx.thenPersist(Hid)(_ => ctx.reply(Done))
	}
	//处理事件
	.onEvent
	{
		case (_, state) =>
			//下架
		state.changeStatus(ProductStatus.Hide)
		state
	}
}
