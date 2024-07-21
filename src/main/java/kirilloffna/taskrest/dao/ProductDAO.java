package kirilloffna.taskrest.dao;

import kirilloffna.taskrest.model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для работы с сущностью {@link Product} в базе данных.
 * <p>
 * Все методы кидают SQLException Если происходит ошибка при выполнении запроса.
 */
public interface ProductDAO {

  /**
   * Сохраняет сущность {@link Product} в базе данных.
   *
   * @param product    Сущность {@link Product}, которую необходимо сохранить.
   * @param connection Соединение с базой данных.
   */
  void save(Product product, Connection connection) throws SQLException;

  /**
   * Ищет сущность {@link Product} по идентификатору.
   *
   * @param id         Идентификатор искомой сущности.
   * @param connection Соединение с базой данных.
   * @return Опционально {@link Product}, если сущность с таким идентификатором найдена, иначе пусто.
   */
  Optional<Product> findById(Long id, Connection connection) throws SQLException;

  /**
   * Обновляет сущность {@link Product} в базе данных.
   *
   * @param product    Сущность {@link Product}, которую необходимо обновить.
   * @param connection Соединение с базой данных.
   * @return Опционально обновлённая сущность {@link Product}, если обновление успешно, иначе пусто.
   */
  Optional<Product> update(Product product, Connection connection) throws SQLException;

  /**
   * Удаляет сущность {@link Product} из базы данных по идентификатору.
   *
   * @param id         Идентификатор удаляемой сущности.
   * @param connection Соединение с базой данных.
   */
  void deleteById(Long id, Connection connection) throws SQLException;

  /**
   * Возвращает список всех сущностей {@link Product} из базы данных.
   *
   * @param connection Соединение с базой данных.
   * @return Список всех сущностей {@link Product}.
   */
  List<Product> findAll(Connection connection) throws SQLException;
}

