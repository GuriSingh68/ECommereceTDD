package EGEN5203.EcommerceTDD.dto;

import lombok.Data;

@Data public class AddProductsDto {
    private String productName;
    private double price;
    private Integer quantity;
    private String category;
    private String description;
}
