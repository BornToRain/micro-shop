package com.github.btr.micro.tool

import com.lightbend.lagom.scaladsl.api.transport.{ExceptionMessage, TransportErrorCode, UnsupportedMediaType}
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import org.slf4j.LoggerFactory
import play.api.http.HeaderNames

/**
	* 接口日志、权限等
	*/
trait Api
{
	private val log = LoggerFactory.getLogger(classOf[Api])

	//日志
	def logged[Request, Response](call: ServerServiceCall[Request, Response]) = ServerServiceCall.compose
	{
		request =>
		log.info(s"请求方式 =====> ${request.method }")
		log.info(s"请求URI =====> ${request.uri }")
		if (log.isDebugEnabled)
		{
			log.debug(s"请求内容协议 =====> ${request.protocol.contentType }")
			log.debug(s"请求内容编码 =====> ${request.protocol.charset }")
			request.headerMap.foreach
			{
				_._2.foreach
				{ case (h, v) => log.debug(s"$h =====> $v") }
			}
		}

		call
	}

	lazy val json   = "application/json"
	lazy val v1Json = "application/borntorain.v1+json"

	//v1版本头
	def v1[Request, Response](call: ServerServiceCall[Request, Response]) = logged(ServerServiceCall.compose
	{
		request =>
		request.getHeader(HeaderNames.ACCEPT) match
		{
			case Some(d) if d == json || d == v1Json => call
			case _                                   => throw new UnsupportedMediaType(TransportErrorCode.ProtocolError,
				new ExceptionMessage("ProtocolError", s"请指定Accept请求头为${json }或${v1Json }"))
		}
	})
}


