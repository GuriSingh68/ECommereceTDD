package EGEN5203.EcommerceTDD.dto;

import lombok.Data;

@Data
public class UpdateProductsDto {
    private String productName;
    private double price;
    private Integer quantity;
}