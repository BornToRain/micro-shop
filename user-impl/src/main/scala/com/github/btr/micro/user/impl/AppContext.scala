package com.github.btr.micro.user.impl

import com.github.btr.micro.user.api.UserService
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.typesafe.conductr.bundlelib.lagom.scaladsl.ConductRApplicationComponents
import play.api.libs.ws.ahc.AhcWSComponents

/**
	* 用户模块启动
	*/
class UserAppLoader extends LagomApplicationLoader
{
	//正式环境启动
	override def load(context: LagomApplicationContext) = new UserApp(context) with ConductRApplicationComponents
	//测试环境启动
	override def loadDevMode(context: LagomApplicationContext) = new UserApp(context) with LagomDevModeComponents
	//服务描述
	override def describeService = Some(readDescriptor[UserService])
}

abstract class UserApp(context: LagomApplicationContext) extends LagomApplication(context)
//Cassandra插件
with CassandraPersistenceComponents
//WS-Http插件
with AhcWSComponents
{

	import com.softwaremill.macwire._

	//绑定服务
	lazy val lagomServer           : LagomServer            = serverFor[UserService](wire[UserServiceImpl])
	//注册序列化
	lazy val jsonSerializerRegistry: JsonSerializerRegistry = SerializerRegistry
	//注册仓库
	lazy val userRepository        : UserRepository         = wire[UserRepository]
	//注册持久化
	persistentEntityRegistry.register(wire[UserEntity])
	//注册事件处理
	readSide.register(wire[UserEventProcessor])
}
