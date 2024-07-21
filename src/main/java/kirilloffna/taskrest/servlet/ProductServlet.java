package kirilloffna.taskrest.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kirilloffna.taskrest.dto.ProductDTO;
import kirilloffna.taskrest.service.ProductService;
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
 * Сервлет для управления обьектами Products.
 */
@Slf4j
@RequiredArgsConstructor
@WebServlet(name = "ProductServlet", urlPatterns = "/products")
public class ProductServlet extends HttpServlet {
  private final ProductService productService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Обрабатывает GET-запросы для получения объекта ProductDTO по ID или списка всех ProductDTO.
   *
   * @param req  запрос от клиента
   * @param resp ответ клиенту
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    log.info("Received GET request for /products");
    setRespReqEncoded(req, resp);

    try {
      String idParam = req.getParameter("id");
      if (idParam != null) {
        Long id = Long.valueOf(idParam);
        Optional<ProductDTO> productDTO = productService.getProductById(id);
        if (productDTO.isPresent()) {
          resp.getWriter().write(objectMapper.writeValueAsString(productDTO.get()));
        } else {
          resp.sendError(HttpServletResponse.SC_NOT_FOUND, PRODUCT_NOT_FOUND);
        }
      } else {
        List<ProductDTO> products = productService.getAllProducts();
        resp.getWriter().write(objectMapper.writeValueAsString(products));
      }
    } catch (NumberFormatException e) {
      log.error(INVALID_PRODUCT_ID, e);
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, INVALID_PRODUCT_ID, e);
    } catch (IOException | SQLException e) {
      log.error(ERROR_PROCESSING_GET_REQUEST, e);
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, ERROR_PROCESSING_IO_ERROR, e);
    } catch (Exception e) {
      log.error(ERROR_PROCESSING_GET_REQUEST, e);
      sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_PROCESSING_GET_REQUEST, e);
    }
  }

  /**
   * Обрабатывает POST-запросы для создания нового Product.
   *
   * @param req  запрос от клиента
   * @param resp ответ клиенту
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    log.info("Received POST request for /products");
    setRespReqEncoded(req, resp);

    try {
      ProductDTO productDTO = objectMapper.readValue(req.getInputStream(), ProductDTO.class);
      productService.createProduct(productDTO);
      resp.getWriter().write(objectMapper.writeValueAsString(productDTO));
    } catch (IOException e) {
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, INVALID_PRODUCT_DATA, e);
    } catch (Exception e) {
      sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_PROCESSING_POST_REQUEST, e);
    }
  }

  /**
   * Обрабатывает PUT-запросы для обновления существующего Product.
   *
   * @param req  запрос от клиента
   * @param resp ответ клиенту
   */
  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
    log.info("Received PUT request for /products");
    setRespReqEncoded(req, resp);

    try {
      ProductDTO productDTO = objectMapper.readValue(req.getInputStream(), ProductDTO.class);

      if (productDTO.getId() == null || productDTO.getId() <= 0) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, INVALID_PRODUCT_ID);
        return;
      }

      Optional<ProductDTO> productOpt = productService.getProductById(productDTO.getId());

      if (productOpt.isPresent()) {
        Optional<ProductDTO> updatedProductDTO = productService.updateProduct(productDTO);
        if (updatedProductDTO.isPresent()) {
          resp.getWriter().write(objectMapper.writeValueAsString(updatedProductDTO.get()));
        }
      } else {
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, PRODUCT_NOT_FOUND);
      }

    } catch (IOException e) {
      log.error("Error reading product data", e);
      sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_PROCESSING_IO_ERROR, e);
    } catch (Exception e) {
      log.error(ERROR_PROCESSING_PUT_REQUEST, e);
      sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_PROCESSING_PUT_REQUEST, e);
    }
  }

  /**
   * Обрабатывает DELETE-запросы для удаления Product по ID.
   *
   * @param req  запрос от клиента
   * @param resp ответ клиенту
   */
  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
    log.info("Received DELETE request for /products");

    try {
      long id = Long.parseLong(req.getParameter("id"));
      if (id > 0) {
        productService.deleteProduct(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
      }
      else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } catch (NumberFormatException | SQLException e) {
      log.error(INVALID_PRODUCT_ID, e);
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, INVALID_PRODUCT_ID, e);
    } catch (Exception e) {
      log.error(ERROR_PROCESSING_DELETE_REQUEST, e);
      sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_PROCESSING_DELETE_REQUEST, e);
    }
  }
}
