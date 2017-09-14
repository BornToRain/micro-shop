package com.github.btr.micro.user.impl

/**
	* 用户姓名值对象
	*/
case class Name
(
	//姓
	firstName: String,
	//名
	lastName: String,
	//英文名
	enName: Option[String]
)
