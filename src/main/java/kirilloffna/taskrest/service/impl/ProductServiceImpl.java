package kirilloffna.taskrest.service.impl;

import kirilloffna.taskrest.dao.ProductDAO;
import kirilloffna.taskrest.dao.impl.ProductDAOImpl;
import kirilloffna.taskrest.dto.ProductDTO;
import kirilloffna.taskrest.mapper.ProductMapper;
import kirilloffna.taskrest.model.Product;
import kirilloffna.taskrest.service.ProductService;
import kirilloffna.taskrest.utils.ConnectionProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private static final ProductMapper mapper = ProductMapper.INSTANCE;
  private final ProductDAO productDAO;

  @Override
  public void createProduct(ProductDTO productDTO) {
    log.debug("ProductServiceImpl. Creating product: {}", productDTO);

    Product entity = mapper.toEntity(productDTO);
    try (Connection connection = ConnectionProvider.getConnection()) {
      productDAO.save(entity, connection);
    } catch (SQLException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public Optional<ProductDTO> getProductById(Long id) throws SQLException {
    log.debug("ProductServiceImpl. Fetching product by id: {}", id);

    try (Connection connection = ConnectionProvider.getConnection()) {
      Optional<Product> product = productDAO.findById(id, connection);
      return Optional.of(mapper.toDTO(product.get()));
    }
  }

  @Override
  public Optional<ProductDTO> updateProduct(ProductDTO productDto) throws SQLException {
    log.debug("ProductServiceImpl. Updating product: {}", productDto);

    try (Connection connection = ConnectionProvider.getConnection()) {
      productDAO.findById(productDto.getId(), connection);
      productDAO.update(mapper.toEntity(productDto), connection);
      return Optional.of(productDto);
    }
  }

  @Override
  public void deleteProduct(Long id) throws SQLException {
    log.debug("ProductServiceImpl. Deleting product with id: {}", id);

    try (Connection connection = ConnectionProvider.getConnection()) {
      productDAO.deleteById(id, connection);
    }
  }

  @Override
  public List<ProductDTO> getAllProducts() throws SQLException {
    log.debug("ProductServiceImpl. Getting all Products");

    try (Connection connection = ConnectionProvider.getConnection()) {
      return productDAO.findAll(connection).stream()
              .map(mapper::toDTO)
              .toList();
    }
  }
}
