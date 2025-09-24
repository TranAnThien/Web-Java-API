package vn.Tuan8.dto;

import lombok.Data;

@Data
public class ProductInput {
    private String title;
    private int quantity;
    private String desc;
    private double price;
    private String userId;
    private String categoryId;
}
