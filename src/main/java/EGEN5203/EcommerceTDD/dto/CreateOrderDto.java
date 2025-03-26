package EGEN5203.EcommerceTDD.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {
    private Long productId;
    private Integer quantity;
    private List<OrderItemsDto> orderItems;
}
