package in.vendingmach.web.snippet

import net.liftweb.util._, Helpers._

object VendingMachine {

  println("Vending Machine Initialized")

  def helloWorld() : CssSel = {
    "h1 *" #> "Hello World" &
      "a *" #> "DevNexus" &
      "a [href+]" #> "http://devnexus.com"
  }

}
