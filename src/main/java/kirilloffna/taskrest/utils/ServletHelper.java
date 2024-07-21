package kirilloffna.taskrest.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kirilloffna.taskrest.servlet.OrderDetailServlet;
import kirilloffna.taskrest.servlet.ProductServlet;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static kirilloffna.taskrest.utils.ServletConstants.ERROR_PROCESSING_SET_ENCODING;
import static kirilloffna.taskrest.utils.ServletConstants.ERROR_SENDING_ERROR_RESPONSE;

/**
 * Утилитарный класс, для установки кодировки ответа Servlet, а также для отправки ошибки в случае ошибки в кодировке.
 * Используется в классах {@link ProductServlet} и {@link OrderDetailServlet}.
 */

@Slf4j
@UtilityClass
public class ServletHelper {

  public static void sendError(HttpServletResponse resp, int statusCode, String errorMessage, Exception e) {
    log.error(errorMessage, e);
    try {
      resp.sendError(statusCode, errorMessage);
    } catch (IOException ioException) {
      log.error(ERROR_SENDING_ERROR_RESPONSE, ioException);
    }
  }

  public static void setRespReqEncoded(HttpServletRequest req, HttpServletResponse resp) {
    try {
      req.setCharacterEncoding("UTF-8");
    } catch (UnsupportedEncodingException e) {
      log.error(ERROR_PROCESSING_SET_ENCODING, e);
    }
    resp.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json;charset=UTF-8");
  }
}
