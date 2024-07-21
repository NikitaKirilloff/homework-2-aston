package kirilloffna.taskrest.service.impl;

import kirilloffna.taskrest.dao.ProductDAO;
import kirilloffna.taskrest.dto.ProductDTO;
import kirilloffna.taskrest.mapper.ProductMapper;
import kirilloffna.taskrest.model.Product;
import kirilloffna.taskrest.utils.ConnectionProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки реализации {@link ProductServiceImpl}.
 * Использует Mockito для создания мок-объектов и проверки взаимодействия с ними.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

  private final ProductDAO productDAO = mock(ProductDAO.class);

  private final ProductServiceImpl productService = new ProductServiceImpl(productDAO);

  /**
   * Настраивает моки и тестовые данные перед выполнением каждого теста.
   */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    Connection connection = mock(Connection.class);
    ConnectionProvider.setMockConnection(connection);
  }

  /**
   * Тестирует метод {@link ProductServiceImpl#createProduct(ProductDTO)}.
   * Проверяет, что метод {@link ProductDAO#save(Product, Connection)} вызывается один раз с правильными параметрами.
   */
  @Test
  void testCreateProduct() throws SQLException {
    ProductDTO productDTO = new ProductDTO(1L, "Test Product",
            BigDecimal.valueOf(129, 55), 10, true, List.of());

    doNothing().when(productDAO).save(any(Product.class), any(Connection.class));

    productService.createProduct(productDTO);

    verify(productDAO, times(1)).save(any(Product.class), any(Connection.class));
  }

  /**
   * Тестирует метод {@link ProductServiceImpl#getProductById(Long)}.
   * Проверяет, что метод {@link ProductDAO#findById(Long, Connection)} возвращает ожидаемый результат.
   */
  @Test
  void testGetProductById() throws SQLException {
    ProductDTO productDTO = new ProductDTO(1L, "Test Product",
            BigDecimal.valueOf(129, 55), 10, true, List.of());
    Product product = ProductMapper.INSTANCE.toEntity(productDTO);

    when(productDAO.findById(eq(1L), any(Connection.class))).thenReturn(Optional.of(product));

    Optional<ProductDTO> result = productService.getProductById(1L);

    assertTrue(result.isPresent());
    assertEquals(productDTO, result.get());
  }

  /**
   * Тестирует метод {@link ProductServiceImpl#updateProduct(ProductDTO)}.
   * Проверяет, что метод {@link ProductDAO#update(Product, Connection)} возвращает обновленный результат.
   */
  @Test
  void testUpdateProduct() throws SQLException {
    ProductDTO productDTO = new ProductDTO(1L, "Test Product",
            BigDecimal.valueOf(129, 55), 10, true, List.of());
    Product product = ProductMapper.INSTANCE.toEntity(productDTO);

    when(productDAO.findById(eq(1L), any(Connection.class))).thenReturn(Optional.of(product));
    when(productDAO.update(any(Product.class), any(Connection.class))).thenReturn(Optional.of(product));

    Optional<ProductDTO> result = productService.updateProduct(productDTO);

    assertTrue(result.isPresent());
    assertEquals(productDTO, result.get());
    verify(productDAO, times(1)).update(any(Product.class), any(Connection.class));
  }

  /**
   * Тестирует метод {@link ProductServiceImpl#deleteProduct(Long)}.
   * Проверяет, что метод {@link ProductDAO#deleteById(Long, Connection)} вызывается один раз с правильными параметрами.
   */
  @Test
  void testDeleteProduct() throws SQLException {
    doNothing().when(productDAO).deleteById(eq(1L), any(Connection.class));

    productService.deleteProduct(1L);

    verify(productDAO, times(1)).deleteById(eq(1L), any(Connection.class));
  }

  /**
   * Тестирует метод {@link ProductServiceImpl#getAllProducts()}.
   * Проверяет, что метод {@link ProductDAO#findAll(Connection)} возвращает ожидаемый список продуктов.
   */
  @Test
  void testGetAllProducts() throws SQLException {
    List<ProductDTO> productDTOList = Arrays.asList(
            new ProductDTO(1L, "Test Product 1",
                    BigDecimal.valueOf(129, 55), 10, true, List.of()),
            new ProductDTO(2L, "Test Product 2",
                    BigDecimal.valueOf(89, 55), 2, true, List.of()),
            new ProductDTO(3L, "Test Product 3",
                    BigDecimal.valueOf(99, 11), 0, false, List.of())

    );

    List<Product> products = productDTOList.stream()
            .map(ProductMapper.INSTANCE::toEntity)
            .toList();

    when(productDAO.findAll(any(Connection.class))).thenReturn(products);

    List<ProductDTO> result = productService.getAllProducts();

    assertEquals(productDTOList.size(), result.size());
    assertEquals(productDTOList, result);

  }
}


