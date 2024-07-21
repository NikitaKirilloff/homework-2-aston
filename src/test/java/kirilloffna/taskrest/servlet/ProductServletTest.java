package kirilloffna.taskrest.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kirilloffna.taskrest.dto.ProductDTO;
import kirilloffna.taskrest.service.ProductService;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kirilloffna.taskrest.HelperTest.createServletInputStream;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки работы сервлета {@link ProductServlet}.
 * Все методы кидают Exception если происходит ошибка при выполнении запроса.
 */
class ProductServletTest {

  private final ProductService productService = mock(ProductService.class);

  private final HttpServletRequest request = mock(HttpServletRequest.class);

  private final HttpServletResponse response = mock(HttpServletResponse.class);

  private final PrintWriter writer = mock(PrintWriter.class);

  private final ProductServlet productServlet = new ProductServlet(productService);

  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Тестирует обработку GET-запроса для получения Product по ID.
   */
  @Test
  void testDoGetProductById() throws Exception {
    ProductDTO product = new ProductDTO(1L, "Test Product", new BigDecimal("10.00"), 10, true, null);
    when(request.getParameter("id")).thenReturn("1");
    when(productService.getProductById(1L)).thenReturn(Optional.of(product));
    when(response.getWriter()).thenReturn(writer);

    productServlet.doGet(request, response);

    verify(response).setContentType("application/json;charset=UTF-8");
    verify(writer).write(objectMapper.writeValueAsString(product));
  }

  /**
   * Тестирует обработку GET-запроса для получения списка всех Product.
   */
  @Test
  void testDoGetAllProducts() throws Exception {
    List<ProductDTO> products = Arrays.asList(
            new ProductDTO(1L, "Test Product 1", new BigDecimal("10.00"), 10, true, null),
            new ProductDTO(2L, "Test Product 2", new BigDecimal("20.00"), 20, true, null)
    );
    when(productService.getAllProducts()).thenReturn(products);
    when(response.getWriter()).thenReturn(writer);

    productServlet.doGet(request, response);

    verify(response).setContentType("application/json;charset=UTF-8");
    verify(writer).write(objectMapper.writeValueAsString(products));
  }

  /**
   * Тестирует обработку POST-запроса для создания нового Product.
   */
  @Test
  void testDoPost() throws Exception {
    ProductDTO product = new ProductDTO(1L, "Test Product", new BigDecimal("10.00"), 10, true, null);
    when(request.getInputStream()).thenReturn(createServletInputStream(objectMapper.writeValueAsBytes(product)));
    when(response.getWriter()).thenReturn(writer);

    productServlet.doPost(request, response);

    verify(productService, times(1)).createProduct(any(ProductDTO.class));
    verify(writer).write(objectMapper.writeValueAsString(product));
  }

  /**
   * Тестирует обработку PUT-запроса для обновления существующего Product.
   */
  @Test
  void testDoPut() throws Exception {
    ProductDTO product = new ProductDTO(1L, "Updated Product", new BigDecimal("15.00"), 15, true, null);
    when(request.getInputStream()).thenReturn(createServletInputStream(objectMapper.writeValueAsBytes(product)));
    when(response.getWriter()).thenReturn(writer);
    when(productService.getProductById(1L)).thenReturn(Optional.of(product));
    when(productService.updateProduct(any(ProductDTO.class))).thenReturn(Optional.of(product));

    productServlet.doPut(request, response);

    verify(response).setContentType("application/json;charset=UTF-8");
    verify(writer).write(objectMapper.writeValueAsString(product));
  }

  /**
   * Тестирует обработку DELETE-запроса для удаления Product по ID.
   */
  @Test
  void testDoDelete() throws Exception {
    when(request.getParameter("id")).thenReturn("1");

    productServlet.doDelete(request, response);

    verify(productService).deleteProduct(1L);
    verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
  }
}