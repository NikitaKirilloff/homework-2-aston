package kirilloffna.taskrest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс, представляющий ProductCategory.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
  private Long id;
  private String name;
  private CategoryType type;
  private List<Product> products;
}