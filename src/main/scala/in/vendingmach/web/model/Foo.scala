package in.vendingmach.web.model

import net.liftweb.mapper._

class Foo extends LongKeyedMapper[Foo] {
  def getSingleton = Foo

  override def primaryKeyField = id

  object id extends MappedLongIndex[Foo](this)
  object uuid extends MappedString[Foo](this, 36)
  object creationDate extends MappedDateTime[Foo](this) {
    override def dbColumnName = "creation_date"
  }
}

object Foo extends Foo with LongKeyedMetaMapper[Foo]