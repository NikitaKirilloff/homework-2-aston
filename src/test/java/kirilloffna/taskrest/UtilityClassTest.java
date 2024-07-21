package kirilloffna.taskrest;

import kirilloffna.taskrest.dao.impl.DaoQueries;
import kirilloffna.taskrest.utils.ConnectionProvider;
import kirilloffna.taskrest.utils.ServletConstants;
import kirilloffna.taskrest.utils.ServletHelper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестовый класс для проверки того, что Utility классы не могут быть созданы.
 */
class UtilityClassTest {

  /**
   * Метод проверяет, что указанные классы не могут быть созданы.
   *
   * @param clazz класс, который проверяется на невозможность создания указанных классов.
   */
  @ParameterizedTest
  @MethodSource("classes")
  void testClassCannotBeInstantiated(Class<?> clazz) {
    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
    for (Constructor<?> constructor : constructors) {
      constructor.setAccessible(true);
      Throwable exception = assertThrows(InvocationTargetException.class, constructor::newInstance).getCause();
      assertInstanceOf(UnsupportedOperationException.class, exception, "Expected UnsupportedOperationException");
    }
  }

  /**
   * Предоставляет поток классов для тестирования.
   *
   * @return поток классов, которые будут проверены.
   */
  static Stream<Class<?>> classes() {
    return Stream.of(
            ConnectionProvider.class,
            ServletHelper.class,
            DaoQueries.class,
            ServletConstants.class,
            HelperTest.class
    );
  }
}
