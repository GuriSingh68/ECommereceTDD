package EGEN5203.EcommerceTDD.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object for adding new products.
 * Contains validation constraints to ensure data integrity.
 */
@Data
public class AddProductsDto {

    @NotBlank(message = "Product name cannot be empty")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Price must be provided")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @NotNull(message = "Stock must be provided")
    @Positive(message = "Stock must be greater than or equal to zero")
    private Integer stock;

    @NotBlank(message = "Category cannot be empty")
    private String category;

    private String imageUrl;

    @Size(max = 10, message = "Tags can have a maximum of 10 items")
    private List<String> tags;

    @NotBlank(message = "Admin ID must be provided")
    private String admin;
}