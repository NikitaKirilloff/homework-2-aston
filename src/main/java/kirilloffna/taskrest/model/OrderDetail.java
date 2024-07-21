package kirilloffna.taskrest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Класс, представляющий OrderDetail.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
  private Long id;
  private OrderStatus orderStatus;
  private List<Product> products;
  private BigDecimal totalAmount;
}

