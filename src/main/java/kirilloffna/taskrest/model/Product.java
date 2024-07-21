package kirilloffna.taskrest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Класс, представляющий Product.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
  private Long id;
  private String name;
  private BigDecimal price;
  private int quantity;
  private boolean available;
  private List<ProductCategory> productCategories;
}
