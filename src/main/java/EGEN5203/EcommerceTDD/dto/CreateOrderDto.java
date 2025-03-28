package EGEN5203.EcommerceTDD.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {
    @NotNull(message = "Total price cannot be null")
    private Double totalPrice;

    @NotNull(message = "Payment method cannot be null")
    private String paymentMethod;

    @NotNull(message = "Customer ID cannot be null")
    private Long customer;

    @NotNull(message = "Products cannot be null")
    private List<OrderItemsDto> products;

    private String status;
}

