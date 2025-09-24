package vn.Tuan8.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.Tuan8.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
