package EGEN5203.EcommerceTDD.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AddToCartDto {
    @NotEmpty(message = "Products cannot be empty")
    private List<ProductDto> products;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;
}

