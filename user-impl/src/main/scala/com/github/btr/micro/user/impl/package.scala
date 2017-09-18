package com.github.btr.micro.user

package object impl
{
	/** ******************领域内值对象转换 ********************/
	implicit def toApiName(data: Option[impl.Name]) = data.map(d => api.Name(d.firstName, d.lastName, d.enName))

	implicit def toImplName(data: Option[api.Name]) = data.map(d => impl.Name(d.firstName, d.lastName, d.enName))

	implicit def toApiAddressStatus(data: impl.AddressStatus.Status) = api.AddressStatus(data.id)

	implicit def toImplAddressStatus(data: api.AddressStatus.Status) = impl.AddressStatus(data.id)

	implicit def toApiAddressType(data: impl.AddressType.Type) = api.AddressType(data.id)

	implicit def toImplAddressType(data: api.AddressType.Type) = impl.AddressType(data.id)
	/** ******************领域内值对象转换 ********************/

}
