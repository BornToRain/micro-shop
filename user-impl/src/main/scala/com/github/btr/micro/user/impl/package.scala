package com.github.btr.micro.user

import org.joda.time.DateTime

package object impl
{
	/**
		* 领域模型与接口对象转换器
		*/
	implicit class DomainModelConverter[K, V](data: AnyRef)
	{
		//转接口对象
		def toApi[T]: T =
		{
			val obj = data match
			{
				case Some(d: impl.Name)   => Some(api.Name(d.firstName, d.lastName, d.enName))
				case d: Enumeration#Value => d match
				{
					case AddressType.Home    => api.AddressType.Home
					case AddressType.Company => api.AddressType.Company
					case AddressType.School  => api.AddressType.School
					case AddressStatus.Use   => api.AddressStatus.Use
					case AddressStatus.Stop  => api.AddressStatus.Stop
					case _                   => throw new IllegalAccessException("未知的枚举值")
				}
				case d: Address           => api.Address(d.province, d.city, d.district, d.zipCode, d.street, d.status.toApi, d.`type`.toApi)
				case m: Map[K, V]         => m.map
				{
					case (id: String, d: Address) => (id, d.toApi)
				}
				case d: User              => api.User(d.id, d.mobile, d.name.toApi, d.age, d.addresses.toApi, d.createTime)
			}
			obj.asInstanceOf[T]
		}

//		def toApiOption[T]: Option[T] = Option(toApi)

		//转领域对象
		def toDomain[T]: T =
		{
			val obj = data match
			{
				case Some(d: api.Name)    => Some(Name(d.firstName, d.lastName, d.enName))
				case d: Enumeration#Value => d match
				{
					case api.AddressType.Home    => AddressType.Home
					case api.AddressType.Company => AddressType.Company
					case api.AddressType.School  => AddressType.School
					case api.AddressStatus.Use   => AddressStatus.Use
					case api.AddressStatus.Stop  => AddressStatus.Stop
					case _                       => println("未知的枚举值")
				}
				case d: api.Address       => Address(d.province, d.city, d.district, d.zipCode, d.street, d.status.toDomain, d.`type`.toDomain)
			}
			obj.asInstanceOf[T]
		}

//		def toDomainOption[T]: Option[T] = Option(toDomain)
	}

	object Test extends App
	{
		val s = Some(Name("", "", None))
		println(s.toApi)

		val s1 = AddressStatus.Use
		println(s1.toApi)

		val s2 = AddressType.Home
		println(s2.toApi)

		val s3 = api.AddressStatus.Stop
		println(s3.toDomain)

		val s4 = api.AddressType.School
		println(s4.toDomain)

		val s5 = Address("", "", "", None, "", s1, s2)
		println(s5)
		println(s5.`type`)

		val s6 = s5.toApi[api.Address]
		println(s6)
		println(s6.`type`)

		val s7 = User("1", "", None, None, Map.empty, DateTime.now, DateTime.now)
		val s8 = User("2", "", None, None, Map.empty, DateTime.now, DateTime.now)

		val s9 = List(s7, s8).map(_.toApi[api.User])
		s9.foreach(println)
	}

}
