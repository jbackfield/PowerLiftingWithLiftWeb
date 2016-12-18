package bootstrap.liftweb

import java.util.{Date, TimeZone}

import in.vendingmach.web.dao.MyDB
import in.vendingmach.web.snippet.CurrentSession
import net.liftweb.common.Full
import net.liftweb.db.DB
import net.liftweb.http.{LiftRules, Req, Html5Properties}
import net.liftweb.sitemap._
import net.liftweb.util.DefaultConnectionIdentifier

object Pages {

  val index = Menu("index") / "index"

  val static = Menu("static") / "static" / **

}

class Boot {

  def setupDB : Boot = {
    DB.defineConnectionManager(DefaultConnectionIdentifier, MyDB)
    this
  }

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

    LiftRules.earlyInStateful.append {
      case Full(r) => {
        CurrentSession.is.lastUpdated(new Date()).save
      }
      case _ =>
    }

    this
  }

  def boot {
    //setupDatabase.setupSiteMap.setupServices.setupMisc
    setupDB.setupSiteMap.setupMisc
  }

}
