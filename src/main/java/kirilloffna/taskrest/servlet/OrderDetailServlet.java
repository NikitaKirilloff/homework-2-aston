package kirilloffna.taskrest.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kirilloffna.taskrest.dto.OrderDetailDTO;
import kirilloffna.taskrest.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static kirilloffna.taskrest.utils.ServletConstants.*;
import static kirilloffna.taskrest.utils.ServletHelper.sendError;
import static kirilloffna.taskrest.utils.ServletHelper.setRespReqEncoded;

/**
 * Сервлет для управления обьектами OrderDetails.
 */
@Slf4j
@RequiredArgsConstructor
@WebServlet(name = "OrderDetailServlet", urlPatterns = "/order-details")
public class OrderDetailServlet extends HttpServlet {
  private final OrderDetailService orderDetailService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Обрабатывает GET-запросы для получения объекта OrderDetailDTO по ID или списка всех OrderDetailDTO.
   *
   * @param req  запрос от клиента
   * @param resp ответ клиенту
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    log.info("Received GET request for /order-details");
    setRespReqEncoded(req, resp);

    try {
      String idParam = req.getParameter("id");
      if (idParam != null) {
        Long id = Long.valueOf(idParam);
        Optional<OrderDetailDTO> orderDetail = orderDetailService.getOrderDetailById(id);
        if (orderDetail.isPresent()) {
          resp.getWriter().write(objectMapper.writeValueAsString(orderDetail.get()));
        } else {
          sendError(resp, HttpServletResponse.SC_NOT_FOUND, ORDER_DETAIL_NOT_FOUND, null);
        }
      } else {
        List<OrderDetailDTO> orderDetailsDTO = orderDetailService.getAllOrderDetails();
        resp.getWriter().write(objectMapper.writeValueAsString(orderDetailsDTO));
      }
    } catch (NumberFormatException e) {
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, INVALID_ORDER_ID, e);
    } catch (IOException | SQLException e) {
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, INVALID_ORDER_DATA, e);
    } catch (Exception e) {
      sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_PROCESSING_GET_REQUEST, e);
    }
  }

  /**
   * Обрабатывает POST-запросы для создания нового OrderDetail.
   *
   * @param req  запрос от клиента
   * @param resp ответ клиенту
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    log.info("Received POST request for /order-details");
    setRespReqEncoded(req, resp);

    try {
      OrderDetailDTO orderDetailDTO = objectMapper.readValue(req.getInputStream(), OrderDetailDTO.class);
      orderDetailService.createOrderDetail(orderDetailDTO);
      resp.getWriter().write(objectMapper.writeValueAsString(orderDetailDTO));
    } catch (IOException e) {
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, INVALID_ORDER_DATA, e);
    } catch (Exception e) {
      sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_PROCESSING_POST_REQUEST, e);
    }
  }

  /**
   * Обрабатывает PUT-запросы для обновления существующего OrderDetail.
   *
   * @param req  запрос от клиента
   * @param resp ответ клиенту
   */
  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
    log.info("Received PUT request for /order-details");
    setRespReqEncoded(req, resp);

    try {
      OrderDetailDTO orderDetailDTO = objectMapper.readValue(req.getInputStream(), OrderDetailDTO.class);

      if (orderDetailDTO.getId() == null || orderDetailDTO.getId() <= 0) {
        sendError(resp, HttpServletResponse.SC_BAD_REQUEST, INVALID_ORDER_ID, null);
        return;
      }

      Optional<OrderDetailDTO> orderDetailOpt = orderDetailService.getOrderDetailById(orderDetailDTO.getId());
      if (orderDetailOpt.isPresent()) {
        Optional<OrderDetailDTO> updatedOrderDetail = orderDetailService.updateOrderDetail(orderDetailDTO);
        if (updatedOrderDetail.isPresent()) {
          resp.getWriter().write(objectMapper.writeValueAsString(updatedOrderDetail.get()));
        }
      } else {
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, ORDER_DETAIL_NOT_FOUND);
      }

    } catch (IOException | SQLException e) {
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, INVALID_ORDER_DATA, e);
    } catch (Exception e) {
      sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_PROCESSING_PUT_REQUEST, e);
    }
  }

  /**
   * Обрабатывает DELETE-запросы для удаления OrderDetail по ID.
   *
   * @param req  запрос от клиента
   * @param resp ответ клиенту
   */
  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
    log.info("Received DELETE request for /order-details");
    setRespReqEncoded(req, resp);
    try {
      long id = Long.parseLong(req.getParameter("id"));
      if (id > 0) {
        orderDetailService.deleteOrderDetail(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
      }
    } catch (NumberFormatException | SQLException e) {
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, INVALID_ORDER_ID, e);
    } catch (Exception e) {
      sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_PROCESSING_DELETE_REQUEST, e);
    }
  }
}
