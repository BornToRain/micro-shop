package com.github.btr.micro.user.impl

/**
	* 用户收货地址值对象
	*/
case class Address
(
	//省
	province: String,
	//市
	city: String,
	//区
	district: String,
	//邮政编码
	zipCode: Option[String],
	//街道
	street: String,
	status: AddressStatus.Status
)
