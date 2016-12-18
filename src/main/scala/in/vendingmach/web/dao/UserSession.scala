package in.vendingmach.web.dao

import net.liftweb.mapper._

class UserSession extends LongKeyedMapper[UserSession] with OneToMany[Long, UserSession] {
  def getSingleton = UserSession

  def primaryKeyField = id
  object id extends MappedLongIndex(this)
  object uuid extends MappedString(this, 64)
  object created extends MappedDateTime(this)
  object lastUpdated extends MappedDateTime(this) {
    override def dbColumnName = "last_updated"
  }
  object operations extends MappedOneToMany(SessionOperation, SessionOperation.userSession, OrderBy(SessionOperation.created, Ascending))
}

object UserSession extends UserSession with LongKeyedMetaMapper[UserSession] {

}
