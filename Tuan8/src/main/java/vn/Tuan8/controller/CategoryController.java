package vn.Tuan8.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import vn.Tuan8.dto.CategoryInput;
import vn.Tuan8.entity.Category;
import vn.Tuan8.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @QueryMapping
    public List<Category> allCategories() {
        return categoryRepository.findAll();
    }

    @QueryMapping
    public Optional<Category> findCategoryById(@Argument Long id) {
        return categoryRepository.findById(id);
    }

    @MutationMapping
    public Category createCategory(@Argument CategoryInput input) {
        Category category = new Category();
        category.setName(input.getName());
        category.setImages(input.getImages());
        return categoryRepository.save(category);
    }

    @MutationMapping
    public Category updateCategory(@Argument Long id, @Argument CategoryInput input) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        existingCategory.setName(input.getName());
        existingCategory.setImages(input.getImages());

        return categoryRepository.save(existingCategory);
    }

    @MutationMapping
    public boolean deleteCategory(@Argument Long id) {
        categoryRepository.deleteById(id);
        return true;
    }
}
