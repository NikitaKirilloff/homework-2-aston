package kirilloffna.taskrest.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kirilloffna.taskrest.dto.OrderDetailDTO;
import kirilloffna.taskrest.model.OrderStatus;
import kirilloffna.taskrest.service.OrderDetailService;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kirilloffna.taskrest.HelperTest.createServletInputStream;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки работы сервлета {@link OrderDetailServlet}.
 * Все методы кидают Exception если происходит ошибка при выполнении запроса.
 */
class OrderDetailServletTest {
  private final OrderDetailService orderDetailService = mock(OrderDetailService.class);

  private final HttpServletRequest request = mock(HttpServletRequest.class);

  private final HttpServletResponse response = mock(HttpServletResponse.class);

  private final PrintWriter writer = mock(PrintWriter.class);

  private final OrderDetailServlet orderDetailServlet = new OrderDetailServlet(orderDetailService);

  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Тестирует обработку GET-запроса для получения OrderDetail по ID.
   */
  @Test
  void testDoGetOrderDetailById() throws Exception {
    OrderDetailDTO orderDetail = new OrderDetailDTO(1L, OrderStatus.Принят, Arrays.asList(), new BigDecimal("100.00"));
    when(request.getParameter("id")).thenReturn("1");
    when(orderDetailService.getOrderDetailById(1L)).thenReturn(Optional.of(orderDetail));
    when(response.getWriter()).thenReturn(writer);

    orderDetailServlet.doGet(request, response);

    verify(response).setContentType("application/json;charset=UTF-8");
    verify(writer).write(objectMapper.writeValueAsString(orderDetail));
  }

  /**
   * Тестирует обработку GET-запроса для получения списка всех OrderDetail.
   */
  @Test
  void testDoGetAllOrderDetails() throws Exception {
    List<OrderDetailDTO> orderDetails = Arrays.asList(
            new OrderDetailDTO(1L, OrderStatus.Принят, Arrays.asList(), new BigDecimal("100.00")),
            new OrderDetailDTO(2L, OrderStatus.Готовится, Arrays.asList(), new BigDecimal("200.00"))
    );
    when(orderDetailService.getAllOrderDetails()).thenReturn(orderDetails);
    when(response.getWriter()).thenReturn(writer);

    orderDetailServlet.doGet(request, response);

    verify(response).setContentType("application/json;charset=UTF-8");
    verify(writer).write(objectMapper.writeValueAsString(orderDetails));
  }

  /**
   * Тестирует обработку POST-запроса для создания нового OrderDetail.
   */
  @Test
  void testDoPost() throws Exception {
    OrderDetailDTO orderDetail = new OrderDetailDTO(1L, OrderStatus.Готов, Arrays.asList(), new BigDecimal("100.00"));
    when(request.getInputStream()).thenReturn(createServletInputStream(objectMapper.writeValueAsBytes(orderDetail)));
    when(response.getWriter()).thenReturn(writer);

    orderDetailServlet.doPost(request, response);

    verify(orderDetailService, times(1)).createOrderDetail(any(OrderDetailDTO.class));
    verify(writer).write(objectMapper.writeValueAsString(orderDetail));
  }

  /**
   * Тестирует обработку PUT-запроса для обновления существующего OrderDetail.
   */
  @Test
  void testDoPut() throws Exception {
    OrderDetailDTO orderDetail = new OrderDetailDTO(1L, OrderStatus.Готовится, Arrays.asList(), new BigDecimal("150.00"));
    when(request.getInputStream()).thenReturn(createServletInputStream(objectMapper.writeValueAsBytes(orderDetail)));
    when(response.getWriter()).thenReturn(writer);
    when(orderDetailService.getOrderDetailById(1L)).thenReturn(Optional.of(orderDetail));
    when(orderDetailService.updateOrderDetail(any(OrderDetailDTO.class))).thenReturn(Optional.of(orderDetail));

    orderDetailServlet.doPut(request, response);

    verify(response).setContentType("application/json;charset=UTF-8");
    verify(writer).write(objectMapper.writeValueAsString(orderDetail));
  }

  /**
   * Тестирует обработку DELETE-запроса для удаления OrderDetail по ID.
   */
  @Test
  void testDoDelete() throws Exception {
    when(request.getParameter("id")).thenReturn("1");

    orderDetailServlet.doDelete(request, response);

    verify(orderDetailService).deleteOrderDetail(1L);
    verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
  }
}