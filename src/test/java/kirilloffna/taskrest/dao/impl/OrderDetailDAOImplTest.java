package kirilloffna.taskrest.dao.impl;

import kirilloffna.taskrest.model.OrderDetail;
import kirilloffna.taskrest.model.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kirilloffna.taskrest.dao.impl.DaoQueries.SELECT_ALL_ORDER_DETAILS;
import static kirilloffna.taskrest.dao.impl.DaoQueries.SELECT_PRODUCT_WITH_ORDER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки реализации {@link OrderDetailDAOImpl}.
 * Использует Mockito для создания мок-объектов и проверки взаимодействия с ними.
 */
class OrderDetailDAOImplTest {
  private final Connection connection = mock(Connection.class);

  private final PreparedStatement preparedStatement = mock(PreparedStatement.class);

  private final ResultSet resultSet = mock(ResultSet.class);

  private final OrderDetailDAOImpl orderDetailDAO = new OrderDetailDAOImpl();

  /**
   * Тестирует метод {@link OrderDetailDAOImpl#save(OrderDetail, Connection)}.
   * Проверяет, что метод {@link PreparedStatement#executeUpdate()} вызывается, а также устанавливает идентификатор для сохраненного заказа.
   */
  @Test
  void testSave() throws SQLException {
    OrderDetail orderDetail = new OrderDetail(null, OrderStatus.Готовится, new ArrayList<>(), BigDecimal.valueOf(100.00));

    when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getLong(1)).thenReturn(1L);

    orderDetailDAO.save(orderDetail, connection);

    verify(connection).setAutoCommit(false);
    verify(preparedStatement).setString(1, orderDetail.getOrderStatus().name());
    verify(preparedStatement).setBigDecimal(2, orderDetail.getTotalAmount());
    verify(preparedStatement).executeUpdate();
    verify(connection).commit();

    assertEquals(1L, orderDetail.getId());
  }

  /**
   * Тестирует метод {@link OrderDetailDAOImpl#update(OrderDetail, Connection)}.
   * Проверяет, что метод {@link PreparedStatement#executeUpdate()} вызывается, а также что возвращается обновленный заказ.
   */
  @Test
  void testUpdate() throws SQLException {
    OrderDetail orderDetail = new OrderDetail(1L, OrderStatus.Готов, List.of(), BigDecimal.valueOf(150.00));

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

    Optional<OrderDetail> updatedOrderDetail = orderDetailDAO.update(orderDetail, connection);

    verify(connection).setAutoCommit(false);

    verify(preparedStatement, times(1)).setString(1, orderDetail.getOrderStatus().name());
    verify(preparedStatement, times(1)).setBigDecimal(2, orderDetail.getTotalAmount());
    verify(preparedStatement, times(1)).setLong(3, orderDetail.getId());
    verify(preparedStatement, times(2)).executeUpdate();

    verify(preparedStatement, times(1)).setLong(1, orderDetail.getId());
    verify(preparedStatement, times(2)).executeUpdate();

    verify(connection).commit();

    assertTrue(updatedOrderDetail.isPresent());
    assertEquals(orderDetail, updatedOrderDetail.get());
  }

  /**
   * Тестирует метод {@link OrderDetailDAOImpl#findById(Long, Connection)}.
   * Проверяет, что метод возвращает ожидаемый заказ, включая его статус и сумму.
   */
  @Test
  void testFindById() throws SQLException {
    Long orderId = 1L;
    OrderDetail expectedOrderDetail = new OrderDetail(orderId, OrderStatus.Принят, null, BigDecimal.valueOf(200.00));

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getLong("id")).thenReturn(orderId);
    when(resultSet.getString("order_status")).thenReturn(OrderStatus.Принят.name());
    when(resultSet.getBigDecimal("total_amount")).thenReturn(BigDecimal.valueOf(200.00));

    Optional<OrderDetail> foundOrderDetail = orderDetailDAO.findById(orderId, connection);

    verify(preparedStatement, times(3)).setLong(1, orderId);
    verify(preparedStatement, times(3)).executeQuery();

    assertTrue(foundOrderDetail.isPresent());
    assertEquals(expectedOrderDetail, foundOrderDetail.get());
  }

  /**
   * Тестирует метод {@link OrderDetailDAOImpl#deleteById(Long, Connection)}.
   * Проверяет, что метод {@link PreparedStatement#executeUpdate()} вызывается для удаления заказа.
   */
  @Test
  void testDeleteById() throws SQLException {
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

    orderDetailDAO.deleteById(1L, connection);

    verify(connection).setAutoCommit(false);
    verify(preparedStatement, times(2)).setLong(1, 1L);
    verify(preparedStatement, times(2)).executeUpdate();
    verify(connection).commit();
  }

  /**
   * Тестирует метод {@link OrderDetailDAOImpl#findAll(Connection)}.
   * Проверяет, что метод возвращает список заказов, и что список содержит ожидаемые элементы.
   */
  @Test
  void testFindAll() throws SQLException {
    List<OrderDetail> expectedOrderDetails = new ArrayList<>();
    OrderDetail orderDetail1 = new OrderDetail(1L, OrderStatus.Готов, List.of(), BigDecimal.valueOf(100.00));
    OrderDetail orderDetail2 = new OrderDetail(2L, OrderStatus.Готовится, List.of(), BigDecimal.valueOf(200.00));
    expectedOrderDetails.add(orderDetail1);
    expectedOrderDetails.add(orderDetail2);

    PreparedStatement orderDetailsPreparedStatement = mock(PreparedStatement.class);
    when(connection.prepareStatement(SELECT_ALL_ORDER_DETAILS)).thenReturn(orderDetailsPreparedStatement);
    ResultSet orderDetailsResultSet = mock(ResultSet.class);
    when(orderDetailsPreparedStatement.executeQuery()).thenReturn(orderDetailsResultSet);
    when(orderDetailsResultSet.next()).thenReturn(true, true, false);
    when(orderDetailsResultSet.getLong("id")).thenReturn(1L, 2L);
    when(orderDetailsResultSet.getString("order_status")).thenReturn(OrderStatus.Готов.name(), OrderStatus.Готовится.name());
    when(orderDetailsResultSet.getBigDecimal("total_amount")).thenReturn(BigDecimal.valueOf(100.00), BigDecimal.valueOf(200.00));

    PreparedStatement productsPreparedStatement = mock(PreparedStatement.class);
    when(connection.prepareStatement(SELECT_PRODUCT_WITH_ORDER_ID)).thenReturn(productsPreparedStatement);
    ResultSet productsResultSet = mock(ResultSet.class);
    when(productsPreparedStatement.executeQuery()).thenReturn(productsResultSet);
    when(productsResultSet.next()).thenReturn(false);

    List<OrderDetail> foundOrderDetails = orderDetailDAO.findAll(connection);

    verify(orderDetailsPreparedStatement).executeQuery();
    verify(productsPreparedStatement, times(2)).setLong(anyInt(), anyLong());

    assertEquals(expectedOrderDetails.size(), foundOrderDetails.size());
    assertEquals(expectedOrderDetails.get(0), foundOrderDetails.get(0));
    assertEquals(expectedOrderDetails.get(1), foundOrderDetails.get(1));
  }
}
