package com.github.btr.micro.user

import com.github.btr.micro.user.api.AddressInfo

package object impl
{
	/** ******************领域内对象转换 ********************/
	//接口层用户信息
	implicit def toApiUser(data: User) = api.Info(data.id, data.mobile, toApiName(data.name), data.age,toApiAddresses(data.addresses),data.createTime)

	//接口层用户信息列表
	implicit def toApiUsers(data: Seq[User]) = data.map(toApiUser)

	//接口层姓名VO
	implicit def toApiName(data: Option[impl.Name]) = data.map(d => api.Name(d.firstName, d.lastName, d.enName))

	//领域层姓名VO
	implicit def toImplName(data: Option[api.Name]) = data.map(d => impl.Name(d.firstName, d.lastName, d.enName))

	//接口层用户收货地址状态
	implicit def toApiAddressStatus(data: impl.AddressStatus.Status) = api.AddressStatus(data.id)

	//领域层用户收货地址状态
	implicit def toImplAddressStatus(data: api.AddressStatus.Status) = impl.AddressStatus(data.id)

	//接口层用户收货地址类型
	implicit def toApiAddressType(data: impl.AddressType.Type) = api.AddressType(data.id)

	//领域层用户收货地址类型
	implicit def toImplAddressType(data: api.AddressType.Type) = impl.AddressType(data.id)

	//接口层用户收货地址
	implicit def toApiAddress(data: impl.Address) = api
	.AddressInfo(data.province, data.city, data.district, data.zipCode, data.street, data.status, data.`type`)

	//接口层用户收货地址列表
	implicit def toApiAddresses(datas: Map[String, impl.Address]):Map[String,AddressInfo] = datas.map
	{
		case (id, data) => (id, toApiAddress(data))
	}

	/** ******************领域内值对象转换 ********************/
	val fullDateTime = "yyyy-MM-dd HH:mm:ss"
//
//	implicit val yourJodaDateReads:Reads[DateTime] = Reads.jodaDateReads(fullDateTime)
//	implicit val yourJodaDateWrites:Writes[DateTime] = Writes.jodaDateWrites(fullDateTime)

//	val fullDateTime = "yyyy-MM-dd HH:mm:ss"
//
//	implicit val dateFormat:Format[DateTime] = Format[DateTime](Reads.jodaDateReads(fullDateTime), Writes.jodaDateWrites(fullDateTime))
//
//	Reads.DefaultJodaDateReads

//	implicit val jodaDateReads:Reads[DateTime] = Reads[DateTime](js =>
//		js.validate[String].map[DateTime](dtString =>
//			DateTime.parse(dtString, DateTimeFormat.forPattern(fullDateTime))
//		)
//	)
//	implicit val formats = Formats.jodaDateTimeFormat(fullDateTime)



//	implicit val durationReads: Reads[Duration] = implicitly[Reads[String]]
//	.collect(ValidationError("Invalid duration"))(Function.unlift { str =>
//	Try(Duration.parse(str)).toOption
//	})
//	implicit val durationWrites: Writes[Duration] = Writes { duration =>
//	JsString(duration.toString)
//	}

//	implicit val jodaDateWrites: Writes[DateTime] = new Writes[DateTime] {
//		def writes(d: DateTime): JsValue = JsString(d.toString())
//	}

//	implicit def dateTimeWrites = Writes.jodaDateWrites(fullDateTime)
//
//	implicit def dateTimeReads = Reads.jodaDateReads(fullDateTime)

}
