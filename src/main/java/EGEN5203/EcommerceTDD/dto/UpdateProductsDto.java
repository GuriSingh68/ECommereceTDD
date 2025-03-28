package EGEN5203.EcommerceTDD.dto;

import lombok.Data;

/**
 * Data Transfer Object for updating existing products.
 * Contains fields that can be modified for a product.
 */
@Data
public class UpdateProductsDto {
    private String name;
    private double price;
    private Integer stock;
    private String imageUrl;
    private String description;
    private String category;
}