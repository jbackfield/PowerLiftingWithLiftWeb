package in.vendingmach.web.comet

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import net.liftweb.http.js.JsCmd

object OperationManager extends LiftActor with ListenerManager {

  var message : Option[JsCmd] = None

  override protected def createUpdate: Any = message

  override def lowPriority = {
    case s : JsCmd => {
      message = Some(s)
      updateListeners()
    }
    case msg => println(msg)
  }

}
