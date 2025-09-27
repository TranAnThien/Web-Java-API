package vn.LTWeb.tuan5.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.LTWeb.tuan5.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer > {
    List<Category> findByCategoryNameContaining(String name);
    Page<Category> findByCategoryNameContaining(String name, Pageable pageable);
    Optional<Category> findByCategoryName(String name);
}
