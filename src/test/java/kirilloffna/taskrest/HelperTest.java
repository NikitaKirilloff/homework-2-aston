package kirilloffna.taskrest;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import kirilloffna.taskrest.model.Product;
import lombok.experimental.UtilityClass;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Утилитарный класс для тестирования.
 * Предоставляет методы для работы с базой данных и создания тестовых данных.
 */
@UtilityClass
public class HelperTest {

  /**
   * Соединение с тестовой базой данных.
   */
  public static Connection testConnection;

  /**
   * Контейнер PostgreSQL для тестирования.
   */
  @Container
  public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:alpine3.18")
          .withDatabaseName("test_restaurant_db")
          .withUsername("test_user")
          .withPassword("test_password");

  /**
   * Запускает контейнер PostgreSQL и устанавливает соединение с базой данных.
   * Создает необходимые таблицы в базе данных.
   */
  public static void startContainer() throws SQLException {
    postgresContainer.start();

    String jdbcUrl = postgresContainer.getJdbcUrl();
    String username = postgresContainer.getUsername();
    String password = postgresContainer.getPassword();

    testConnection = DriverManager.getConnection(jdbcUrl, username, password);

    createTables(testConnection);
  }

  /**
   * Останавливает контейнер PostgreSQL и закрывает соединение с базой данных.
   */
  public static void stopContainer() throws SQLException {
    if (testConnection != null && !testConnection.isClosed()) {
      testConnection.close();
    }
    postgresContainer.stop();
  }

  /**
   * Создает {@link ServletInputStream} из массива байтов.
   *
   * @param data массив байтов, представляющий входные данные.
   * @return {@link ServletInputStream}, использующий указанный массив байтов.
   */
  public static ServletInputStream createServletInputStream(byte[] data) {
    return new ServletInputStream() {
      private final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);

      @Override
      public int read() {
        return byteArrayInputStream.read();
      }

      @Override
      public boolean isFinished() {
        return byteArrayInputStream.available() == 0;
      }

      @Override
      public boolean isReady() {
        return true;
      }

      @Override
      public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
      }
    };
  }

  /**
   * Создает список тестовых продуктов для использования в тестах.
   */
  public static List<Product> getProducts1() {
    List<Product> products = new ArrayList<>();
    Product product1 = new Product();
    product1.setName("Чай");
    product1.setPrice(BigDecimal.valueOf(120.99));
    product1.setQuantity(1);
    product1.setAvailable(true);
    product1.setProductCategories(List.of());

    Product product2 = new Product();
    product2.setName("Кофе");
    product2.setPrice(BigDecimal.valueOf(190.02));
    product2.setQuantity(2);
    product2.setAvailable(true);
    product2.setProductCategories(List.of());

    products.add(product1);
    products.add(product2);
    return products;
  }

  /**
   * Создает список тестовых продуктов для использования в тестах.
   */
  public static List<Product> getProducts2() {
    List<Product> products = new ArrayList<>();
    Product product1 = new Product();
    product1.setName("Салат");
    product1.setPrice(BigDecimal.valueOf(200.99));
    product1.setQuantity(1);
    product1.setAvailable(true);
    product1.setProductCategories(List.of());

    Product product2 = new Product();
    product2.setName("Гаспачо");
    product2.setPrice(BigDecimal.valueOf(249.99));
    product2.setQuantity(2);
    product2.setAvailable(true);
    product2.setProductCategories(List.of());

    products.add(product1);
    products.add(product2);
    return products;
  }

  /**
   * Создает список тестовых продуктов для использования в тестах.
   */
  public static List<Product> getProducts3() {
    List<Product> products = new ArrayList<>();
    Product product1 = new Product();
    product1.setName("Картошка фри");
    product1.setPrice(BigDecimal.valueOf(170.99));
    product1.setQuantity(1);
    product1.setAvailable(true);
    product1.setProductCategories(List.of());

    Product product2 = new Product();
    product2.setName("Бургер");
    product2.setPrice(BigDecimal.valueOf(350.00));
    product2.setQuantity(2);
    product2.setAvailable(true);
    product2.setProductCategories(List.of());

    Product product3 = new Product();
    product3.setName("Пицца");
    product3.setPrice(BigDecimal.valueOf(500.00));
    product3.setQuantity(7);
    product3.setAvailable(true);
    product3.setProductCategories(List.of());

    Product product4 = new Product();
    product4.setName("Греческий салат");
    product4.setPrice(BigDecimal.valueOf(300.00));
    product4.setQuantity(5);
    product4.setAvailable(true);
    product4.setProductCategories(List.of());

    products.add(product1);
    products.add(product2);
    products.add(product3);
    products.add(product4);
    return products;
  }

  /**
   * Выполняет SQL-скрипт для создания таблиц в базе данных.
   */
  private static void createTables(Connection connection) {
    try (Statement statement = connection.createStatement()) {
      String schema = readResourceFile();
      for (String sql : schema.split(";")) {
        if (!sql.trim().isEmpty()) {
          statement.execute(sql);
        }
      }
    } catch (SQLException ignored) {
    }
  }

  /**
   * Читает SQL-скрипт из файла.
   */
  private static String readResourceFile() {
    InputStream inputStream = HelperTest.class.getClassLoader().getResourceAsStream("initTestDB.sql");
    if (inputStream == null) {
      throw new IllegalArgumentException("File not found: " + "initTestDB.sql");
    }
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      return reader.lines().collect(Collectors.joining("\n"));
    } catch (Exception e) {
      throw new RuntimeException("Failed to read file: " + "initTestDB.sql", e);
    }
  }
}
