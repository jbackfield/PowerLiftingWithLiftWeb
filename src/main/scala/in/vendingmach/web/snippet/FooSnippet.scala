package in.vendingmach.web.snippet

import java.text.SimpleDateFormat

import in.vendingmach.web.model.Foo
import net.liftweb.util._, Helpers._

class FooSnippet(foo : Foo) {

  val formatter = new SimpleDateFormat()

  def body : CssSel = {
    ".page-link [href+]" #> s"/foo/${foo.uuid.toString}" &
      "#timestamp *" #> formatter.format(foo.created.getTime)
  }

}
