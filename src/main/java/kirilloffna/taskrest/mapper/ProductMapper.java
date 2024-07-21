package kirilloffna.taskrest.mapper;

import kirilloffna.taskrest.dto.OrderDetailDTO;
import kirilloffna.taskrest.dto.ProductDTO;
import kirilloffna.taskrest.model.OrderDetail;
import kirilloffna.taskrest.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Интерфейс маппера для преобразования объектов Product и ProductDTO.
 * Использует библиотеку MapStruct для автоматической генерации реализации маппера.
 */
@Mapper
public interface ProductMapper {
  /**
   * Экземпляр маппера.
   */
  ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

  /**
   * Преобразует объект Product в объект ProductDTO.
   *
   * @param product {@link Product}, Сущность которую необходимо преобразовать.
   * @return DTO {@link ProductDTO}, соответствующий сущности.
   */
  ProductDTO toDTO(Product product);

  /**
   * Преобразует объект ProductDTOв объект Product.
   *
   * @param productDTO {@link ProductDTO}, DTO которое необходимо преобразовать.
   * @return Сущность {@link Product}, соответствующая DTO.
   */
  Product toEntity(ProductDTO productDTO);
}
