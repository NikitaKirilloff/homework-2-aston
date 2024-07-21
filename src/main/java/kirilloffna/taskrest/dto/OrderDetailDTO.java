package kirilloffna.taskrest.dto;

import kirilloffna.taskrest.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object (DTO) для представления информации о продукте.
 * Используется для передачи данных между слоями приложения.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
  private Long id;
  private OrderStatus orderStatus;
  private List<ProductDTO> products;
  private BigDecimal totalAmount;
}