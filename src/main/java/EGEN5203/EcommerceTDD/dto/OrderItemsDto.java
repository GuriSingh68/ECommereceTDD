package EGEN5203.EcommerceTDD.dto;

import lombok.Data;

@Data
public class OrderItemsDto {
    private Long id;
    private ProductDto product;
    private Integer quantity;
    private Double itemPrice;
    private Double totalPrice;
}

