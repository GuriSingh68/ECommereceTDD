package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.UpdatePaymentStatusDto;
import EGEN5203.EcommerceTDD.enums.CardType;
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
import java.util.Random;
import java.util.UUID;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private UserRepo userRepo;
    @Transactional
    public Payments processPayment(Order order) {
        //if (order == null)  {throw new NullPointerException("Received a null order in processPayment!");}

        System.out.println("Processing payment for Order ID: " + order.getId());
        Payments payment = new Payments();
        payment.setOrder(order);
        payment.setAmount(order.getTotalPrice());
        payment.setPaymentMethod(String.valueOf(CardType.CREDIT_CARD));
        payment.setCardLastFour(String.valueOf(1245));
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.SUCCESS);

        return paymentRepo.save(payment);
    }
//    public class RandomFourDigit {
//        public static void main(String[] args) {
//            Random random = new Random();
//            int randomNumber = 1000 + random.nextInt(9000); // Generates a number between 1000 and 9999
//            System.out.println("Random 4-digit number: " + randomNumber);
//        }
//    }
    // Additional method to simulate card processing
    public Payments processCardPayment(String cardNumber, String cvv, Double amount) {
        //  just simulating a successful payment
        Payments payment = new Payments();
        payment.setPaymentMethod(String.valueOf(CardType.CREDIT_CARD));
        payment.setCardLastFour(cardNumber.substring(cardNumber.length() - 4));
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.SUCCESS);

        return paymentRepo.save(payment);
    }

    public List<Payments> fetchAllDetails() {
        return paymentRepo.findAll();
    }

    /**
     * Method to get Payment Details By Id for any user
     * @param paymentId
     * @param userId
     * @return
     */
    public Payments getPaymentsById(Integer paymentId,Long userId) {
        Users users = getUsers(userId);

        if (isAdmin(users.getRole())) {
            return getPayments(paymentId);
        }
        throw new IllegalArgumentException("Only admin access");
    }
    private static boolean isAdmin(Roles users) {
        return users.equals(Roles.ADMIN);
    }
    private Users getUsers(Long userId) {
        Users users=userRepo.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        return users;
    }
    public Payments updatePaymentStatus(Integer paymentId, Long userId, UpdatePaymentStatusDto status) {
        Users users=userRepo.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        Payments payments=paymentRepo.findById(paymentId).orElseThrow(
                () -> new IllegalArgumentException("Payment not found")
        );if (Roles.ADMIN.equals(users.getRole())){
            payments.setStatus(status.getPaymentStatus());
            return paymentRepo.save(payments);
        } throw new IllegalArgumentException("Error occurred while update");}



    private Payments getPayments(Integer paymentId) {
        return paymentRepo.findById(paymentId).orElseThrow(
                () -> new IllegalArgumentException("Not found")
        );
    }



}