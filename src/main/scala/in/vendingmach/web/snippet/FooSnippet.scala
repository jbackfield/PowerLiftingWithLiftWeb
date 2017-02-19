package in.vendingmach.web.snippet

import java.text.SimpleDateFormat

import bootstrap.liftweb.Pages
import in.vendingmach.web.model.Foo
import net.liftweb.http.{GUIDJsExp, SHtml}
import net.liftweb.http.js.JsCmds
import net.liftweb.util._, Helpers._

class FooSnippet(foo : Foo) {

  val formatter = new SimpleDateFormat()

  def body : CssSel = {
    val GUIDJsExp(_, js) = SHtml.ajaxInvoke(
      () => JsCmds.Alert(foo.uuid.toString)
    )
    ".page-link [href+]" #> Pages.foo.calcHref(foo) &
      "#timestamp [onclick+]" #> js &
      "#timestamp *" #> formatter.format(foo.created.getTime)
  }

}
