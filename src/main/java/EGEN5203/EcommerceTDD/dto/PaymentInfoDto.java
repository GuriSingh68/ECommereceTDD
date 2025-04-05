package EGEN5203.EcommerceTDD.dto;

import EGEN5203.EcommerceTDD.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
class PaymentInfoDto {
    private Long id;
    private String paymentMethod;
    private String cardLastFour;
    private Double amount;
    private LocalDateTime paymentDate;
    private PaymentStatus status;
}
