package kirilloffna.taskrest.dao.impl;

import kirilloffna.taskrest.dao.OrderDetailDAO;
import kirilloffna.taskrest.model.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kirilloffna.taskrest.dao.impl.DaoQueries.*;

@Slf4j
public class OrderDetailDAOImpl implements OrderDetailDAO {

  @Override
  public void save(OrderDetail orderDetail, Connection connection) throws SQLException {
    log.info("Executing save with orderDetail: {}", orderDetail);

    try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER_DETAIL, Statement.RETURN_GENERATED_KEYS)) {
      connection.setAutoCommit(false);

      preparedStatement.setString(1, orderDetail.getOrderStatus().name());
      preparedStatement.setBigDecimal(2, orderDetail.getTotalAmount());
      preparedStatement.executeUpdate();

      try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          Long generatedId = generatedKeys.getLong(1);
          orderDetail.setId(generatedId);
        }
      }
      saveProducts(orderDetail, connection);
      connection.commit();
    } catch (Exception e) {
      connection.rollback();
      log.error("Error executing save: {}", e.getMessage(), e);
    }
  }

  @Override
  public Optional<OrderDetail> update(OrderDetail orderDetail, Connection connection) throws SQLException {
    log.info("Executing update with orderDetail: {}", orderDetail);
    try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_DETAIL)) {
      connection.setAutoCommit(false);
      preparedStatement.setString(1, orderDetail.getOrderStatus().name());
      preparedStatement.setBigDecimal(2, orderDetail.getTotalAmount());
      preparedStatement.setLong(3, orderDetail.getId());
      preparedStatement.executeUpdate();

      deleteOrderDetailProduct(orderDetail.getId(), connection);
      saveProducts(orderDetail, connection);

      connection.commit();
    } catch (Exception e) {
      connection.rollback();
      log.error("Error executing update: {}", e.getMessage(), e);
    }
    return Optional.of(orderDetail);
  }

  public Optional<OrderDetail> findById(Long id, Connection connection) throws SQLException {
    log.info("Executing findById with id: {}", id);

    OrderDetail orderDetail = null;
    try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDER_DETAILS_BY_ID)) {
      connection.setAutoCommit(false);
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        orderDetail = new OrderDetail();
        orderDetail.setId(resultSet.getLong("id"));
        orderDetail.setOrderStatus(OrderStatus.valueOf(resultSet.getString("order_status")));
        orderDetail.setTotalAmount(resultSet.getBigDecimal("total_amount"));
        orderDetail.setProducts(findProductByOrderId(orderDetail.getId(), connection));
      }
      connection.commit();
    } catch (Exception e) {
      connection.rollback();
      log.error("Error executing query: {} with orderDetailId: {}", SELECT_ORDER_DETAILS_BY_ID, id, e);
    }

    return Optional.ofNullable(orderDetail);
  }

  @Override
  public void deleteById(Long id, Connection connection) throws SQLException {
    log.info(EXECUTE_ORDER_DETAIL_SQL, DELETE_ORDER_DETAILS_SQL, id);

    try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ORDER_DETAILS_SQL)) {
      connection.setAutoCommit(false);

      deleteOrderDetailProduct(id, connection);
      preparedStatement.setLong(1, id);
      preparedStatement.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      connection.rollback();
      log.error("Error executing query: {} with orderDetailId: {}", DELETE_ORDER_DETAILS_SQL, id, e);
    }
  }

  @Override
  public List<OrderDetail> findAll(Connection connection) throws SQLException {
    log.info("OrderDetailDAOImpl. Executing query: {}", SELECT_ALL_ORDER_DETAILS);

    List<OrderDetail> orderDetailList = new ArrayList<>();
    try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ORDER_DETAILS)) {
      connection.setAutoCommit(false);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(resultSet.getLong("id"));
        orderDetail.setOrderStatus(OrderStatus.valueOf(resultSet.getString("order_status")));
        orderDetail.setTotalAmount(resultSet.getBigDecimal("total_amount"));
        orderDetail.setProducts(findProductByOrderId(orderDetail.getId(), connection));
        orderDetailList.add(orderDetail);
      }
      connection.commit();
    } catch (Exception e) {
      connection.rollback();
      log.error("Error executing query: {}", SELECT_ALL_ORDER_DETAILS, e);
    }
    return orderDetailList;
  }

  private void deleteOrderDetailProduct(Long orderDetailId, Connection connection) throws SQLException {
    try (PreparedStatement preparedStatement = connection
            .prepareStatement(DELETE_PRODUCT_BY_ORDER_ID_SQL)) {
      preparedStatement.setLong(1, orderDetailId);
      preparedStatement.executeUpdate();
    }
  }

  private List<Product> findProductByOrderId(Long orderDetailId, Connection connection) {
    List<Product> products = new ArrayList<>();
    String query = SELECT_PRODUCT_WITH_ORDER_ID;

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setLong(1, orderDetailId);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Product product = new Product();
        product.setId(resultSet.getLong("id"));
        product.setName(resultSet.getString("name"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setQuantity(resultSet.getInt("quantity"));
        product.setAvailable(resultSet.getBoolean("available"));
        product.setProductCategories(findProductCategoriesByProductId(product.getId(), connection));
        products.add(product);
      }
    } catch (SQLException e) {
      log.error("Error fetching products for orderDetailId: {}", orderDetailId, e);
    }

    return products;
  }

  public List<ProductCategory> findProductCategoriesByProductId(Long productId, Connection connection) {
    List<ProductCategory> categories = new ArrayList<>();
    String query = FIND_PRODUCT_CATEGORY_BY_PRODUCT_ID_SQL;

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setLong(1, productId);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        ProductCategory category = new ProductCategory();
        category.setId(resultSet.getLong("id"));
        category.setName(resultSet.getString("name"));
        category.setType(CategoryType.valueOf(resultSet.getString("type")));
        categories.add(category);
      }
    } catch (SQLException e) {
      log.error("Error fetching categories for productId: {}", productId, e);
    }

    return categories;
  }

  private void saveProducts(OrderDetail orderDetail, Connection connection) {
    for (Product product : orderDetail.getProducts()) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
        preparedStatement.setString(1, product.getName());
        preparedStatement.setBigDecimal(2, product.getPrice());
        preparedStatement.setInt(3, product.getQuantity());
        preparedStatement.setBoolean(4, product.isAvailable());
        preparedStatement.setLong(5, orderDetail.getId());
        preparedStatement.executeUpdate();

        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            Long generatedProductId = generatedKeys.getLong(1);
            product.setId(generatedProductId);
          }
        }

        if (product.getProductCategories() != null) {
          saveProductCategories(product, connection);
        }

      } catch (SQLException e) {
        log.error("Error saving product: {}", e.getMessage(), e);
      }
    }
  }

  private void saveProductCategories(Product product, Connection connection) {
    for (ProductCategory category : product.getProductCategories()) {
      try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_CATEGORY)) {
        preparedStatement.setLong(1, product.getId()); // связь с products
        preparedStatement.setLong(2, category.getId());
        preparedStatement.executeUpdate();
      } catch (SQLException e) {
        log.error("Error saving product category: {}", e.getMessage(), e);
      }
    }
  }
}
