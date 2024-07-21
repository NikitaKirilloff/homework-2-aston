package kirilloffna.taskrest.service;

import kirilloffna.taskrest.dto.OrderDetailDTO;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 *  Сервис для работы с заказами.
 * <p>
 * Все методы кидают SQLException Если происходит ошибка при выполнении запроса.
 */
public interface OrderDetailService {

  /**
   * Создает новый объект заказа.
   * @param orderDetailDTO объект OrderDetailDTO, содержащий детали создаваемого заказа.
   */
  void createOrderDetail(OrderDetailDTO orderDetailDTO) throws SQLException;

  /**
   * Получает объект OrderDetailDTO по его ID.
   *
   * @param id ID OrderDetailDTO.
   * @return Optional, содержащий OrderDetailDTO, если он найден, или пустой Optional, если не найден.
   */
  Optional<OrderDetailDTO> getOrderDetailById(Long id) throws SQLException;

  /**
   * Обновляет существующий OrderDetail.
   *
   * @param orderDetailDTO объект DTO, содержащий обновленные детали заказа.
   * @return Optional, содержащий обновленный OrderDetailDTO, если обновление прошло успешно,
   * или пустой Optional, если OrderDetailDTO не найден.
   */
  Optional<OrderDetailDTO> updateOrderDetail(OrderDetailDTO orderDetailDTO) throws SQLException;

  /**
   * Удаляет OrderDetail по его ID.
   *
   * @param id ID OrderDetailDTO, для удаления.
   */
  void deleteOrderDetail(Long id) throws SQLException;

  /**
   * Получает все OrderDetailDTO из базы данных.
   *
   * @return список всех OrderDetailDTO.
   */
  List<OrderDetailDTO> getAllOrderDetails() throws SQLException;
}
