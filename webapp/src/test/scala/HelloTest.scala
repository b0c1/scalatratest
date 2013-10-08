import org.scalatest.FunSpec
import org.scalatra.test.scalatest.ScalatraSuite

class HelloTest extends FunSpec with ScalatraSuite {
  addServlet(new HelloController, "/*")

  describe("Hello Servlet") {
    it("should compile view") {
      get("/") {
        status should equal(200)
        body should include("Hi there")
      }
    }
  }
}
