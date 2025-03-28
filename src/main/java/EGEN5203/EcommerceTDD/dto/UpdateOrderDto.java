package EGEN5203.EcommerceTDD.dto;

import lombok.Data;

@Data
public class UpdateOrderDto {
    private String status;
    private String paymentStatus;
    private String transactionId;
}
