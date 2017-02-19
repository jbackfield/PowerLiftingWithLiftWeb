package bootstrap.liftweb

import java.util.{TimeZone}

import in.vendingmach.web.dao.MyDB
import in.vendingmach.web.model.Foo
import net.liftweb.common.Full
import net.liftweb.http.{LiftRules, Req, Html5Properties}
import net.liftweb.mapper.{By, DB}
import net.liftweb.sitemap._
import net.liftweb.util.DefaultConnectionIdentifier

object Pages {

  val index = Menu("index") / "index"

  val foo = Menu.param[Foo](
    "foo", // Unique Name
    "Foo", // Link title
    in => DB.use(DefaultConnectionIdentifier) { conn => {
      Foo.find(By(Foo.uuid, in))
    }},
    _.uuid.get) / "foo"

  val static = Menu("static") / "static" / **

}

class Boot {

  def setupDB : Boot = {
    DB.defineConnectionManager(DefaultConnectionIdentifier, MyDB)
    this
  }

  def setupSiteMap : Boot = {
    LiftRules.setSiteMapFunc(() => SiteMap(
      Pages.index,
      Pages.foo,
      Pages.static
    ))
    this
  }

  def setupMisc : Boot = {
    // where to search snippet
    LiftRules.addToPackages("in.vendingmach.web")

    /*
     * Show the spinny image when an Ajax call starts
     */
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    /*
     * Make the spinny image go away when it ends
     */
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

    LiftRules.htmlProperties.default.set((r : Req) => new Html5Properties(r.userAgent))

    this
  }

  def boot {
    setupDB.setupSiteMap.setupMisc
  }

}
