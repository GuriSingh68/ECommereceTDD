package EGEN5203.EcommerceTDD.dto;

import EGEN5203.EcommerceTDD.enums.PaymentStatus;
import lombok.Data;

@Data
public class UpdatePaymentStatusDto {
    private PaymentStatus paymentStatus;
}
