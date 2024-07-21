package kirilloffna.taskrest.service.impl;

import kirilloffna.taskrest.dao.OrderDetailDAO;
import kirilloffna.taskrest.dto.OrderDetailDTO;
import kirilloffna.taskrest.mapper.OrderDetailMapper;
import kirilloffna.taskrest.model.OrderDetail;
import kirilloffna.taskrest.service.OrderDetailService;
import kirilloffna.taskrest.utils.ConnectionProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
  private static final OrderDetailMapper mapper = OrderDetailMapper.INSTANCE;
  private final OrderDetailDAO orderDetailDAO;

  @Override
  public void createOrderDetail(OrderDetailDTO orderDetailDTO) throws SQLException {
    log.debug("OrderDetailServiceImpl. Creating orderDetail {}", orderDetailDTO);

    OrderDetail entity = mapper.toEntity(orderDetailDTO);
    try (Connection connection = ConnectionProvider.getConnection()) {
      orderDetailDAO.save(entity, connection);
    }
  }

  @Override
  public Optional<OrderDetailDTO> getOrderDetailById(Long id) throws SQLException {
    log.debug("OrderDetailServiceImpl. Fetching orderDetail by id: {}", id);

    try (Connection connection = ConnectionProvider.getConnection()) {
      Optional<OrderDetail> orderDetail = orderDetailDAO.findById(id, connection);
      return Optional.of(mapper.toDTO(orderDetail.get()));
    }
  }

  @Override
  public Optional<OrderDetailDTO> updateOrderDetail(OrderDetailDTO orderDetailDTO) throws SQLException {
    log.debug("OrderDetailServiceImpl. Updating OrderDetail: {}", orderDetailDTO);

    try (Connection connection = ConnectionProvider.getConnection()) {
      orderDetailDAO.findById(orderDetailDTO.getId(), connection);
      orderDetailDAO.update(mapper.toEntity(orderDetailDTO), connection);
      return Optional.of(orderDetailDTO);
    }
  }

  @Override
  public void deleteOrderDetail(Long id) throws SQLException {
    log.debug("OrderDetailServiceImpl. Deleting OrderDetail with id: {}", id);

    try (Connection connection = ConnectionProvider.getConnection()) {
      orderDetailDAO.deleteById(id, connection);
    }
  }

  @Override
  public List<OrderDetailDTO> getAllOrderDetails() throws SQLException {
    log.debug("OrderDetailServiceImpl. Getting all OrderDetails");

    try (Connection connection = ConnectionProvider.getConnection()) {
      return orderDetailDAO.findAll(connection).stream()
              .map(mapper::toDTO)
              .toList();
    }
  }
}
