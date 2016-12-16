package in.vendingmach.web.snippet

import in.vendingmach.web.model.{SpiteDrink, DietCrudDrink, CrudDrink, Drink}
import net.liftweb.http.{GUIDJsExp, SHtml}
import net.liftweb.http.js.{JE, JsCmds, JsCmd}
import net.liftweb.json.JsonAST.{JString, JArray, JInt}
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

  var machineOpen : Boolean = false

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

  def openMachine() : NodeSeq = {
    def _openMachine() : JsCmd = {
      machineOpen = true
      JE.Call("displayOpenMachine", items.map(d => "count" -> d.length).toList : JArray).cmd
    }
    val GUIDJsExp(_, js) = SHtml.ajaxInvoke(_openMachine)
    JsCmds.Script(JsCmds.Function("openMachine", List(), js.cmd))
  }

  def closeMachine() : NodeSeq = {
    def _closeMachine() : JsCmd = {
      machineOpen = false
      JE.Call("displayClosedMachine").cmd
    }
    val GUIDJsExp(_, js) = SHtml.ajaxInvoke(_closeMachine)
    JsCmds.Script(JsCmds.Function("closeMachine", List(), js.cmd))
  }

  def refill(l : Int, d : => Drink) : Unit = if(items(l).length < 5) {
    items(l) = items(l) ::: List(d)
  }

  def refill(l1 : Int, l2 : Int, d : => Drink) : Unit = {
    refill(if(items(l1).length < items(l2).length) {
      l1
    } else {
      l2
    }, d)
  }

  def refill() : NodeSeq = {
    def _refill(in : String) : JsCmd = parseOpt(in) match {
      case Some(JString(t)) => Symbol(t) match {
        case 'CRUD => refill(0, 1, new CrudDrink())
        case 'DIETCRUD => refill(2, 3, new DietCrudDrink())
        case 'SPITE => refill(4, new SpiteDrink())
      }
      JE.Call("displayOpenMachine", items.map(d => "count" -> d.length).toList : JArray).cmd
      case _ => JsCmds.Alert("Invalid Parameters")
    }
    val GUIDJsExp(_, js) = SHtml.ajaxCall(JE.Stringify(JE.JsVar("type")), _refill)
    JsCmds.Script(JsCmds.Function("refill", "type" :: Nil, js.cmd))
  }

}
