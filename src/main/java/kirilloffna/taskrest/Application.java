package kirilloffna.taskrest;

import kirilloffna.taskrest.dao.OrderDetailDAO;
import kirilloffna.taskrest.dao.ProductDAO;
import kirilloffna.taskrest.dao.impl.OrderDetailDAOImpl;
import kirilloffna.taskrest.dao.impl.ProductDAOImpl;
import kirilloffna.taskrest.service.OrderDetailService;
import kirilloffna.taskrest.service.ProductService;
import kirilloffna.taskrest.service.impl.OrderDetailServiceImpl;
import kirilloffna.taskrest.service.impl.ProductServiceImpl;
import kirilloffna.taskrest.servlet.OrderDetailServlet;
import kirilloffna.taskrest.servlet.ProductServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

/**
 * Основной класс приложения, который инициализирует и запускает встроенный сервер Tomcat.
 * Сервер Tomcat запускается на порту 9090.
 */
public class Application {
  public static void main(String[] args) throws LifecycleException {
    Tomcat tomcat = new Tomcat();
    ProductDAO productDAO = new ProductDAOImpl();
    OrderDetailDAO orderDetailDAO = new OrderDetailDAOImpl();
    ProductService productService = new ProductServiceImpl(productDAO);
    OrderDetailService orderDetailService = new OrderDetailServiceImpl(orderDetailDAO);

    tomcat.setPort(9090);
    tomcat.getConnector();

    var context = tomcat.addContext("", null);

    Tomcat.addServlet(context, "productServlet", new ProductServlet(productService));
    context.addServletMappingDecoded("/products", "productServlet");
    Tomcat.addServlet(context, "orderDetailServlet", new OrderDetailServlet(orderDetailService));
    context.addServletMappingDecoded("/order-details", "orderDetailServlet");

    tomcat.start();
    tomcat.getServer().await();
  }
}
