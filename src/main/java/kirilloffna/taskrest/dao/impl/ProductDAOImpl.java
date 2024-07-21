package kirilloffna.taskrest.dao.impl;

import kirilloffna.taskrest.dao.ProductDAO;
import kirilloffna.taskrest.model.CategoryType;
import kirilloffna.taskrest.model.Product;
import kirilloffna.taskrest.model.ProductCategory;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kirilloffna.taskrest.dao.impl.DaoQueries.*;

@Slf4j
public class ProductDAOImpl implements ProductDAO {

  @Override
  public void save(Product product, Connection connection) throws SQLException {
    log.info(PRODUCT_DAO_EXECUTE_SQL, INSERT_PRODUCT_SQL);

    connection.setAutoCommit(false);
    try (PreparedStatement preparedStatement = connection
            .prepareStatement(INSERT_PRODUCT_SQL, Statement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setString(1, product.getName());
      preparedStatement.setBigDecimal(2, product.getPrice());
      preparedStatement.setInt(3, product.getQuantity());
      preparedStatement.setBoolean(4, product.isAvailable());
      int affectedRows = preparedStatement.executeUpdate();

      if (affectedRows == 0) {
        log.warn("Creating product failed, no rows affected.");
      }

      try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          product.setId(generatedKeys.getLong(1));
        } else {
          log.warn("Creating product failed, no ID obtained.");
        }
      }

      if (product.getProductCategories() != null && !product.getProductCategories().isEmpty()) {
        for (ProductCategory category : product.getProductCategories()) {
          saveProductCategory(product.getId(), category.getId(), connection);
        }
      }
      connection.commit();
    } catch (SQLException e) {
      connection.rollback();
      log.error(PRODUCT_DAO_EXECUTE_WITH_PRODUCT_ID_SQL, INSERT_PRODUCT_SQL, product, e);
    }
  }

  @Override
  public Optional<Product> findById(Long id, Connection connection) throws SQLException {
    log.info(PRODUCT_DAO_EXECUTE_WITH_PRODUCT_ID_SQL, SELECT_PRODUCT_BY_ID, id);

    connection.setAutoCommit(false);
    Product product = null;
    try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID)) {
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        product = fillProduct(resultSet, connection);
      }

      connection.commit();
    } catch (SQLException e) {
      connection.rollback();
      log.error(PRODUCT_DAO_EXECUTE_WITH_PRODUCT_ID_SQL, SELECT_PRODUCT_BY_ID, id, e);
    }
    return Optional.ofNullable(product);
  }

  @Override
  public Optional<Product> update(Product product, Connection connection) throws SQLException {
    log.info(PRODUCT_DAO_EXECUTE_WITH_PRODUCT_SQL, UPDATE_PRODUCT_SQL, product);

    connection.setAutoCommit(false);
    try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_SQL)) {
      preparedStatement.setString(1, product.getName());
      preparedStatement.setBigDecimal(2, product.getPrice());
      preparedStatement.setInt(3, product.getQuantity());
      preparedStatement.setBoolean(4, product.isAvailable());
      preparedStatement.setLong(5, product.getId());
      preparedStatement.executeUpdate();

      if (product.getProductCategories() != null && !product.getProductCategories().isEmpty()) {
        for (ProductCategory category : product.getProductCategories()) {
          saveProductCategory(product.getId(), category.getId(), connection);
        }
      }

      connection.commit();
    } catch (SQLException e) {
      connection.rollback();
      log.error(PRODUCT_DAO_EXECUTE_WITH_PRODUCT_SQL, UPDATE_PRODUCT_SQL, product, e);
    }
    return Optional.ofNullable(product);
  }

  @Override
  public void deleteById(Long id, Connection connection) throws SQLException {
    log.info(PRODUCT_DAO_EXECUTE_WITH_PRODUCT_ID_SQL, DELETE_PRODUCT_PRODUCT_CATEGORY_SQL, id);

    connection.setAutoCommit(false);
    try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCT_PRODUCT_CATEGORY_SQL)) {
      preparedStatement.setLong(1, id);
      preparedStatement.executeUpdate();
      deleteProductCategory(id, connection);

      connection.commit();
    } catch (SQLException e) {
      connection.rollback();
      log.error(PRODUCT_DAO_EXECUTE_WITH_PRODUCT_ID_SQL, DELETE_PRODUCT_PRODUCT_CATEGORY_SQL, id, e);
    }
  }

  @Override
  public List<Product> findAll(Connection connection) throws SQLException {
    log.info(PRODUCT_DAO_EXECUTE_SQL, SELECT_ALL_PRODUCTS);

    connection.setAutoCommit(false);
    List<Product> products = new ArrayList<>();

    try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS)) {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Product product = fillProduct(resultSet, connection);
        products.add(product);
      }
      connection.commit();
    } catch (SQLException e) {
      connection.rollback();
      log.error("Error executing: {}", SELECT_ALL_PRODUCTS, e);
    }
    return products;
  }

  private void saveProductCategory(Long productId, Long categoryId, Connection connection) throws SQLException {
    try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_PRODUCT_CATEGORY_PRODUCT_SQL)) {
      preparedStatement.setLong(1, productId);
      preparedStatement.setLong(2, categoryId);
      preparedStatement.executeUpdate();
    }
  }

  private void deleteProductCategory(Long productId, Connection connection) throws SQLException {
    try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCT_PRODUCT_CATEGORY_SQL)) {
      preparedStatement.setLong(1, productId);
      preparedStatement.executeUpdate();
    }
  }

  private Product fillProduct(ResultSet resultSet, Connection connection) throws SQLException {
    Product product = new Product();
    product.setId(resultSet.getLong("id"));
    product.setName(resultSet.getString("name"));
    product.setPrice(resultSet.getBigDecimal("price"));
    product.setQuantity(resultSet.getInt("quantity"));
    product.setAvailable(resultSet.getBoolean("available"));
    product.setProductCategories(findCategoryByProductId(product.getId(), connection));
    return product;
  }

  private List<ProductCategory> findCategoryByProductId(Long productId, Connection connection) throws SQLException {
    List<ProductCategory> categories = new ArrayList<>();
    try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_PRODUCT_CATEGORY_BY_PRODUCT_ID_SQL)) {
      preparedStatement.setLong(1, productId);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        ProductCategory category = new ProductCategory();
        category.setId(resultSet.getLong("id"));
        category.setName(resultSet.getString("name"));
        category.setType(CategoryType.valueOf(resultSet.getString("type")));
        categories.add(category);
      }
    }
    return categories;
  }
}