package az.edu.ada.wm2.lab6.service;

import az.edu.ada.wm2.lab6.model.Category;
import az.edu.ada.wm2.lab6.model.Product;
import az.edu.ada.wm2.lab6.model.dto.CategoryRequestDto;
import az.edu.ada.wm2.lab6.model.dto.CategoryResponseDto;
import az.edu.ada.wm2.lab6.model.dto.ProductResponseDto;
import az.edu.ada.wm2.lab6.model.mapper.CategoryMapper;
import az.edu.ada.wm2.lab6.model.mapper.ProductMapper;
import az.edu.ada.wm2.lab6.repository.CategoryRepository;
import az.edu.ada.wm2.lab6.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ProductRepository productRepository,
                               ProductMapper productMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public CategoryResponseDto create(CategoryRequestDto dto) {
        Category category = CategoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toResponseDto(saved);
    }

    public List<CategoryResponseDto> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toResponseDto)
                .toList();
    }

    public void addProduct(UUID categoryId, UUID productId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.getCategories().add(category);
        productRepository.save(product);
    }

    public List<ProductResponseDto> getProducts(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return category.getProducts()
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }
}