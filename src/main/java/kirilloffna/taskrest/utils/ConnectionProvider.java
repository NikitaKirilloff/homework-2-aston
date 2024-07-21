package kirilloffna.taskrest.utils;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Утилитарный класс, содержит методы для подключения к основной базе данных. Подключение осуществляется через JDBC.
 */
@Slf4j
@UtilityClass
public class ConnectionProvider {
  private static final Properties properties = new Properties();
  @Setter
  private static Connection mockConnection;

  static {
    try (InputStream input = new FileInputStream("src/main/resources/application.properties")) {
      properties.load(input);
    } catch (IOException e) {
      log.error("Error loading properties file", e);
    }
  }

  @SneakyThrows
  public static Connection getConnection() {
    Class.forName("org.postgresql.Driver");
    String url = properties.getProperty("db.url");
    String username = properties.getProperty("db.username");
    String password = properties.getProperty("db.password");

    if (mockConnection != null) {
      return mockConnection;
    }

    return DriverManager.getConnection(url, username, password);
  }
}
