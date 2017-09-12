package com.github.btr.micro.tool

import org.joda.time.DateTime
import play.api.data.validation.ValidationError
import play.api.libs.json._

import scala.util.Try

/**
	* JSON格式化
	*/
object JSONTool
{
	//枚举读
	def enumReads[E <: Enumeration](enum: E): Reads[E#Value] = Reads
	{
		case JsString(s) => Try(JsSuccess(enum.withName(s).asInstanceOf[E#Value])).get
		case _           => JsError("不在枚举值范围")
	}

	//枚举写
	def enumWrites[E <: Enumeration]: Writes[E#Value] = Writes(v => JsString(v.toString))

	//枚举读写
	def enumFormat[E <: Enumeration](enum: E): Format[E#Value] = Format(enumReads(enum), enumWrites)

	//单个值读
	def singletonReads[O](singleton: O): Reads[O] = (__ \ "value").read[String].collect
	{
		ValidationError(s"JSON对象里面没有${singleton.getClass.getSimpleName }字段")
	}
	{
		case s if s == singleton.getClass.getSimpleName => singleton
	}

	//单个值写
	def singletonWrites[O]: Writes[O] = Writes
	{
		singleton =>
		Json.obj("value" -> singleton.getClass.getSimpleName)
	}

	//单个值读写
	def singletonFormat[O](singleton: O): Format[O] = Format(singletonReads(singleton), singletonWrites)

	implicit object dataTimeFormat extends Format[DateTime]
	{
		val dateTime = "yyyy-MM-dd HH:mm:ss"

		override def reads(json: JsValue): JsResult[DateTime] = JsSuccess(json.as[DateTime])

		override def writes(o: DateTime): JsValue = JsString(o.toString(dateTime))
	}
}

