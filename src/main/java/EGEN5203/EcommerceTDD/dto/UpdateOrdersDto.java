package EGEN5203.EcommerceTDD.dto;

import EGEN5203.EcommerceTDD.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrdersDto {
    private OrderStatus status;
    private Integer orderId;
}