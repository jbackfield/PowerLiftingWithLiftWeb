package in.vendingmach.web.snippet

import in.vendingmach.web.comet.OperationManager
import in.vendingmach.web.model.{SpiteDrink, DietCrudDrink, CrudDrink, Drink}
import net.liftweb.http.{GUIDJsExp, SHtml}
import net.liftweb.http.js.{JE, JsCmds, JsCmd}
import net.liftweb.json.JsonAST.{JString, JArray, JInt}
import net.liftweb.json.JsonDSL._
import net.liftweb.json.{JObject, parseOpt}
import net.liftweb.util._
import Helpers._

import scala.xml.NodeSeq

object VendingMachine {

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
        case Left(drink) => {
          OperationManager ! JE.Call("setVended", "name" -> drink.name : JObject).cmd
          JsCmds.Noop
        }
        case Right(message) => {
          OperationManager ! JE.Call("setMessage", message).cmd
          JsCmds.Noop
        }
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
        OperationManager ! JE.Call(JE.JsVar(JE.Call("$", "#vended").toJsCmd, "addClass").toJsCmd, "hidden").cmd
        JsCmds.Noop
      }).getOrElse(JsCmds.Alert("Nothing to grab!"))
    }
    val GUIDJsExp(_, js) = SHtml.ajaxInvoke(_grabVended)
    JsCmds.Script(JsCmds.Function("grabVended", List(), js.cmd))
  }

  def openMachine() : NodeSeq = {
    def _openMachine() : JsCmd = {
      machineOpen = true
      OperationManager ! JE.Call("displayOpenMachine", items.map(d => "count" -> d.length).toList : JArray).cmd
    }
    val GUIDJsExp(_, js) = SHtml.ajaxInvoke(_openMachine)
    JsCmds.Script(JsCmds.Function("openMachine", List(), js.cmd))
  }

  def closeMachine() : NodeSeq = {
    def _closeMachine() : JsCmd = {
      machineOpen = false
      OperationManager ! JE.Call("displayClosedMachine").cmd
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
      case Some(JString(t)) => {
        Symbol(t) match {
          case 'CRUD => refill(0, 1, new CrudDrink())
          case 'DIETCRUD => refill(2, 3, new DietCrudDrink())
          case 'SPITE => refill(4, new SpiteDrink())
        }
        OperationManager ! JE.Call("displayOpenMachine", items.map(d => "count" -> d.length).toList : JArray).cmd
      }
      case _ => JsCmds.Alert("Invalid Parameters")
    }
    val GUIDJsExp(_, js) = SHtml.ajaxCall(JE.Stringify(JE.JsVar("type")), _refill)
    JsCmds.Script(JsCmds.Function("refill", "type" :: Nil, js.cmd))
  }

  def createDivs(cnt : Int) : NodeSeq = for(i <- 0 until cnt) yield <div class="can-top row"></div>

  def state() : CssSel = {
    if(machineOpen) {
      "div .closed [class+]" #> "hidden" &
        ".vending-machine .open .can-holder-spacer .holder-1 *" #> createDivs(items(0).length) &
        ".vending-machine .open .can-holder-spacer .holder-2 *" #> createDivs(items(1).length) &
        ".vending-machine .open .can-holder-spacer .holder-3 *" #> createDivs(items(2).length) &
        ".vending-machine .open .can-holder-spacer .holder-4 *" #> createDivs(items(3).length) &
        ".vending-machine .open .can-holder-spacer .holder-5 *" #> createDivs(items(4).length)
    } else {
      "div .open [class+]" #> "hidden" & (vendedItem match {
        case Some(vend : CrudDrink) => "#vended [class+]" #> "crud-colors" & "#vended *" #> "Crud"
        case Some(vend : DietCrudDrink) => "#vended [class+]" #> "diet-crud-colors" & "#vended *" #> "Diet Crud"
        case Some(vend : SpiteDrink) => "#vended [class+]" #> "spite-colors" & "#vended *" #> "Spite"
        case _ => "#vended [class+]" #> "hidden" & "#vended *" #> "Hidden Text"
      })
    }
  }

}
