package vn.LTWeb.tuan5.service.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.LTWeb.tuan5.entity.Product;
import vn.LTWeb.tuan5.repository.ProductRepository;
import vn.LTWeb.tuan5.service.ProductService;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public <S extends Product> S save(S entity) {
        return productRepository.save(entity);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }
}
