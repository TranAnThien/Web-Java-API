package vn.LTWeb.tuan5.service;

import vn.LTWeb.tuan5.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    <S extends Product> S save(S entity);
    List<Product> findAll();
    Optional<Product> findById(Integer id);
    void deleteById(Integer id);
}
