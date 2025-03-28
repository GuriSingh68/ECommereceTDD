package EGEN5203.EcommerceTDD.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object for product-related operations that require product ID and quantity.
 */
@Data
public class ProductDto {
    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    private String farmer;
}
