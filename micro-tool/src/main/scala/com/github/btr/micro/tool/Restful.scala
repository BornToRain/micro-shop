package com.github.btr.micro.tool

import com.lightbend.lagom.scaladsl.api.transport.{MessageProtocol, RequestHeader, ResponseHeader}
import play.api.libs.json.{Format, Json}

/**
	* Restful风格返回工具
	*/
object Restful
{
	/**
		* Http资源创建成功 => 状态码201
		*/
	def created(request: RequestHeader)(implicit id: String) = ResponseHeader(201, MessageProtocol(Some("application/json"), Some("UTF-8")),
		Vector.empty)
	.withHeader("Location", request.uri + "/" + id)

	/**
		* Http资源删除成功 => 状态码204
		*/
	def noContent = ResponseHeader(204, MessageProtocol.empty, Vector.empty)
}


/**
	* Http参数请求错误 => 状态码400
	*/
case class ErrorData(code: Int, msg: String)
{
	val toJSONString = Json.toJson(this).toString
}

object ErrorData
{
	implicit val format: Format[ErrorData] = Json.format
}
