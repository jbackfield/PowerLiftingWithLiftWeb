package in.vendingmach.web.snippet

import java.text.SimpleDateFormat
import java.util.Calendar

import bootstrap.liftweb.Pages
import in.vendingmach.web.model.Foo
import net.liftweb.http.{GUIDJsExp, SHtml}
import net.liftweb.http.js.{JE, JsCmds}
import net.liftweb.util._, Helpers._

import scala.xml.Text

class FooSnippet(foo : Foo) {

  val formatter = new SimpleDateFormat()

  def body : CssSel = {
    val GUIDJsExp(_, js) = SHtml.ajaxInvoke(
      () => {
        foo.creationDate.set(Calendar.getInstance.getTime)
        foo.save
        JsCmds.SetHtml("timestamp", Text(formatter.format(foo.creationDate.get))) &
          JsCmds.Alert(foo.uuid.toString)
      }
    )
    ".page-link [href+]" #> Pages.foo.calcHref(foo) &
      "#timestamp [onclick+]" #> js &
      "#timestamp *" #> formatter.format(foo.creationDate.get)
  }

}
