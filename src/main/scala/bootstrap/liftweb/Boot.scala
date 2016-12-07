package bootstrap.liftweb

import java.util.TimeZone

import net.liftweb.common.Full
import net.liftweb.http.{LiftRules, Req, Html5Properties}
import net.liftweb.sitemap._

object Pages {

  val index = Menu("index") / "index"

  val static = Menu("static") / "static" / **

}

class Boot {

  def setupSiteMap : Boot = {
    LiftRules.setSiteMapFunc(() => SiteMap(
      Pages.index
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
    //setupDatabase.setupSiteMap.setupServices.setupMisc
    setupSiteMap.setupMisc
  }

}
