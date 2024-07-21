package kirilloffna.taskrest.dao.impl;

import lombok.experimental.UtilityClass;

/**
 * Утилитарный класс, содержащий SQL-запросы, используемые для работы с таблицами products и order_details в базе данных,
 * а также Лог-сообщения для отслеживания выполнения запросов.
 */
@UtilityClass
public class DaoQueries {
  public static final String INSERT_PRODUCT_SQL = "INSERT INTO products (name, price, quantity, available) VALUES (?, ?, ?, ?)";
  public static final String SELECT_PRODUCT_BY_ID = "SELECT * FROM products WHERE id = ?";
  public static final String UPDATE_PRODUCT_SQL = "UPDATE products SET name = ?, price = ?, quantity = ?, available = ? WHERE id = ?";
  public static final String SELECT_ALL_PRODUCTS = "SELECT * FROM products";
  public static final String INSERT_PRODUCT_CATEGORY =
          "INSERT INTO products_products_categories (product_id, category_id) VALUES (?, ?)";
  public static final String SELECT_PRODUCT_WITH_ORDER_ID =
          "SELECT p.id, p.name, p.price, p.quantity, p.available FROM products p WHERE p.order_detail_id = ?";
  public static final String DELETE_PRODUCT_BY_ORDER_ID_SQL = "DELETE FROM products WHERE order_detail_id = ?";

  public static final String SELECT_ORDER_DETAILS_BY_ID = "SELECT * FROM order_details WHERE id = ?";
  public static final String DELETE_ORDER_DETAILS_SQL = "DELETE FROM order_details WHERE id = ?";
  public static final String SELECT_ALL_ORDER_DETAILS = "SELECT * FROM order_details";
  public static final String INSERT_ORDER_DETAIL = "INSERT INTO order_details (order_status, total_amount) VALUES (?, ?)";
  public static final String UPDATE_ORDER_DETAIL = "UPDATE order_details SET order_status = ?, total_amount = ? WHERE id = ?";
  public static final String INSERT_PRODUCT =
          "INSERT INTO products (name, price, quantity, available, order_detail_id) VALUES (?, ?, ?, ?, ?)";

  public static final String SAVE_PRODUCT_CATEGORY_PRODUCT_SQL =
          "INSERT INTO products_products_categories (product_id, category_id) VALUES (?, ?)  ON CONFLICT (product_id, category_id) DO NOTHING";
  public static final String FIND_PRODUCT_CATEGORY_BY_PRODUCT_ID_SQL =
          "SELECT pc.* FROM products_categories pc JOIN products_products_categories pcg ON pc.id = pcg.category_id WHERE pcg.product_id = ?";
  public static final String DELETE_PRODUCT_PRODUCT_CATEGORY_SQL = "DELETE FROM products WHERE id = ?";

  public static final String EXECUTE_ORDER_DETAIL_SQL=  "OrderDetailDAOImpl. Executing query: {} with orderDetail: {}";
  public static final String PRODUCT_DAO_EXECUTE_WITH_PRODUCT_SQL = "ProductDAOImpl. Executing query: {} with product: {}";
  public static final String PRODUCT_DAO_EXECUTE_WITH_PRODUCT_ID_SQL = "ProductDAOImpl. Executing query: {} with productId: {}";
  public static final String PRODUCT_DAO_EXECUTE_SQL = "ProductDAOImpl. Executing query: {}";
}
