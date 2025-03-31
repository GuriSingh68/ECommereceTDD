package EGEN5203.EcommerceTDD.dto;

import EGEN5203.EcommerceTDD.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private UserDto user;
    private List<OrderItemsDto> orderItems;
    private Double totalPrice;
    private LocalDateTime orderDate;
    private OrderStatus status;
}