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

/**
 * Test class for PaymentService
 */
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
     * Set up common test data before each test
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
     * Should process payment and return success status
     */
    @Test
    void processPayment() {
        when(paymentRepo.save(any(Payments.class))).thenReturn(payment);

        Payments result = paymentService.processPayment(order);

        assertNotNull(result);
        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
        assertEquals(order.getTotalPrice(), result.getAmount());
    }

    /**
     * Should save card payment with correct details
     */
    @Test
    void processCardPayment_shouldSavePaymentWithCardDetailsAndSuccessStatus() {
        String cardNumber = "1234567890123456";
        String cvv = "123";
        double amount = 50.0;
        payment.setPaymentMethod(String.valueOf(CardType.CREDIT_CARD));
        payment.setCardLastFour("3456");

        when(paymentRepo.save(any(Payments.class))).thenReturn(payment);

        Payments result = paymentService.processCardPayment(cardNumber, cvv, amount);

        assertNotNull(result);
        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
        assertEquals(100, result.getAmount());
        assertEquals("CREDIT_CARD", result.getPaymentMethod());
        assertEquals("3456", result.getCardLastFour());
    }

    /**
     * Should return all payment records
     */
    @Test
    void fetchAllDetails() {
        List<Payments> paymentsList = new ArrayList<>();
        paymentsList.add(payment);

        when(paymentRepo.findAll()).thenReturn(paymentsList);

        List<Payments> result = paymentService.fetchAllDetails();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(payment, result.get(0));
    }

    /**
     * Should return payment for given ID if user is admin
     */
    @Test
    void getPaymentsById() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(adminUser));
        when(paymentRepo.findById(1)).thenReturn(Optional.of(payment));

        Payments result = paymentService.getPaymentsById(1, 1L);

        assertEquals(payment, result);
    }

    /**
     * Should throw exception if user is not admin
     */
    @Test
    void getPaymentsById_withNonAdminUser_shouldThrowException() {
        when(userRepo.findById(2L)).thenReturn(Optional.of(nonAdminUser));

        assertThrows(IllegalArgumentException.class, () -> paymentService.getPaymentsById(1, 2L));
    }

    /**
     * Should throw exception if payment not found
     */
    @Test
    void getPaymentsById_paymentNotFound_shouldThrowException() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(adminUser));
        when(paymentRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> paymentService.getPaymentsById(1, 1L));
    }

    /**
     * Should throw exception if user not found
     */
    @Test
    void getPaymentsById_userNotFound_shouldThrowException() {
        when(userRepo.findById(3L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> paymentService.getPaymentsById(1, 3L));
    }

    /**
     * Should update payment status if user is admin
     */
    @Test
    void updatePaymentStatus() {
        Users users = new Users();
        users.setUser_id(1L);
        users.setRole(Roles.ADMIN);

        Payments payment = new Payments();
        payment.setStatus(PaymentStatus.FAILED);

        when(userRepo.findById(1L)).thenReturn(Optional.of(users));
        when(paymentRepo.findById(1)).thenReturn(Optional.of(payment));
        when(paymentRepo.save(any(Payments.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdatePaymentStatusDto updatePaymentStatusDto = new UpdatePaymentStatusDto();
        updatePaymentStatusDto.setPaymentStatus(PaymentStatus.SUCCESS);

        Payments updatedPayment = paymentService.updatePaymentStatus(1, 1L, updatePaymentStatusDto);

        assertEquals(PaymentStatus.SUCCESS, updatedPayment.getStatus());
    }

    /**
     * Should throw exception if non-admin tries to update payment
     */
    @Test
    void updatePaymentStatus_withNonAdminUser_shouldThrowException() {
        when(userRepo.findById(2L)).thenReturn(Optional.of(nonAdminUser));

        assertThrows(IllegalArgumentException.class, () -> paymentService.updatePaymentStatus(1, 2L, updateStatusDto));
    }

    /**
     * Should throw exception if payment not found while updating
     */
    @Test
    void updatePaymentStatus_paymentNotFound_shouldThrowException() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(adminUser));
        when(paymentRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> paymentService.updatePaymentStatus(1, 1L, updateStatusDto));
    }

    /**
     * Should throw exception if user not found while updating
     */
    @Test
    void updatePaymentStatus_userNotFound_shouldThrowException() {
        when(userRepo.findById(3L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> paymentService.updatePaymentStatus(1, 3L, updateStatusDto));
    }

    /**
     * Should throw exception if user is not admin during update
     */
    @Test
    void shouldThrowExceptionWhenUserIsNotAdmin() {
        Users nonAdminUser = new Users();
        nonAdminUser.setRole(Roles.USER);

        when(userRepo.findById(1L)).thenReturn(Optional.of(nonAdminUser));
        when(paymentRepo.findById(1)).thenReturn(Optional.of(new Payments()));

        assertThrows(IllegalArgumentException.class, () ->
                paymentService.updatePaymentStatus(1, 1L, updateStatusDto)
        );
    }

}