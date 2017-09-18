name := "micro-shop"
version in ThisBuild := "1.0"
scalaVersion in ThisBuild := "2.11.11"
organization in ThisBuild := "com.github.btr"

//简化代码
val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"

//模块基础配置
def project(id: String) = Project(id, base = file(id))
.settings(javacOptions in compile ++= Seq("-encoding", "UTF-8", "-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation"))

//商城微服务ByLagom
//lazy val `micro-shop` = project(".")

//微服务工具包、配置模块
lazy val `micro-tool` = project("micro-tool")
.settings(
	name := "micro-tool",
	libraryDependencies ++= Seq(
		lagomScaladslApi,
		lagomScaladslServer % Optional
	)
)

//用户模块接口
lazy val `user-api` = project("user-api")
.settings(
	name := "user-api",
	libraryDependencies ++= Seq(
		lagomScaladslApi
	)
)
.dependsOn(`micro-tool`)
//用户模块实现
lazy val `user-impl` = project("user-impl")
.enablePlugins(LagomScala)
.settings(
	name := "user-impl",
	libraryDependencies ++= Seq(
		macwire,
		lagomScaladslServer,
		lagomScaladslPersistenceCassandra
	)
)
.dependsOn(`micro-tool`, `user-api`)

//商品模块接口
lazy val `product-api` = project("product-api")
.settings(
	name := "product-api",
	libraryDependencies ++= Seq(
		lagomScaladslApi
	)
)
//商品模块实现
lazy val `product-impl` = project("product-impl")
.enablePlugins(LagomScala)
.settings(
	name := "product-impl",
	libraryDependencies ++= Seq(
		macwire,
		lagomScaladslServer,
		lagomScaladslPersistenceCassandra
	)
)
.dependsOn(`micro-tool`, `product-api`)

//库存模块接口
lazy val `inventory-api` = project("inventory-api")
.settings(
	name := "inventory-api",
	libraryDependencies ++= Seq(
		lagomScaladslApi
	)
)
//库存模块实现
lazy val `inventory-impl` = project("inventory-impl")
.enablePlugins(LagomScala)
.settings(
	name := "inventory-impl",
	libraryDependencies ++= Seq(
		macwire,
		lagomScaladslServer,
		lagomScaladslPersistenceCassandra
	)
)
.dependsOn(`micro-tool`, `inventory-api`)

//订单模块接口
lazy val `order-api` = project("order-api")
.settings(
	name := "order-api",
	libraryDependencies ++= Seq(
		lagomScaladslApi
	)
)
//订单模块实现
lazy val `order-impl` = project("order-impl")
.enablePlugins(LagomScala)
.settings(
	name := "order-impl",
	libraryDependencies ++= Seq(
		macwire,
		lagomScaladslServer,
		lagomScaladslPersistenceCassandra
	)
)
.dependsOn(`micro-tool`, `order-api`)

//购物车模块接口
lazy val `cart-api` = project("cart-api")
.settings(
	name := "cart-api",
	libraryDependencies ++= Seq(
		lagomScaladslApi
	)
)
//购物车模块实现
lazy val `cart-impl` = project("cart-impl")
.enablePlugins(LagomScala)
.settings(
	name := "cart-impl",
	libraryDependencies ++= Seq(
		macwire,
		lagomScaladslServer,
		lagomScaladslPersistenceCassandra
	)
)
.dependsOn(`micro-tool`, `cart-api`)

//搜索模块接口
lazy val `search-api` = project("search-api")
.settings(
	name := "search-api",
	libraryDependencies ++= Seq(
		lagomScaladslApi
	)
)
//搜索模块实现
lazy val `search-impl` = project("search-impl")
.settings(
	name := "search-impl",
	libraryDependencies ++= Seq(
		macwire,
		lagomScaladslServer
	)
)
.dependsOn(`micro-tool`,`search-api`)

lagomCassandraCleanOnStart in ThisBuild := true