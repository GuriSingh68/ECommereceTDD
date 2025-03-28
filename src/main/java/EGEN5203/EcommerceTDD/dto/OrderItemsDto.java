package EGEN5203.EcommerceTDD.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemsDto {
    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    private String admin;
}