package in.vendingmach.web.snippet

import java.util.{Date, UUID}

import in.vendingmach.web.dao.UserSession
import net.liftweb.http.SessionVar

object CurrentSession extends SessionVar[UserSession]({
  val sess = UserSession.create
  sess.created(new Date())
  sess.lastUpdated(new Date())
  sess.uuid(UUID.randomUUID().toString)
  sess.save
  sess
})