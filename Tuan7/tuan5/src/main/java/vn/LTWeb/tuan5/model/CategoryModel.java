package vn.LTWeb.tuan5.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryModel {
    private Integer id;

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = 255, message = "Tên tối đa 255 ký tự")
    private String categoryName;

    private String images;

    private Boolean isEdit = false;
}
