package kirilloffna.taskrest.mapper;

import kirilloffna.taskrest.dto.OrderDetailDTO;
import kirilloffna.taskrest.model.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Интерфейс маппера для преобразования объектов OrderDetail и OrderDetailDTO.
 * Использует библиотеку MapStruct для автоматической генерации реализации маппера.
 */
@Mapper
public interface OrderDetailMapper {
  /**
   * Экземпляр маппера.
   */
  OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);

  /**
   * Преобразует объект OrderDetail в объект OrderDetailDTO.
   *
   * @param orderDetail Сущность {@link OrderDetail}, которую необходимо преобразовать.
   * @return DTO {@link OrderDetailDTO}, соответствующий сущности.
   */
  OrderDetailDTO toDTO(OrderDetail orderDetail);

  /**
   * Преобразует объект OrderDetailDTO в объект OrderDetail.
   *
   * @param orderDetailDTO {@link OrderDetailDTO}, DTO которое необходимо преобразовать.
   * @return Сущность {@link OrderDetail}, соответствующая DTO.
   */
  OrderDetail toEntity(OrderDetailDTO orderDetailDTO);
}
