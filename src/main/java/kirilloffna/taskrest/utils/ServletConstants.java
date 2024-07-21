package kirilloffna.taskrest.utils;

import kirilloffna.taskrest.servlet.OrderDetailServlet;
import kirilloffna.taskrest.servlet.ProductServlet;
import lombok.experimental.UtilityClass;

/**
 * Утилитарный класс, содержит набор вспомогательных констант для обработки запросов.
 * Используется в классах {@link ProductServlet} и {@link OrderDetailServlet}.
 */

@UtilityClass
public class ServletConstants {
  public static final String INVALID_PRODUCT_ID = "Invalid product ID";
  public static final String PRODUCT_NOT_FOUND = "Product not found";
  public static final String INVALID_PRODUCT_DATA = "Invalid product data";
  public static final String ERROR_PROCESSING_IO_ERROR = "Error processing IO error";
  public static final String ERROR_SENDING_ERROR_RESPONSE = "Error sending error response";
  public static final String ERROR_PROCESSING_SET_ENCODING = "Error processing set encoding";

  public static final String INVALID_ORDER_ID = "Invalid OrderDetail ID";
  public static final String ORDER_DETAIL_NOT_FOUND = "OrderDetail not found";
  public static final String INVALID_ORDER_DATA = "Invalid order data";

  public static final String ERROR_PROCESSING_POST_REQUEST = "Error processing POST request";
  public static final String ERROR_PROCESSING_GET_REQUEST = "Error processing GET request";
  public static final String ERROR_PROCESSING_PUT_REQUEST = "Error processing PUT request";
  public static final String ERROR_PROCESSING_DELETE_REQUEST = "Error processing DELETE request";
}
