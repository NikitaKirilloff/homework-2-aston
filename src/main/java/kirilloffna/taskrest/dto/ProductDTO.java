package kirilloffna.taskrest.dto;

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
public class ProductDTO {
  private Long id;
  private String name;
  private BigDecimal price;
  private int quantity;
  private boolean available;
  private List<ProductCategoryDTO> productCategories;
}
