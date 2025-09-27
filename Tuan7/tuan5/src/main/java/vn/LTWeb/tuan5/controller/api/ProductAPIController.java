package vn.LTWeb.tuan5.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.LTWeb.tuan5.entity.Category;
import vn.LTWeb.tuan5.entity.Product;
import vn.LTWeb.tuan5.model.Response;
import vn.LTWeb.tuan5.service.CategoryService;
import vn.LTWeb.tuan5.service.ProductService;
import vn.LTWeb.tuan5.service.StorageService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
public class ProductAPIController {

    @Autowired
    private ProductService productService;
    @Autowired private CategoryService categoryService;
    @Autowired private StorageService storageService;

    @GetMapping
    public ResponseEntity<Response> getAllProducts() {
        List<Product> products = productService.findAll();
        return new ResponseEntity<>(new Response(true, "Thành công", products), HttpStatus.OK);
    }

    @PostMapping("/addProduct")
    public ResponseEntity<Response> addProduct(@RequestParam("productName") String productName,
                                               @RequestParam("quantity") int quantity,
                                               @RequestParam("unitPrice") double unitPrice,
                                               @RequestParam("categoryId") int categoryId,
                                               @RequestParam(name = "images", required = false) MultipartFile imagesFile) {
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Category", null), HttpStatus.NOT_FOUND);
        }

        Product product = new Product();
        product.setProductName(productName);
        product.setQuantity(quantity);
        product.setUnitPrice(unitPrice);
        product.setCategory(optCategory.get());

        if (imagesFile != null && !imagesFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            product.setImages(storageService.getSorageFilename(imagesFile, uuid.toString()));
            storageService.store(imagesFile, product.getImages());
        }

        Product savedProduct = productService.save(product);
        return new ResponseEntity<>(new Response(true, "Thêm sản phẩm thành công", savedProduct), HttpStatus.OK);
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<Response> updateProduct(@RequestParam("productId") int productId,
                                                  @RequestParam("productName") String productName,
                                                  @RequestParam("quantity") int quantity,
                                                  @RequestParam("unitPrice") double unitPrice,
                                                  @RequestParam("categoryId") int categoryId,
                                                  @RequestParam(name = "images", required = false) MultipartFile imagesFile) {
        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Product", null), HttpStatus.NOT_FOUND);
        }

        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Category", null), HttpStatus.NOT_FOUND);
        }

        Product product = optProduct.get();
        product.setProductName(productName);
        product.setQuantity(quantity);
        product.setUnitPrice(unitPrice);
        product.setCategory(optCategory.get());

        if (imagesFile != null && !imagesFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            product.setImages(storageService.getSorageFilename(imagesFile, uuid.toString()));
            storageService.store(imagesFile, product.getImages());
        }

        Product updatedProduct = productService.save(product);
        return new ResponseEntity<>(new Response(true, "Cập nhật sản phẩm thành công", updatedProduct), HttpStatus.OK);
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<Response> deleteProduct(@RequestParam("productId") int productId) {
        if (productService.findById(productId).isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Product", null), HttpStatus.NOT_FOUND);
        }
        productService.deleteById(productId);
        return new ResponseEntity<>(new Response(true, "Xóa sản phẩm thành công", null), HttpStatus.OK);
    }
}
