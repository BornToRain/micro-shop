package com.github.btr.micro.tool

import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import play.api.libs.json.{Format, Reads, Writes}

/**
	* 接口日志、权限等
	*/
trait Api
{
	private val log = LoggerFactory.getLogger(classOf[Api])

	//日志
	def logged[Request, Response](call: => ServerServiceCall[Request, Response]) = ServerServiceCall.compose
	{
		request =>
		log.info(s"请求方式 =====> ${request.method }")
		log.info(s"请求URI =====> ${request.uri }")
		log.debug(s"请求内容协议 =====> ${request.protocol.contentType }")
		log.debug(s"请求内容编码 =====> ${request.protocol.charset }")
		request.headerMap.foreach
		{
			_._2.foreach
			{ case (h, v) => log.debug(s"$h =====> $v") }
		}
		call
	}
	val fullDateTime = "yyyy-MM-dd HH:mm:ss"

	implicit val dateFormat:Format[DateTime] = Format[DateTime](Reads.jodaDateReads(fullDateTime), Writes.jodaDateWrites(fullDateTime))

}


