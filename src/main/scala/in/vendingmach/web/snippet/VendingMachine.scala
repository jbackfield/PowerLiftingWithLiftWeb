package in.vendingmach.web.snippet

import java.util.UUID

import net.liftweb.http.{GUIDJsExp, SHtml}
import net.liftweb.http.js.{JsCmds, JsCmd}
import net.liftweb.util._, Helpers._

class VendingMachine {

  println("Vending Machine Initialized")

  val uuid = UUID.randomUUID()

  def helloWorld() : CssSel = {
    val GUIDJsExp(_, js) = SHtml.ajaxInvoke(
      () => JsCmds.Alert(uuid.toString)
    )
    "a *" #> "DevNexus" &
      "a [href+]" #> "#" &
      "a [onclick+]" #> js
  }

}
