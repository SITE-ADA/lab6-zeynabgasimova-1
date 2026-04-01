package az.edu.ada.wm2.lab6.service;

import az.edu.ada.wm2.lab6.model.Product;
import az.edu.ada.wm2.lab6.model.dto.ProductRequestDto;
import az.edu.ada.wm2.lab6.model.dto.ProductResponseDto;
import az.edu.ada.wm2.lab6.model.mapper.ProductMapper;
import az.edu.ada.wm2.lab6.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Product product = productMapper.toEntity(dto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    public ProductResponseDto getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return productMapper.toResponseDto(product);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    public ProductResponseDto updateProduct(UUID id, ProductRequestDto dto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setProductName(dto.getProductName());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setExpirationDate(dto.getExpirationDate());

        Product updatedProduct = productRepository.save(existingProduct);

        return productMapper.toResponseDto(updatedProduct);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    public List<ProductResponseDto> getProductsExpiringBefore(LocalDate date) {
        return productRepository.findByExpirationDateBefore(date)
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    public List<ProductResponseDto> getProductsByPriceRange(BigDecimal min,
                                                            BigDecimal max) {
        return productRepository.findByPriceBetween(min, max)
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }
}