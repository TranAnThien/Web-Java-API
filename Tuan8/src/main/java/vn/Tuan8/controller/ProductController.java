package vn.Tuan8.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import vn.Tuan8.dto.ProductInput;
import vn.Tuan8.entity.Category;
import vn.Tuan8.entity.Product;
import vn.Tuan8.entity.User;
import vn.Tuan8.repository.CategoryRepository;
import vn.Tuan8.repository.ProductRepository;
import vn.Tuan8.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;

    @QueryMapping
    public List<Product> productsSortedByPrice() {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @QueryMapping
    public Optional<Product> findProductById(@Argument Long id) {
        return productRepository.findById(id);
    }

    @MutationMapping
    public Product createProduct(@Argument ProductInput input) {
        User user = userRepository.findById(Long.valueOf(input.getUserId()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Category category = categoryRepository.findById(Long.valueOf(input.getCategoryId()))
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Product product = new Product();
        product.setTitle(input.getTitle());
        product.setQuantity(input.getQuantity());
        product.setDesc(input.getDesc());
        product.setPrice(input.getPrice());
        product.setUser(user);
        product.setCategory(category);
        return productRepository.save(product);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument ProductInput input) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        User user = userRepository.findById(Long.valueOf(input.getUserId()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Category category = categoryRepository.findById(Long.valueOf(input.getCategoryId()))
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        existingProduct.setTitle(input.getTitle());
        existingProduct.setQuantity(input.getQuantity());
        existingProduct.setDesc(input.getDesc());
        existingProduct.setPrice(input.getPrice());
        existingProduct.setUser(user);
        existingProduct.setCategory(category);

        return productRepository.save(existingProduct);
    }

    @MutationMapping
    public boolean deleteProduct(@Argument Long id) {
        productRepository.deleteById(id);
        return true;
    }
}
