import javax.servlet.ServletContext
import org.scalatra.LifeCycle

class Boot extends LifeCycle {
  override def init(context: ServletContext) {
    context mount(new HelloController, "/*")
  }
}
