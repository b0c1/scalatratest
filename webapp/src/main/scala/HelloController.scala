import org.scalatra.scalate.ScalateSupport
import org.scalatra.ScalatraServlet

class HelloController extends ScalatraServlet with ScalateSupport {
  get("/") {
    try {
      ssp("index")
    } catch {
      case e: Exception =>
        e.printStackTrace()
        throw e
    }
  }
}
