package in.vendingmach.web.snippet

import in.vendingmach.web.model.{SpiteDrink, DietCrudDrink, CrudDrink, Drink}
import net.liftweb.http.{GUIDJsExp, SHtml}
import net.liftweb.http.js.{JE, JsCmds, JsCmd}
import net.liftweb.json.JsonAST.JInt
import net.liftweb.json.JsonDSL._
import net.liftweb.json.{JObject, parseOpt}

import scala.xml.NodeSeq

class VendingMachine {

  println("Vending Machine Initialized")

  val items : Array[List[Drink]] = Array(
    List(CrudDrink(), CrudDrink(), CrudDrink(), CrudDrink(), CrudDrink()),
    List(CrudDrink(), CrudDrink(), CrudDrink(), CrudDrink(), CrudDrink()),
    List(DietCrudDrink(), DietCrudDrink(), DietCrudDrink(), DietCrudDrink(), DietCrudDrink()),
    List(DietCrudDrink(), DietCrudDrink(), DietCrudDrink(), DietCrudDrink(), DietCrudDrink()),
    List(SpiteDrink(), SpiteDrink(), SpiteDrink(), SpiteDrink(), SpiteDrink())
  )

  var vendedItem : Option[Drink] = None

  def purchaseDrink(index : Int) : Either[Drink, String] = {
    if(index >= items.length || index < 0) {
      Right("Invalid Option")
    } else {
      vendedItem.map(_ => Right("Blocked")).getOrElse {
        items(index) match {
          case List() => Right("Sold Out")
          case head :: tail => {
            items(index) = tail
            vendedItem = Some(head)
            Left(head)
          }
        }
      }
    }
  }

  def purchase() : NodeSeq = {
    def _purchase(in : String) : JsCmd = parseOpt(in) match {
      case Some(JInt(index)) => purchaseDrink(index toInt) match {
        case Left(drink) => JE.Call("setVended", "name" -> drink.name : JObject).cmd
        case Right(message) => JE.Call("setMessage", message).cmd
      }
      case _ => JsCmds.Alert("Invalid Parameters")
    }
    val GUIDJsExp(_, js) = SHtml.ajaxCall(JE.Stringify(JE.JsVar("index")), _purchase)
    JsCmds.Script(JsCmds.Function("purchase", "index" :: Nil, js.cmd))
  }

  def grabVended() : NodeSeq = {
    def _grabVended() : JsCmd = {
      vendedItem.map(d => {
        vendedItem = None
        JE.Call(JE.JsVar(JE.Call("$", "#vended").toJsCmd, "addClass").toJsCmd, "hidden").cmd
      }).getOrElse(JsCmds.Alert("Nothing to grab!"))
    }
    val GUIDJsExp(_, js) = SHtml.ajaxInvoke(_grabVended)
    JsCmds.Script(JsCmds.Function("grabVended", List(), js.cmd))
  }

}
