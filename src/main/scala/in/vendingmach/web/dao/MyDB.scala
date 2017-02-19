package in.vendingmach.web.dao

import java.sql.Connection
import com.mchange.v2.c3p0.ComboPooledDataSource
import net.liftweb.common.{Full, Box}
import net.liftweb.db.ConnectionManager
import net.liftweb.util.{Props, ConnectionIdentifier}

object MyDB extends ConnectionManager {

  val pool = {
    val pool = new ComboPooledDataSource()
    pool.setJdbcUrl(Props.get("jdbc.url").openOrThrowException("JDBC Required"))
    pool.setUser(Props.get("jdbc.user").openOrThrowException("Username required"))
    pool.setPassword(Props.get("jdbc.password").openOrThrowException("Password required"))
    pool.setMinPoolSize(5)
    pool.setMaxPoolSize(20)
    pool.setDriverClass("com.mysql.jdbc.Driver")
    pool
  }

  override def newConnection(name: ConnectionIdentifier): Box[Connection] = {
    val conn = pool.getConnection
    conn.setAutoCommit(false)
    Full(conn)
  }

  override def releaseConnection(conn: Connection): Unit = {
    conn.close()
  }

}