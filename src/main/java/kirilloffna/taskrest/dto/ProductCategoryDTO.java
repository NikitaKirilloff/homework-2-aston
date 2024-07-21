package kirilloffna.taskrest.dto;

import kirilloffna.taskrest.model.CategoryType;
import kirilloffna.taskrest.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object (DTO) для представления информации о продукте.
 * Используется для передачи данных между слоями приложения.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryDTO {
  Long id;
  String name;
  CategoryType type;
  List<Product> products;
}
