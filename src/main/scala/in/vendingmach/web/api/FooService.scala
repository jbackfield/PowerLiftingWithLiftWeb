package in.vendingmach.web.api

import in.vendingmach.web.model.Foo
import net.liftweb.http.{JsonResponse, NotFoundResponse, LiftResponse}
import net.liftweb.http.rest.RestHelper
import net.liftweb.json.JsonAST.JArray
import net.liftweb.json.JsonDSL._
import net.liftweb.mapper.By

object FooService extends RestHelper {

  def getAll : LiftResponse = {
    Foo.findAll.map(f =>
      ("uuid" -> f.uuid.get) ~ ("creationDate" -> f.creationDate.get.getTime)
    ) : JArray
  }

  def getFoo(uuid : String) : LiftResponse = {
    Foo.find(By(Foo.uuid, uuid)).map(f =>
      JsonResponse(("uuid" -> f.uuid.get) ~
        ("creationDate" -> f.creationDate.get.getTime))
    ).getOrElse(NotFoundResponse())
  }

  serve("api" / "foo" prefix {
    case Nil JsonGet _ => getAll
    case uuid :: Nil JsonGet _ => getFoo(uuid)
  })

}
