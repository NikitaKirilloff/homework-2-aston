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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тестовый класс для проверки работы сервлетов в приложении с использованием встроенного сервера Tomcat.
 */
@TestInstance(Lifecycle.PER_CLASS)
class ApplicationTest {

  private Tomcat tomcat;

  /**
   * Настраивает и запускает сервер Tomcat перед выполнением всех тестов.
   *
   * @throws LifecycleException если происходит ошибка при запуске Tomcat.
   */
  @BeforeAll
  void setupTomcat() throws LifecycleException {
    tomcat = new Tomcat();
    ProductDAO productDAO = new ProductDAOImpl();
    OrderDetailDAO orderDetailDAO = new OrderDetailDAOImpl();
    ProductService productService = new ProductServiceImpl(productDAO);
    OrderDetailService orderDetailService = new OrderDetailServiceImpl(orderDetailDAO);
    tomcat.setPort(9090);
    tomcat.getConnector();

    var context = tomcat.addContext("", null);

    Tomcat.addServlet(context, "orderDetailServlet", new OrderDetailServlet(orderDetailService));
    context.addServletMappingDecoded("/order-details", "orderDetailServlet");
    Tomcat.addServlet(context, "productServlet", new ProductServlet(productService));
    context.addServletMappingDecoded("/products", "productServlet");

    tomcat.start();
  }

  /**
   * Останавливает сервер Tomcat после выполнения всех тестов.
   *
   * @throws LifecycleException если происходит ошибка при остановке Tomcat.
   */
  @AfterAll
  void cleanupTomcat() throws LifecycleException {
    if (tomcat != null) {
      tomcat.stop();
    }
  }

  /**
   * Тестирует доступность сервлета OrderDetailServlet.
   *
   * @throws Exception если происходит ошибка при выполнении запроса.
   */
  @Test
  void testOrderDetailServlet() throws Exception {
    int statusCode = 400;
    String endpointUrl = "http://localhost:9090/order-details";

    HttpURLConnection connection = (HttpURLConnection) new URL(endpointUrl).openConnection();
    connection.setRequestMethod("GET");

    int responseCode = connection.getResponseCode();
    assertEquals(statusCode, responseCode);
  }

  /**
   * Тестирует доступность сервлета ProductServlet.
   *
   * @throws IOException если происходит ошибка при выполнении запроса.
   */
  @Test
  void testProductServletAvailability() throws IOException {
    int statusCode = 400;
    String endpointUrl = "http://localhost:9090/products";

    HttpURLConnection connection = (HttpURLConnection) new URL(endpointUrl).openConnection();
    connection.setRequestMethod("GET");

    int responseCode = connection.getResponseCode();
    assertEquals(statusCode, responseCode);
  }
}