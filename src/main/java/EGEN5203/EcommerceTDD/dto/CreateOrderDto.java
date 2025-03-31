package EGEN5203.EcommerceTDD.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {
    @NotNull(message = "Order items cannot be null")
    private List<OrderItemRequestDto> orderItems;
}
