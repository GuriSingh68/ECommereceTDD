package EGEN5203.EcommerceTDD.stepDefinitions;

import EGEN5203.EcommerceTDD.dto.UpdatePaymentStatusDto;
import EGEN5203.EcommerceTDD.enums.PaymentStatus;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Order;
import EGEN5203.EcommerceTDD.model.Payments;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.PaymentRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import EGEN5203.EcommerceTDD.service.PaymentService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PaymentStepDefinitions {

    @Mock
    private PaymentRepo paymentRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private PaymentService paymentService;

    private Order testOrder;
    private Payments testPayment;
    private Users adminUser;
    private Users regularUser;
    private Exception thrownException;
    private List<Payments> allPayments;
    private UpdatePaymentStatusDto updateStatusDto;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("the payment system is available")
    public void thePaymentSystemIsAvailable() {
        // Nothing to do here, just a precondition
    }

    @Given("a user with ID {string} and role {string} exists")
    public void aUserWithIdAndRoleExists(String userId, String role) {
        Users user = new Users();
        user.setUser_id(Long.parseLong(userId));
        user.setRole(Roles.valueOf(role));

        when(userRepo.findById(Long.parseLong(userId))).thenReturn(Optional.of(user));

        if (role.equals("ADMIN")) {
            adminUser = user;
        } else {
            regularUser = user;
        }
    }

    @Given("a valid order with ID {string} and total price {string}")
    public void aValidOrderWithDetails(String orderId, String totalPrice) {
        testOrder = new Order();
        testOrder.setId((long) Integer.parseInt(orderId));
        testOrder.setTotalPrice(Double.parseDouble(totalPrice));
    }

    @When("the payment is processed for the order")
    public void thePaymentIsProcessedForTheOrder() {
        testPayment = new Payments();
        testPayment.setOrder(testOrder);
        testPayment.setAmount(testOrder.getTotalPrice());
        testPayment.setPaymentDate(LocalDateTime.now());
        testPayment.setStatus(PaymentStatus.SUCCESS);
        when(paymentRepo.save(any(Payments.class))).thenReturn(testPayment);

        testPayment = paymentService.processPayment(testOrder);
    }

    @When("the payment is processed for a null order")
    public void thePaymentIsProcessedForANullOrder() {
        try {
            paymentService.processPayment(null);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("a payment record should be created with status {string}")
    public void aPaymentRecordShouldBeCreatedWithStatus(String status) {
        assertNotNull(testPayment);
        assertEquals(PaymentStatus.valueOf(status), testPayment.getStatus());
        verify(paymentRepo, times(1)).save(any(Payments.class));
    }

    @Then("the payment amount should be {string}")
    public void thePaymentAmountShouldBe(String amount) {
        assertEquals(Double.parseDouble(amount), testPayment.getAmount(), 0.001);
    }

    @Then("a NullPointerException should be thrown with message {string}")
    public void aNullPointerExceptionShouldBeThrown(String message) {
        assertNotNull(thrownException);
        assertTrue(thrownException instanceof NullPointerException);
        assertEquals(message, thrownException.getMessage());
    }

    @Given("a credit card with number {string} and CVV {string}")
    public void aCreditCardWithDetails(String cardNumber, String cvv) {
        // Store credit card details for later use
        testPayment = new Payments();
        testPayment.setCardLastFour(cardNumber.substring(cardNumber.length() - 4));
        testPayment.setPaymentMethod("CREDIT_CARD");
        testPayment.setStatus(PaymentStatus.SUCCESS);

        when(paymentRepo.save(any(Payments.class))).thenReturn(testPayment);
    }

    @When("a payment of {string} is processed using the credit card")
    public void aPaymentIsProcessedUsingTheCreditCard(String amount) {
        testPayment.setAmount(Double.parseDouble(amount));
        testPayment.setPaymentDate(LocalDateTime.now());
        testPayment = paymentService.processCardPayment("4111111111111111", "123", Double.parseDouble(amount));
    }

    @Then("the payment should have the last four digits {string}")
    public void thePaymentShouldHaveTheLastFourDigits(String lastFour) {
        assertEquals(lastFour, testPayment.getCardLastFour());
    }

    @Then("the payment method should be {string}")
    public void thePaymentMethodShouldBe(String method) {
        assertEquals(method, testPayment.getPaymentMethod());
    }

    @Given("multiple payments exist in the system")
    public void multiplePaymentsExistInTheSystem() {
        Payments payment1 = new Payments();
        payment1.setId(1L);
        payment1.setStatus(PaymentStatus.SUCCESS);

        Payments payment2 = new Payments();
        payment2.setId(2L);
        payment2.setStatus(PaymentStatus.SUCCESS);

        allPayments = Arrays.asList(payment1, payment2);

        when(paymentRepo.findAll()).thenReturn(allPayments);
    }

    @When("all payment details are requested")
    public void allPaymentDetailsAreRequested() {
        allPayments = paymentService.fetchAllDetails();
    }

    @Then("all existing payment records should be returned")
    public void allExistingPaymentRecordsShouldBeReturned() {
        assertNotNull(allPayments);
        assertEquals(2, allPayments.size());
        verify(paymentRepo, times(1)).findAll();
    }

    @Given("a payment with ID {string} exists in the system")
    public void aPaymentWithIdExistsInTheSystem(String paymentId) {
        testPayment = new Payments();
        testPayment.setId((long) Integer.parseInt(paymentId));
        testPayment.setStatus(PaymentStatus.SUCCESS);

        when(paymentRepo.findById(Integer.parseInt(paymentId))).thenReturn(Optional.of(testPayment));
    }

    @When("an admin with ID {string} requests the payment with ID {string}")
    public void anAdminRequestsThePaymentWithId(String userId, String paymentId) {
        try {
            testPayment = paymentService.getPaymentsById(Integer.parseInt(paymentId), Long.parseLong(userId));
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("the payment details should be returned")
    public void thePaymentDetailsShouldBeReturned() {
        assertNotNull(testPayment);
        assertNull(thrownException);
    }

    @When("a regular user with ID {string} requests the payment with ID {string}")
    public void aRegularUserRequestsThePaymentWithId(String userId, String paymentId) {
        try {
            paymentService.getPaymentsById(Integer.parseInt(paymentId), Long.parseLong(userId));
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("an IllegalArgumentException should be thrown with message {string}")
    public void anIllegalArgumentExceptionShouldBeThrown(String message) {
        assertNotNull(thrownException);
        assertTrue(thrownException instanceof IllegalArgumentException);
        assertEquals(message, thrownException.getMessage());
    }

    @When("an admin with ID {string} updates the payment status to {string}")
    public void anAdminUpdatesThePaymentStatus(String userId, String status) {
        updateStatusDto = new UpdatePaymentStatusDto();
        updateStatusDto.setPaymentStatus(PaymentStatus.valueOf(status));

        when(paymentRepo.save(any(Payments.class))).thenReturn(testPayment);

        try {
            testPayment = paymentService.updatePaymentStatus(Math.toIntExact(testPayment.getId()), Long.parseLong(userId), updateStatusDto);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("the payment status should be updated to {string}")
    public void thePaymentStatusShouldBeUpdatedTo(String status) {
        assertNotNull(testPayment);
        assertEquals(PaymentStatus.valueOf(status), testPayment.getStatus());
        verify(paymentRepo, times(1)).save(any(Payments.class));
    }

    @When("a regular user with ID {string} attempts to update the payment status to {string}")
    public void aRegularUserAttemptsToUpdateThePaymentStatus(String userId, String status) {
        updateStatusDto = new UpdatePaymentStatusDto();
        updateStatusDto.setPaymentStatus(PaymentStatus.valueOf(status));

        try {
            paymentService.updatePaymentStatus(Math.toIntExact(testPayment.getId()), Long.parseLong(userId), updateStatusDto);
        } catch (Exception e) {
            thrownException = e;
        }
    }
}