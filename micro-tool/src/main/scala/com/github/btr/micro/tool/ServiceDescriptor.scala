package com.github.btr.micro.tool

/**
	* 接口定义描述符
	*/
case class ServiceDescriptor(name: String, version: String)
{
	/**
		* 版本URI地址
		*
		* @param path
		* @return
		*/
	def versionURI(path: String = "") = s"/api/$name/$version$path"
}
