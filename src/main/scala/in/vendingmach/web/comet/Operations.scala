package in.vendingmach.web.comet

import net.liftweb.common.SimpleActor
import net.liftweb.http.js.{JsCmd, JsCmds}
import net.liftweb.http.{CometListener, RenderOut, CometActor}

class Operations extends CometActor with CometListener {

  var message : Option[JsCmd] = None

  override def lowPriority = {
    case Some(msg : JsCmd) => message = Some(msg); reRender()
  }

  override def render: RenderOut = message match {
    case Some(cmd) => {
      message = None
      cmd
    }
    case msg => {
      println(s"Got message $msg")
      JsCmds.Noop
    }
  }

  override protected def registerWith: SimpleActor[Any] = OperationManager
}
