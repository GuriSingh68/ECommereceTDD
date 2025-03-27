package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.UpdatePaymentStatusDto;
import EGEN5203.EcommerceTDD.enums.CarDType;
import EGEN5203.EcommerceTDD.enums.PaymentStatus;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Order;
import EGEN5203.EcommerceTDD.model.Payments;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.PaymentRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
        private PaymentRepo paymentRepo;
    @Autowired
        private UserRepo userRepo;
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
        //  just simulating a successful payment
        Payments payment = new Payments();
        payment.setPaymentMethod(String.valueOf(CarDType.CREDIT_CARD));
        payment.setCardLastFour(cardNumber.substring(cardNumber.length() - 4));
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.SUCCESS);

        return paymentRepo.save(payment);
    }

    public List<Payments> fetchAllDetails() {
        return paymentRepo.findAll();
    }

    public Payments getPaymentsById(Integer paymentId,Long userId) {
        Users users = getUsers(userId);

        if (isAdmin(users)) {
            return getPayments(paymentId);
        }
        throw new IllegalArgumentException("Only admin access");
    }
    public Payments updatePaymentStatus(Integer paymentId, Long userId, UpdatePaymentStatusDto status) {
        Users users = getUsers(userId);
        Payments payments=getPayments(paymentId);
        if (isAdmin(users)){
            payments.setStatus(status.getPaymentStatus());
            paymentRepo.save(payments);
        }
        throw new IllegalArgumentException("Cannot update payment status...User needs admin role");
    }

    private static boolean isAdmin(Users users) {
        return users.getRole().equals(Roles.ADMIN);
    }

    private Payments getPayments(Integer paymentId) {
        return paymentRepo.findById(paymentId).orElseThrow(
                () -> new IllegalArgumentException("Not found")
        );
    }

    private Users getUsers(Long userId) {
        Users users=userRepo.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        return users;
    }


}
