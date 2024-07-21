package kirilloffna.taskrest.service.impl;

import kirilloffna.taskrest.dao.OrderDetailDAO;
import kirilloffna.taskrest.dto.OrderDetailDTO;
import kirilloffna.taskrest.mapper.OrderDetailMapper;
import kirilloffna.taskrest.model.OrderDetail;
import kirilloffna.taskrest.model.OrderStatus;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки реализации {@link OrderDetailServiceImpl}.
 * Использует Mockito для создания мок-объектов и проверки взаимодействия с ними.
 */
@ExtendWith(MockitoExtension.class)
class OrderDetailServiceImplTest {

  private final OrderDetailDAO orderDetailDAO = mock(OrderDetailDAO.class);

  private final OrderDetailServiceImpl orderDetailService = new OrderDetailServiceImpl(orderDetailDAO);

  /**
   * Настраивает моки и тестовые данные перед выполнением каждого теста.
   */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    Connection connection = mock(Connection.class);
    ConnectionProvider.setMockConnection(connection); // Мок соединение
  }

  /**
   * Тестирует метод {@link OrderDetailServiceImpl#createOrderDetail(OrderDetailDTO)}.
   * Проверяет, что метод {@link OrderDetailDAO#save(OrderDetail, Connection)} вызывается один раз с правильными параметрами.
   */
  @Test
  void testCreateOrderDetail() throws SQLException {
    OrderDetailDTO orderDetailDTO = new OrderDetailDTO(1L, OrderStatus.Принят, List.of(), BigDecimal.valueOf(150.05));

    doNothing().when(orderDetailDAO).save(any(OrderDetail.class), any(Connection.class));

    orderDetailService.createOrderDetail(orderDetailDTO);

    verify(orderDetailDAO, times(1)).save(any(OrderDetail.class), any(Connection.class));
  }

  /**
   * Тестирует метод {@link OrderDetailServiceImpl#getOrderDetailById(Long)}.
   * Проверяет, что метод {@link OrderDetailDAO#findById(Long, Connection)} возвращает ожидаемый результат.
   */
  @Test
  void testGetOrderDetailById() throws SQLException {
    OrderDetailDTO orderDetailDTO = new OrderDetailDTO(1L, OrderStatus.Принят, List.of(), BigDecimal.valueOf(150.05));
    OrderDetail orderDetail = OrderDetailMapper.INSTANCE.toEntity(orderDetailDTO);

    when(orderDetailDAO.findById(eq(1L), any(Connection.class))).thenReturn(Optional.of(orderDetail));

    Optional<OrderDetailDTO> result = orderDetailService.getOrderDetailById(1L);

    assertTrue(result.isPresent());
    assertEquals(orderDetailDTO, result.get());
  }

  /**
   * Тестирует метод {@link OrderDetailServiceImpl#updateOrderDetail(OrderDetailDTO)}.
   * Проверяет, что метод {@link OrderDetailDAO#update(OrderDetail, Connection)} возвращает обновленный результат.
   */
  @Test
  void testUpdateOrderDetail() throws SQLException {
    OrderDetailDTO orderDetailDTO = new OrderDetailDTO(1L, OrderStatus.Готов, List.of(), BigDecimal.valueOf(150.05));
    OrderDetail orderDetail = OrderDetailMapper.INSTANCE.toEntity(orderDetailDTO);

    when(orderDetailDAO.findById(eq(1L), any(Connection.class))).thenReturn(Optional.of(orderDetail));
    when(orderDetailDAO.update(any(OrderDetail.class), any(Connection.class))).thenReturn(Optional.of(orderDetail));

    Optional<OrderDetailDTO> result = orderDetailService.updateOrderDetail(orderDetailDTO);

    assertTrue(result.isPresent());
    assertEquals(orderDetailDTO, result.get());
    verify(orderDetailDAO, times(1)).update(any(OrderDetail.class), any(Connection.class));
  }

  /**
   * Тестирует метод {@link OrderDetailServiceImpl#deleteOrderDetail(Long)}.
   * Проверяет, что метод {@link OrderDetailDAO#deleteById(Long, Connection)} вызывается один раз с правильными параметрами.
   */
  @Test
  void testDeleteOrderDetail() throws SQLException {
    doNothing().when(orderDetailDAO).deleteById(anyLong(), any(Connection.class));

    orderDetailService.deleteOrderDetail(1L);

    verify(orderDetailDAO, times(1)).deleteById(eq(1L), any(Connection.class));
  }

  /**
   * Тестирует метод {@link OrderDetailServiceImpl#getAllOrderDetails()}.
   * Проверяет, что метод {@link OrderDetailDAO#findAll(Connection)} возвращает ожидаемый список деталей заказов.
   */
  @Test
  void testGetAllOrderDetails() throws SQLException {
    List<OrderDetailDTO> orderDetailDTOList = Arrays.asList(
            new OrderDetailDTO(1L, OrderStatus.Принят, List.of(), BigDecimal.valueOf(100.05)),
            new OrderDetailDTO(2L, OrderStatus.Готовится, List.of(), BigDecimal.valueOf(210.05))
    );
    List<OrderDetail> orderDetails = orderDetailDTOList.stream()
            .map(OrderDetailMapper.INSTANCE::toEntity)
            .toList();

    when(orderDetailDAO.findAll(any(Connection.class))).thenReturn(orderDetails);

    List<OrderDetailDTO> result = orderDetailService.getAllOrderDetails();

    assertEquals(orderDetailDTOList.size(), result.size());
    assertEquals(orderDetailDTOList, result);
  }
}