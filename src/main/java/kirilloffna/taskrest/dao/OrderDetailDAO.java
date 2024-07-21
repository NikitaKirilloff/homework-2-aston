package kirilloffna.taskrest.dao;

import kirilloffna.taskrest.model.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для работы с сущностью {@link OrderDetail} в базе данных.
 * <p>
 * Все методы кидают SQLException Если происходит ошибка при выполнении запроса.
 */
public interface OrderDetailDAO {

  /**
   * Сохраняет сущность {@link OrderDetail} в базе данных.
   *
   * @param orderDetail Сущность {@link OrderDetail}, которую необходимо сохранить.
   * @param connection  Соединение с базой данных.
   */
  void save(OrderDetail orderDetail, Connection connection) throws SQLException;

  /**
   * Ищет сущность {@link OrderDetail} по идентификатору.
   *
   * @param id          Идентификатор искомой сущности.
   * @param connection  Соединение с базой данных.
   * @return Optional {@link OrderDetail}, если сущность с таким идентификатором найдена.
   */
  Optional<OrderDetail> findById(Long id, Connection connection) throws SQLException;

  /**
   * Обновляет сущность {@link OrderDetail} в базе данных.
   *
   * @param orderDetail Сущность {@link OrderDetail}, которую необходимо обновить.
   * @param connection  Соединение с базой данных.
   * @return Optional обновлённая сущность {@link OrderDetail}, если обновление успешно.
   */
  Optional<OrderDetail> update(OrderDetail orderDetail, Connection connection) throws SQLException;

  /**
   * Удаляет сущность {@link OrderDetail} из базы данных по идентификатору.
   *
   * @param id          Идентификатор удаляемой сущности.
   * @param connection  Соединение с базой данных.
   */
  void deleteById(Long id, Connection connection) throws SQLException;

  /**
   * Возвращает список всех сущностей {@link OrderDetail} из базы данных.
   *
   * @param connection  Соединение с базой данных.
   * @return Список всех сущностей {@link OrderDetail}.
   */
  List<OrderDetail> findAll(Connection connection) throws SQLException;
}
