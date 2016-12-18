package in.vendingmach.web.api

import in.vendingmach.web.dao.UserSession
import net.liftweb.common.Full
import net.liftweb.db.DB
import net.liftweb.http.{NotFoundResponse, LiftResponse}
import net.liftweb.http.rest.RestHelper
import net.liftweb.json.JArray
import net.liftweb.util.DefaultConnectionIdentifier

import net.liftweb.json.JsonDSL._

object SessionInfoAPI extends RestHelper {

  def getSessions : LiftResponse = {
    DB.use(DefaultConnectionIdentifier) { connection => {
      UserSession.findAll().map(us => {
        ("id" -> us.id.get) ~
          ("uuid" -> us.uuid.get) ~
          ("created" -> us.created.get.getTime) ~
          ("lastUpdated" -> us.lastUpdated.get.getTime)
      }) : JArray
    }}
  }

  def getSession(id : Long) : LiftResponse = {
    DB.use(DefaultConnectionIdentifier) { connection => {
      UserSession.findByKey(id).map(us => {
        ("id" -> us.id.get) ~
          ("uuid" -> us.uuid.get) ~
          ("created" -> us.created.get.getTime) ~
          ("lastUpdated" -> us.lastUpdated.get.getTime) ~
          ("operations" -> us.operations.map(op => {
            ("id" -> op.id.get) ~
              ("created" -> op.created.get.getTime) ~
              ("action" -> op.action.get)
          })) : LiftResponse
      }).getOrElse(NotFoundResponse())
    }}
  }

  serve("api" / "sessions" prefix {
    case Nil Get _ => getSessions
    case id :: Nil Get _ => getSession(id.toLong)
  })

}
