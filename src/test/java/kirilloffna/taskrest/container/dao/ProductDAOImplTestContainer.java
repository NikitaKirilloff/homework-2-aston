package kirilloffna.taskrest.container.dao;

import kirilloffna.taskrest.HelperTest;
import kirilloffna.taskrest.dao.ProductDAO;
import kirilloffna.taskrest.dao.impl.ProductDAOImpl;
import kirilloffna.taskrest.model.Product;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static kirilloffna.taskrest.HelperTest.getProducts3;

/**
 * Тестовый класс для проверки реализации {@link ProductDAOImpl} с использованием Testcontainers.
 * Этот класс запускает контейнеры базы данных перед выполнением тестов и останавливает их после завершения всех тестов.
 */
@Testcontainers
public class ProductDAOImplTestContainer {

  @Mock
  private final ProductDAO productDAO = new ProductDAOImpl();

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
   * Проверяет, что продукт сохраняется и имеет идентификатор.
   */
  @Test
  void testSaveProduct() throws SQLException {
    Product product = getProducts3().get(3);
    productDAO.save(product, HelperTest.testConnection);

    Optional<Product> foundProduct = productDAO.findById(product.getId(), HelperTest.testConnection);

    Assertions.assertTrue(foundProduct.isPresent());
    Assertions.assertNotNull(product.getId());
    Assertions.assertEquals(1, foundProduct.get().getId());

    productDAO.deleteById(product.getId(), HelperTest.testConnection);
  }

  /**
   * Тестирует метод find.
   * Проверяет, что продукт сохраняется и имеет идентификатор, а после он возвращается из базы данных.
   */
  @Test
  void testFindProduct() throws SQLException {
    Product product = getProducts3().get(2);
    productDAO.save(product, HelperTest.testConnection);

    Optional<Product> foundProduct = productDAO.findById(product.getId(), HelperTest.testConnection);

    Assertions.assertTrue(foundProduct.isPresent());
    Assertions.assertEquals(product.getName(), foundProduct.get().getName());

    productDAO.deleteById(product.getId(), HelperTest.testConnection);

    List<Product> productList = getProducts3();
    for (Product products : productList) {
      productDAO.save(products, HelperTest.testConnection);
    }

    Assertions.assertEquals(productList.size(), productDAO.findAll(HelperTest.testConnection).size());
  }

  /**
   * Тестирует метод update,.
   * Проверяет, что продукт обновляется и что обновленный продукт имеет заданные значения.
   */
  @Test
  void testUpdateProduct() throws SQLException {
    Product product = getProducts3().get(2);

    productDAO.save(product, HelperTest.testConnection);

    product.setName("Updated Test Product");
    productDAO.update(product, HelperTest.testConnection);

    Optional<Product> updatedProduct = productDAO.findById(product.getId(), HelperTest.testConnection);
    Assertions.assertTrue(updatedProduct.isPresent());
    Assertions.assertEquals("Updated Test Product", updatedProduct.get().getName());

    productDAO.deleteById(product.getId(), HelperTest.testConnection);
  }

  /**
   * Тестирует метод delete.
   * Проверяет, что продукт может быть удален и что после удаления его больше не существует в базе данных.
   */
  @Test
  void testDeleteProduct() throws SQLException {
    Product product = getProducts3().get(3);
    productDAO.save(product, HelperTest.testConnection);

    productDAO.deleteById(product.getId(), HelperTest.testConnection);

    Optional<Product> deletedProduct = productDAO.findById(product.getId(), HelperTest.testConnection);
    Assertions.assertFalse(deletedProduct.isPresent());
  }

  /**
   * Тестирует метод delete с неверным идентификатором.
   * Проверяет, что метод не вызывает исключений и корректно обрабатывает удаление с неверным идентификатором.
   */
  @Test
  void testDeleteProductInvalidId() throws SQLException {
    Product product = new Product();
    product.setId((long) -5);
    productDAO.deleteById(product.getId(), HelperTest.testConnection);

    Optional<Product> deletedProduct = productDAO.findById(product.getId(), HelperTest.testConnection);
    Assertions.assertFalse(deletedProduct.isPresent());
  }
}
