package kirilloffna.taskrest.service;

import kirilloffna.taskrest.dto.ProductDTO;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с продуктами.
 * <p>
 * Все методы кидают SQLException Если происходит ошибка при выполнении запроса.
 */
public interface ProductService {

  /**
   * Создает новый объект продукта.
   *
   * @param productDto объект ProductDTO, содержащий продукт.
   */
  void createProduct(ProductDTO productDto) throws SQLException;

  /**
   * Получает объект ProductDTO по его ID.
   *
   * @param id ID ProductDTO.
   * @return Optional, содержащий ProductDTO, если он найден, или пустой Optional, если не найден.
   */
  Optional<ProductDTO> getProductById(Long id) throws SQLException;

  /**
   * Обновляет существующий ProductDTO.
   *
   * @param productDto объект DTO, содержащий обновленные данные продукта.
   * @return Optional, содержащий обновленный ProductDTO, если обновление прошло успешно,
   * или пустой Optional, если ProductDTO не найден.
   */
  Optional<ProductDTO> updateProduct(ProductDTO productDto) throws SQLException;

  /**
   * Удаляет Product по его ID.
   *
   * @param id ID ProductDTO, для удаления.
   */
  void deleteProduct(Long id) throws SQLException;

  /**
   * Получает все ProductDTO из базы данных.
   *
   * @return список всех ProductDTO.
   */
  List<ProductDTO> getAllProducts() throws SQLException;
}
