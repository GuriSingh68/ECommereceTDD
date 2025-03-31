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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private PaymentRepo paymentRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private PaymentService paymentService;

    private Order order;
    private Payments payment;
    private Users adminUser;
    private Users nonAdminUser;
    private UpdatePaymentStatusDto updateStatusDto;

    /**
     * Creating dto and models before testing each method
     */
    @BeforeEach
    void setUp() {
        order = new Order();
        order.setTotalPrice(100.0);

        payment = new Payments();
        payment.setId(1L);
        payment.setOrder(order);
        payment.setAmount(100.0);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.SUCCESS);

        adminUser = new Users();
        adminUser.setUser_id(1L);
        adminUser.setRole(Roles.ADMIN);

        nonAdminUser = new Users();
        nonAdminUser.setUser_id(2L);
        nonAdminUser.setRole(Roles.USER);

        updateStatusDto = new UpdatePaymentStatusDto();
        updateStatusDto.setPaymentStatus(PaymentStatus.FAILED);
    }

    /**
     * Successfully testing a payment process
     */
    @Test
    void processPayment() {
        //Arrange
        when(paymentRepo.save(any(Payments.class))).thenReturn(payment);
        //Act
        Payments result = paymentService.processPayment(order);
        //Assert
        assertNotNull(result);
        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
        assertEquals(order.getTotalPrice(), result.getAmount());
    }

    /**
     * Testing payment with all details
     */
    @Test
    void processCardPayment_shouldSavePaymentWithCardDetailsAndSuccessStatus() {
        //Arrange
        String cardNumber = "1234567890123456";
        String cvv = "123";
        double amount = 50.0;
        payment.setPaymentMethod(String.valueOf(CarDType.CREDIT_CARD));
        payment.setCardLastFour("3456");
        when(paymentRepo.save(any(Payments.class))).thenReturn(payment);
        //Act
        Payments result = paymentService.processCardPayment(cardNumber, cvv, amount);
        //Assert
        assertNotNull(result);
        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
        assertEquals(100, result.getAmount());
        assertEquals("CREDIT_CARD", result.getPaymentMethod());
        assertEquals("3456", result.getCardLastFour());
    }

    /**
     * Fetching all details of payments
     */
    @Test
    void fetchAllDetails() {
        //Arrange
        List<Payments> paymentsList = new ArrayList<>();
        paymentsList.add(payment);
        //Act
        when(paymentRepo.findAll()).thenReturn(paymentsList);

        List<Payments> result = paymentService.fetchAllDetails();
        //Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(payment, result.get(0));
    }

    /**
     * Fetching payment detail of user by ID
     */
    @Test
    void getPaymentsById() {
        //Act + Arrange
        when(userRepo.findById(1L)).thenReturn(Optional.of(adminUser));
        when(paymentRepo.findById(1)).thenReturn(Optional.of(payment));

        Payments result = paymentService.getPaymentsById(1, 1L);
        //Assert
        assertEquals(payment, result);
    }

    /**
     * Non - admin user tries to fetch user payment data
     */
    @Test
    void getPaymentsById_withNonAdminUser_shouldThrowException() {
        //Act + arrange
        when(userRepo.findById(2L)).thenReturn(Optional.of(nonAdminUser));
        //Assert
        assertThrows(IllegalArgumentException.class, () -> paymentService.getPaymentsById(1, 2L));
    }

    /**
     * Fetching payment details with invalid ID
     */
    @Test
    void getPaymentsById_paymentNotFound_shouldThrowException() {
        //Act + Arrange
        when(userRepo.findById(1L)).thenReturn(Optional.of(adminUser));
        when(paymentRepo.findById(1)).thenReturn(Optional.empty());
        //Assert
        assertThrows(IllegalArgumentException.class, () -> paymentService.getPaymentsById(1, 1L));
    }

    /**
     * User not found
     */
    @Test
    void getPaymentsById_userNotFound_shouldThrowException() {
        when(userRepo.findById(3L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> paymentService.getPaymentsById(1, 3L));
    }

    /**
     * Admin updating payment status
     */
    @Test
    void updatePaymentStatus() {
        Users users = new Users();
        users.setUser_id(1L);
        users.setRole(Roles.ADMIN);

        Payments payment = new Payments();
        payment.setStatus(PaymentStatus.FAILED);  // Initial status

        when(userRepo.findById(1L)).thenReturn(Optional.of(users));
        when(paymentRepo.findById(1)).thenReturn(Optional.of(payment));

        UpdatePaymentStatusDto updatePaymentStatusDto = new UpdatePaymentStatusDto();
        updatePaymentStatusDto.setPaymentStatus(PaymentStatus.SUCCESS);

        when(paymentRepo.save(any(Payments.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payments updatedPayment = paymentService.updatePaymentStatus(1, 1L, updatePaymentStatusDto);

        assertEquals(PaymentStatus.SUCCESS, updatedPayment.getStatus());
    }
    /**
     * When non admin user updates payment status
     */
    @Test
    void updatePaymentStatus_withNonAdminUser_shouldThrowException() {
        when(userRepo.findById(2L)).thenReturn(Optional.of(nonAdminUser));

        assertThrows(IllegalArgumentException.class, () -> paymentService.updatePaymentStatus(1, 2L, updateStatusDto));
    }

    /**
     * When payment is not found while updating
     */
    @Test
    void updatePaymentStatus_paymentNotFound_shouldThrowException() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(adminUser));
        when(paymentRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> paymentService.updatePaymentStatus(1, 1L, updateStatusDto));
    }

    /**
     * User id which is passed for updating user
     */

    @Test
    void updatePaymentStatus_userNotFound_shouldThrowException() {
        when(userRepo.findById(3L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> paymentService.updatePaymentStatus(1, 3L, updateStatusDto));
    }

}
