package kirilloffna.taskrest.model;

import lombok.Data;

/**
 * Класс, представляющий OrderApproval.
 */
@Data
public class OrderApproval {
  private Long id;
  private OrderDetail orderDetail;
}
