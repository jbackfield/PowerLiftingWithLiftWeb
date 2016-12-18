package in.vendingmach.web.dao

import net.liftweb.mapper._

class SessionOperation extends LongKeyedMapper[SessionOperation] {
  def getSingleton = SessionOperation

  def primaryKeyField = id
  object id extends MappedLongIndex(this)
  object action extends MappedString(this, 32)
  object userSession extends MappedLongForeignKey(this, UserSession) {
    override def dbColumnName = "user_session_id"
  }
  object created extends MappedDateTime(this)

}

object SessionOperation extends SessionOperation with LongKeyedMetaMapper[SessionOperation] {}