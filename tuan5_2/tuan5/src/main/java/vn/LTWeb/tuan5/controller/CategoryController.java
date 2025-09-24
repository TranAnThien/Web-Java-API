package vn.LTWeb.tuan5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.LTWeb.tuan5.entity.Category;
import vn.LTWeb.tuan5.model.CategoryModel;
import vn.LTWeb.tuan5.service.CategoryService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("add")
    public String add(ModelMap model) {
        model.addAttribute("activePage", "categories");
        model.addAttribute("category", new Category());
        return "admin/categories/addOrEdit";
    }

    @PostMapping("/saveOrUpdate")
    public String saveOrUpdate(@ModelAttribute("category") Category category,
                               @RequestParam("imageFile") MultipartFile file,
                               RedirectAttributes redirect) {
        try {
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path projectRoot = Paths.get("").toAbsolutePath();

                Path uploadPath = projectRoot.resolve("tuan5/images");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());

                // Chỉ lưu tên file vào DB
                category.setImages(fileName);
            }
            categoryService.save(category);
            redirect.addFlashAttribute("message", "Lưu thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirect.addFlashAttribute("message", "Lỗi khi lưu!");
        }
        return "redirect:/admin/categories";
    }

    @RequestMapping("")
    public String list(ModelMap model) {
        model.addAttribute("activePage", "categories");
        List<Category> list = categoryService.findAll(Sort.by(Sort.Direction.ASC, "categoryName"));
        model.addAttribute("categories", list);
        return "admin/categories/list";
    }

    @GetMapping("edit/{id}")
    public ModelAndView edit(ModelMap model, @PathVariable("id") Integer id) {
        model.addAttribute("activePage", "categories");
        Optional<Category> opt = categoryService.findById(id);
        if (opt.isPresent()) {
            model.addAttribute("category", opt.get());
            return new ModelAndView("admin/categories/addOrEdit", model);
        }
        model.addAttribute("message", "Category is not existed!");
        return new ModelAndView("forward:/admin/categories", model);
    }

    @GetMapping("delete/{id}")
    public ModelAndView delete(ModelMap model, @PathVariable("id") Integer id) {
        categoryService.deleteById(id);
        model.addAttribute("message", "Category is deleted!!!");
        return new ModelAndView("forward:/admin/categories/searchpaginated", model);
    }

    @GetMapping("search")
    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
        model.addAttribute("activePage", "search");
        List<Category> list = StringUtils.hasText(name)
                ? categoryService.findByNameContaining(name)
                : categoryService.findAll();
        model.addAttribute("categories", list);
        return "admin/categories/search";
    }

    @RequestMapping("searchpaginated")
    public String searchPaginated(ModelMap model,
                                  @RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "page", required = false) Optional<Integer> page,
                                  @RequestParam(name = "size", required = false) Optional<Integer> size) {
        model.addAttribute("activePage", "search");

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("categoryName"));
        Page<Category> resultPage = StringUtils.hasText(name)
                ? categoryService.findByNameContaining(name, pageable)
                : categoryService.findAll(pageable);

        model.addAttribute("name", name);
        model.addAttribute("categoryPage", resultPage);

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);
            if (totalPages > 5) {
                if (end == totalPages) start = end - 4;
                else if (start == 1) end = start + 4;
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/categories/searchpaginated";
    }
}
