package EGEN5203.EcommerceTDD.dto;

import lombok.Data;

@Data public class AddProductsDto {
    private String productName;
    private double price;
    private Integer quantity;
}
