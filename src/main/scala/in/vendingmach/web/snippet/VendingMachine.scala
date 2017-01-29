package in.vendingmach.web.snippet

import net.liftweb.util._, Helpers._

object VendingMachine {

  println("Vending Machine Initialized")

  def helloWorld() : CssSel = {
    "h1 *" #> "<h1>Hello World</h1>" &
      "a *" #> "DevNexus" &
      "a [href+]" #> """"/><a href="http://badsite.com"></a>"""
  }

}
