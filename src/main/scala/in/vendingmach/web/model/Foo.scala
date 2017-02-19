package in.vendingmach.web.model

import net.liftweb.mapper._

class Foo extends KeyedMapper[String, Foo] {
  def getSingleton = Foo

  override def primaryKeyField = id
  object id extends MappedStringIndex[Foo](this, 36) {
    override def dbColumnName = "uuid"
  }
  object creationDate extends MappedDateTime[Foo](this) {
    override def dbColumnName = "creation_date"
  }
}

object Foo extends Foo with KeyedMetaMapper[String, Foo]