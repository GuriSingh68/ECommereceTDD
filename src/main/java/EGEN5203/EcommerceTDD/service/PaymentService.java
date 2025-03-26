package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.enums.CarDType;
import EGEN5203.EcommerceTDD.enums.PaymentStatus;
import EGEN5203.EcommerceTDD.model.Order;
import EGEN5203.EcommerceTDD.model.Payments;
import EGEN5203.EcommerceTDD.repo.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {
    @Autowired
        private PaymentRepo paymentRepo;
    @Transactional
    public Payments processPayment(Order order) {
        //Create a new Payment
        Payments payment=new Payments();
        payment.setOrder(order);
        payment.setAmount(order.getTotalPrice());
        payment.setPaymentDate(LocalDateTime.now());

        // Always set status to SUCCESS as per requirement
        payment.setStatus(PaymentStatus.SUCCESS);

        // Save the payment record
        return paymentRepo.save(payment);
    }
    // Additional method to simulate card processing (if needed)
    public Payments processCardPayment(String cardNumber, String cvv, Double amount) {
        // In a real system, this would integrate with a payment gateway
        // Here, we'll just simulate a successful payment
        Payments payment = new Payments();
        payment.setPaymentMethod(String.valueOf(CarDType.CREDIT_CARD));
        payment.setCardLastFour(cardNumber.substring(cardNumber.length() - 4));
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.SUCCESS);

        return paymentRepo.save(payment);
    }
}
