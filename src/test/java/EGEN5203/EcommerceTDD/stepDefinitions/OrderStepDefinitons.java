package EGEN5203.EcommerceTDD.stepDefinitions;

import EGEN5203.EcommerceTDD.dto.CreateOrderDto;
import EGEN5203.EcommerceTDD.dto.OrderItemsDto;
import EGEN5203.EcommerceTDD.enums.OrderStatus;
import EGEN5203.EcommerceTDD.enums.PaymentStatus;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Order;
import EGEN5203.EcommerceTDD.model.Payments;
import EGEN5203.EcommerceTDD.model.Product;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.OrderRepo;
import EGEN5203.EcommerceTDD.repo.ProductRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import EGEN5203.EcommerceTDD.service.OrderService;
import EGEN5203.EcommerceTDD.service.PaymentService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import io.cucumber.java.en.Then;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderStepDefinitons {
        @InjectMocks
         OrderService orderService;
        @Mock
        PaymentService paymentService;
        @Mock
        UserRepo userRepo;
        @Mock
        ProductRepo productRepo;
        @Mock
        OrderRepo orderRepo;

        private Order createdOrder;
        private Exception exception;
        private Users user;
        private List<Product> products;
        private Payments payment;
        private CreateOrderDto createOrderDto;
    public OrderStepDefinitons() {
        MockitoAnnotations.openMocks(this);
    }
        @Given("a user with ID {long}")
        public void aUserWithId(long userId) {
            user = new Users();
            user.setUser_id(userId);
            when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        }

        @Given("products with sufficient stock")
        public void productsWithSufficientStock() {
            products = new ArrayList<>();
            Product product1 = new Product();
            product1.setProduct_id(1L);
            product1.setQuantity(10);
            product1.setPrice(10.0);
            Product product2 = new Product();
            product2.setProduct_id(2L);
            product2.setQuantity(5);
            product2.setPrice(20.0);
            products.add(product1);
            products.add(product2);
            when(productRepo.findById(1L)).thenReturn(Optional.of(product1));
            when(productRepo.findById(2L)).thenReturn(Optional.of(product2));
        }

    @Given("products with insufficient stock")
    public void productsWithInsufficientStock() {
        Product product = new Product();
        product.setProduct_id(1L);
        product.setQuantity(1);
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));

        // You added this mock
        payment = new Payments();
        payment.setStatus(PaymentStatus.FAILED);
        when(paymentService.processPayment(any(Order.class))).thenReturn(payment);
    }

    @Given("the payment is successful")
    public void thePaymentIsSuccessful() {
        payment = new Payments();
        payment.setStatus(PaymentStatus.SUCCESS);
        when(paymentService.processPayment(any(Order.class))).thenReturn(payment);
    }

        @Given("the payment is failed")
        public void thePaymentIsFailed() {
            payment = new Payments();
            payment.setStatus(PaymentStatus.FAILED);
            when(paymentService.processPayment(any(Order.class))).thenReturn(payment);
        }

        @Given("a user with ID {long} does not exist")
        public void aUserWithIdDoesNotExist(long userId) {
            when(userRepo.findById(userId)).thenReturn(Optional.empty());
        }

        @Given("a product with ID {long} does not exist")
        public void aProductWithIdDoesNotExist(long productId) {
            when(productRepo.findById(productId)).thenReturn(Optional.empty());
        }

        @When("I create an order with items")
        public void iCreateAnOrderWithItems(DataTable dataTable) {
            List<OrderItemsDto> orderItemsDtos = new ArrayList<>();
            dataTable.asMaps().forEach(row -> {
                OrderItemsDto dto = new OrderItemsDto();
                dto.setProductId(Long.parseLong(row.get("productId")));
                dto.setQuantity(Integer.parseInt(row.get("quantity")));
                orderItemsDtos.add(dto);
            });
            createOrderDto = new CreateOrderDto();
            createOrderDto.setOrderItems(orderItemsDtos);
            try {
                createdOrder = orderService.createOrder(createOrderDto, user.getUser_id());
            } catch (Exception e) {
                exception = e;
            }
        }

        @When("I create an order with empty items")
        public void iCreateAnOrderWithEmptyItems() {
            createOrderDto = new CreateOrderDto();
            createOrderDto.setOrderItems(new ArrayList<>());
            try {
                createdOrder = orderService.createOrder(createOrderDto, user.getUser_id());
            } catch (Exception e) {
                exception = e;
            }
        }

        @Then("the order should be created with status {string}")
        public void theOrderShouldBeCreatedWithStatus(String status) {
            Assertions.assertEquals(OrderStatus.valueOf(status), OrderStatus.COMPLETED);
        }

        @Then("the product quantities should be updated")
        public void theProductQuantitiesShouldBeUpdated() {
            Assertions.assertEquals(8, products.get(0).getQuantity());
            Assertions.assertEquals(4, products.get(1).getQuantity());
        }

        @Then("I should receive an {string} error")
        public void iShouldReceiveAnError(String errorMessage) {
            Assertions.assertNotNull(exception);
            Assertions.assertEquals(errorMessage, "Checkout failed...retry again");
        }
    @Then("I should receive a {string} error")
    public void iShouldReceiveAnErrorProductNotFound(String errorMessage) {
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(errorMessage, "Product not found");
    }
    @Then("I should receive a stock error {string} error")
    public void iShouldReceiveAnErrorStockError(String errorMessage) {
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(errorMessage, "Insufficient Stock");
    }
    @Then("the order should be created with status a {string}")
    public void theOrderShouldBeCreatedWithStatusCancelled(String status) {
        Assertions.assertEquals(OrderStatus.valueOf(status), OrderStatus.CANCELLED);
    }

    }

