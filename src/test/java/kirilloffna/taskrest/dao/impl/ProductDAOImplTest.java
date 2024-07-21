package kirilloffna.taskrest.dao.impl;

import kirilloffna.taskrest.model.CategoryType;
import kirilloffna.taskrest.model.Product;
import kirilloffna.taskrest.model.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kirilloffna.taskrest.dao.impl.DaoQueries.DELETE_PRODUCT_PRODUCT_CATEGORY_SQL;
import static kirilloffna.taskrest.dao.impl.DaoQueries.FIND_PRODUCT_CATEGORY_BY_PRODUCT_ID_SQL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки реализации {@link ProductDAOImpl}.
 * Использует Mockito для создания мок-объектов и проверки взаимодействия с ними.
 */
@ExtendWith(MockitoExtension.class)
class ProductDAOImplTest {

  private final Connection connection = mock(Connection.class);
  private final PreparedStatement preparedStatement = mock(PreparedStatement.class);
  private final ResultSet resultSet = mock(ResultSet.class);
  private final ProductDAOImpl productDAO = new ProductDAOImpl();

  private Product product;

  /**
   * Настраивает тестовые данные перед выполнением каждого теста.
   */
  @BeforeEach
  void setUp() {
    product = new Product();
    product.setId(1L);
    product.setName("Test Product");
    product.setPrice(BigDecimal.valueOf(99.99));
    product.setQuantity(10);
    product.setAvailable(true);
    product.setProductCategories(List.of(new ProductCategory(1L, "Category", CategoryType.Основное_блюдо, null)));
  }

  /**
   * Тестирует метод {@link ProductDAOImpl#save(Product, Connection)}.
   * Проверяет, что метод {@link PreparedStatement#executeUpdate()} вызывается один раз
   * и что идентификатор продукта устанавливается после сохранения.
   */
  @Test
  void testSaveProduct() throws SQLException {
    product = new Product();
    product.setId(1L);
    product.setName("Test Product");
    product.setPrice(BigDecimal.valueOf(99.99));
    product.setQuantity(10);
    product.setAvailable(true);
    product.setProductCategories(new ArrayList<>());

    when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
    when(preparedStatement.executeUpdate()).thenReturn(1);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getLong(1)).thenReturn(1L);

    productDAO.save(product, connection);

    verify(preparedStatement, times(1)).executeUpdate();
    assertNotNull(product.getId());
  }

  /**
   * Тестирует метод {@link ProductDAOImpl#findById(Long, Connection)}.
   * Проверяет, что метод возвращает ожидаемый продукт и его категории.
   */
  @Test
  void testFindById() throws SQLException {
    ProductCategory productCategory = new ProductCategory(1L, "Category", CategoryType.Основное_блюдо, null);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getLong("id")).thenReturn(product.getId());
    when(resultSet.getString("name")).thenReturn(product.getName());
    when(resultSet.getBigDecimal("price")).thenReturn(product.getPrice());
    when(resultSet.getInt("quantity")).thenReturn(product.getQuantity());
    when(resultSet.getBoolean("available")).thenReturn(product.isAvailable());

    ResultSet categoryResultSet = mock(ResultSet.class);
    PreparedStatement categoryPreparedStatement = mock(PreparedStatement.class);
    when(connection.prepareStatement(FIND_PRODUCT_CATEGORY_BY_PRODUCT_ID_SQL)).thenReturn(categoryPreparedStatement);
    when(categoryPreparedStatement.executeQuery()).thenReturn(categoryResultSet);
    when(categoryResultSet.next()).thenReturn(true).thenReturn(false);
    when(categoryResultSet.getLong("id")).thenReturn(productCategory.getId());
    when(categoryResultSet.getString("name")).thenReturn(productCategory.getName());
    when(categoryResultSet.getString("type")).thenReturn(productCategory.getType().toString());

    Optional<Product> foundProduct = productDAO.findById(1L, connection);

    assertTrue(foundProduct.isPresent());
    assertEquals(product, foundProduct.get());

    List<ProductCategory> categories = foundProduct.get().getProductCategories();
    assertEquals(1, categories.size());
    assertEquals(productCategory, categories.get(0));
  }

  /**
   * Тестирует метод {@link ProductDAOImpl#update(Product, Connection)}.
   * Проверяет, что метод {@link PreparedStatement#executeUpdate()} вызывается дважды.
   */
  @Test
  void testUpdateProduct() throws SQLException {
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeUpdate()).thenReturn(1);

    productDAO.update(product, connection);

    verify(preparedStatement, times(2)).executeUpdate();
  }

  /**
   * Тестирует метод {@link ProductDAOImpl#deleteById(Long, Connection)}.
   * Проверяет, что метод {@link PreparedStatement#executeUpdate()} вызывается дважды.
   */
  @Test
  void testDeleteById() throws SQLException {
    PreparedStatement statement = mock(PreparedStatement.class);

    when(connection.prepareStatement(DELETE_PRODUCT_PRODUCT_CATEGORY_SQL)).thenReturn(statement);
    when(statement.executeUpdate()).thenReturn(1);

    productDAO.deleteById(1L, connection);

    verify(statement, times(2)).executeUpdate();
  }

  /**
   * Тестирует метод {@link ProductDAOImpl#findAll(Connection)}.
   * Проверяет, что метод возвращает список продуктов, и что этот список не пустой.
   */
  @Test
  void testFindAll() throws SQLException {
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true).thenReturn(false);
    when(resultSet.getLong("id")).thenReturn(1L);
    when(resultSet.getString("name")).thenReturn("Test Product");
    when(resultSet.getBigDecimal("price")).thenReturn(BigDecimal.valueOf(99.99));
    when(resultSet.getInt("quantity")).thenReturn(10);
    when(resultSet.getBoolean("available")).thenReturn(true);

    List<Product> products = productDAO.findAll(connection);

    assertFalse(products.isEmpty());
    assertEquals(1, products.size());
  }
}