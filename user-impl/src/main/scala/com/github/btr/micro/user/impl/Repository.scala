package com.github.btr.micro.user.impl

import com.google.common.reflect.TypeToken
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext

/**
	* 用户读边
	*/
class UserRepository(session: CassandraSession)(implicit ex: ExecutionContext)
{
	def gets = session.selectAll("SELECT * FROM user").map(_.map(d =>
	{
		val name = d.getUDTValue("name")
		val fullName = Name(name.getString("first_name"), name.getString("last_name"), Option(name.getString("en_name")))
		val dd = d.getMap[String, Address]("addresses",TypeToken.of(classOf[String]),TypeToken.of(classOf[Address]))
		println(dd)
//		val addresses = d.getUDTValue("addresses")

//		val addresses = d.getMap("addresses", TypeToken.of(classOf[String]), TypeToken.of(classOf[Address]))
		User(d.getString("id"), d.getString("mobile"), Some(fullName), Some(d.getInt("age")), Map.empty, new DateTime(d.getTimestamp("create_time")),
			new DateTime(d.getTimestamp("update_time")))
	}))
}
