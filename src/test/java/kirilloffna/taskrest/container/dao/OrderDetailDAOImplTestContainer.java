package kirilloffna.taskrest.container.dao;

import kirilloffna.taskrest.HelperTest;
import kirilloffna.taskrest.dao.OrderDetailDAO;
import kirilloffna.taskrest.dao.impl.OrderDetailDAOImpl;
import kirilloffna.taskrest.model.OrderDetail;
import kirilloffna.taskrest.model.OrderStatus;
import kirilloffna.taskrest.model.Product;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static kirilloffna.taskrest.HelperTest.getProducts1;
import static kirilloffna.taskrest.HelperTest.getProducts2;

/**
 * Тестовый класс для проверки реализации {@link OrderDetailDAOImpl} с использованием Testcontainers.
 * Этот класс запускает контейнеры базы данных перед выполнением тестов и останавливает их после завершения всех тестов.
 */
@Testcontainers()
public class OrderDetailDAOImplTestContainer {

  private final OrderDetailDAO orderDetailDAO = new OrderDetailDAOImpl();

  /**
   * Выполняется перед выполнением всех тестов.
   * Запускает контейнеры базы данных для выполнения тестов.
   */
  @BeforeAll
  public static void beforeAll() throws SQLException {
    HelperTest.startContainer();
  }

  /**
   * Выполняется после завершения всех тестов.
   * Останавливает контейнеры базы данных.
   */
  @AfterAll
  public static void afterAll() throws SQLException {
    HelperTest.stopContainer();
  }

  /**
   * Тестирует метод save.
   * Проверяет, что детали заказа сохраняются и имеют идентификатор.
   */
  @Test
  void testSaveOrderDetail() throws SQLException {
    OrderDetail orderDetail = new OrderDetail();
    Optional<OrderDetail> foundOrderDetail;
    List<Product> products = getProducts1();

    orderDetail.setOrderStatus(OrderStatus.valueOf("Принят"));
    orderDetail.setTotalAmount(BigDecimal.valueOf(311.01));
    orderDetail.setProducts(products);
    orderDetail.setId(10L);

    orderDetailDAO.save(orderDetail, HelperTest.testConnection);
    foundOrderDetail = orderDetailDAO.findById(orderDetail.getId(), HelperTest.testConnection);

    Assertions.assertTrue(foundOrderDetail.isPresent());
    Assertions.assertEquals(orderDetail, foundOrderDetail.get());

    Product product1 = products.get(0);
    Product foundProduct1 = foundOrderDetail.get().getProducts().get(0);

    Product product2 = products.get(1);
    Product foundProduct2 = foundOrderDetail.get().getProducts().get(1);

    Assertions.assertEquals(product1, foundProduct1);
    Assertions.assertEquals(product2, foundProduct2);

    orderDetailDAO.deleteById(orderDetail.getId(), HelperTest.testConnection);
  }

  /**
   * Тестирует метод find.
   * Проверяет, что детали заказа сохраняются и имеют идентификатор, а после они возвращаются из базы данных.
   */
  @Test
  void testFindOrderDetail() throws SQLException {
    List<OrderDetail> orderDetailList;

    OrderDetail orderDetail1 = new OrderDetail();
    orderDetail1.setOrderStatus(OrderStatus.valueOf("Принят"));
    orderDetail1.setTotalAmount(BigDecimal.valueOf(310.99));
    orderDetail1.setProducts(getProducts1());
    orderDetail1.setId(20L);

    OrderDetail orderDetail2 = new OrderDetail();
    orderDetail2.setOrderStatus(OrderStatus.valueOf("Готовится"));
    orderDetail2.setTotalAmount(BigDecimal.valueOf(450.98));
    orderDetail2.setProducts(getProducts2());
    orderDetail2.setId(21L);

    orderDetailDAO.save(orderDetail1, HelperTest.testConnection);
    orderDetailDAO.save(orderDetail2, HelperTest.testConnection);

    orderDetailList = orderDetailDAO.findAll(HelperTest.testConnection);
    Assertions.assertTrue(orderDetailList != null && !orderDetailList.isEmpty());

    Assertions.assertEquals(2, orderDetailList.size());
    Assertions.assertEquals(orderDetail1, orderDetailList.get(0));
    Assertions.assertEquals(orderDetail2, orderDetailList.get(1));
  }

  /**
   * Тестирует метод update.
   * Проверяет, что детали заказа обновляются, и что обновленные детали заказа имеют заданные значения.
   */
  @Test
  void testUpdateOrderDetail() throws SQLException {
    OrderDetail orderDetail = new OrderDetail();
    OrderDetail newOrderDetail;
    OrderDetail updatedOrderDetail;

    orderDetail.setOrderStatus(OrderStatus.valueOf("Принят"));
    orderDetail.setTotalAmount(BigDecimal.valueOf(310.99));
    orderDetail.setProducts(getProducts1());
    orderDetail.setId(30L);
    orderDetail.setProducts(getProducts1());

    orderDetailDAO.save(orderDetail, HelperTest.testConnection);
    newOrderDetail = orderDetailDAO.findById(orderDetail.getId(), HelperTest.testConnection).get();

    Assertions.assertEquals(orderDetail.getOrderStatus(), newOrderDetail.getOrderStatus());
    Assertions.assertEquals(orderDetail.getTotalAmount(), newOrderDetail.getTotalAmount());

    newOrderDetail.setOrderStatus(OrderStatus.valueOf("Готовится"));
    newOrderDetail.setTotalAmount(BigDecimal.valueOf(450.99));
    newOrderDetail.setProducts(getProducts2());

    orderDetailDAO.update(newOrderDetail, HelperTest.testConnection);
    updatedOrderDetail = orderDetailDAO.findById(newOrderDetail.getId(), HelperTest.testConnection).get();

    Assertions.assertEquals(newOrderDetail, updatedOrderDetail);
  }

  /**
   * Тестирует метод delete.
   * Проверяет, что детали заказа могут быть удалены, и что после их удаления больше не существует в базе данных.
   */
  @Test
  void testDeleteOrderDetail() throws SQLException {
    Optional<OrderDetail> foundOrderDetail;
    OrderDetail orderDetail = new OrderDetail();

    orderDetail.setOrderStatus(OrderStatus.valueOf("Принят"));
    orderDetail.setTotalAmount(BigDecimal.valueOf(310.99));
    orderDetail.setProducts(getProducts1());
    orderDetail.setId(40L);

    orderDetailDAO.save(orderDetail, HelperTest.testConnection);
    foundOrderDetail = orderDetailDAO.findById(orderDetail.getId(), HelperTest.testConnection);

    Assertions.assertTrue(foundOrderDetail.isPresent());
    Assertions.assertEquals(orderDetail, foundOrderDetail.get());

    orderDetailDAO.deleteById(orderDetail.getId(), HelperTest.testConnection);

    Assertions.assertTrue(orderDetailDAO.findById(foundOrderDetail.get().getId(), HelperTest.testConnection).isEmpty());
  }
}
