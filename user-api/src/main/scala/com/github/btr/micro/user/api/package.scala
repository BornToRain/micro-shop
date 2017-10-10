package com.github.btr.micro.user

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.data.format.Formats
import play.api.libs.json.{Format, Reads, Writes}

package object api
{

	val fullDateTime = "yyyy-MM-dd HH:mm:ss"

	implicit val dateFormat: Format[DateTime] = Format[DateTime](Reads.jodaDateReads(fullDateTime), Writes.jodaDateWrites(fullDateTime))

	implicit val jodaDateReads: Reads[DateTime] = Reads[DateTime](js =>
		js.validate[String].map[DateTime](dtString =>
			DateTime.parse(dtString, DateTimeFormat.forPattern(fullDateTime))
		)
	)
	implicit val formats                        = Formats.jodaDateTimeFormat
}
