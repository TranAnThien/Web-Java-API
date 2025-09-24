package vn.LTWeb.tuan5.service.ServiceImpl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import vn.LTWeb.tuan5.entity.Category;
import vn.LTWeb.tuan5.repository.CategoryRepository;
import vn.LTWeb.tuan5.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repo;

    @Override
    public <S extends Category> S save(S entity) {
        if (entity.getId() == null) {
            return repo.save(entity);
        } else {
            Optional<Category> opt = findById(entity.getId());
            if (opt.isPresent()) {
                if (StringUtils.isEmpty(entity.getImages())) {
                    entity.setImages(opt.get().getImages());
                } else {
                    //lấy lại images cũ
                    entity.setImages(entity.getImages());
                }
            }
            return repo.save(entity);
        }
    }

    @Override
    public List<Category> findAll() {
        return repo.findAll();
    }

    @Override
    public List<Category> findAll(Sort sort) {
        return repo.findAll(sort);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public Optional<Category> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Category entity) {
        repo.delete(entity);
    }

    @Override
    public void deleteAll() {
        repo.deleteAll();
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public List<Category> findByNameContaining(String name) {
        return repo.findByCategoryNameContaining(name);
    }
    @Override
    public Page<Category> findByNameContaining(String name, Pageable pageable) {
        return repo.findByCategoryNameContaining(name, pageable);
    }

    @Override
    public <S extends Category> Optional<S> findOne(Example<S> example) {
        return repo.findOne(example);
    }

    @Override
    public List<Category> findAllById(Iterable<Integer> ids) {
        return repo.findAllById(ids);

    }

    @Override
    public Optional<Category> findByCategoryName(String name) {
        return Optional.empty();
    }
}