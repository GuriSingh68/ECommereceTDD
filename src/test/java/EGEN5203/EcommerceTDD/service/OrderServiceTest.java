package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.CreateOrderDto;
import EGEN5203.EcommerceTDD.dto.OrderItemsDto;
import EGEN5203.EcommerceTDD.dto.UpdateOrdersDto;
import EGEN5203.EcommerceTDD.enums.OrderStatus;
import EGEN5203.EcommerceTDD.enums.PaymentStatus;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.*;
import EGEN5203.EcommerceTDD.repo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for orders
 */
@ExtendWith(MockitoExtension.class) class OrderServiceTest {
    /**
     * Mocking user repo
     */
    @Mock
    private UserRepo userRepo;
    /**
     * Mocking product repo
     */
    @Mock
    private ProductRepo productRepo;
    /**
     * Mocking Order repo
     */
    @Mock
    private OrderRepo orderRepo;
    /**
     * Mocking Payment service
     */
    @InjectMocks
    private PaymentService paymentService;
    /**
     * Mocking payment repo
     */
    @Mock
    private PaymentRepo paymentRepo;
    /**
     * Mocking Order items repo
     */
    @Mock
    private OrderItemsrepo orderItemsrepo;
    /**
     * Mocking order service
     */
    @InjectMocks
    private OrderService orderService;

    private Users user;
    private Product product;
    private Order order;
    private OrderItem orderItem;
    private CreateOrderDto createOrderDto;
    private OrderItemsDto orderItemsDto;
    private Payments payment;
    private UpdateOrdersDto updateOrdersDto;

    /**
     * setting up dto and models before each test case
     */
    @BeforeEach()
    void setUp() {
        user = new Users();
        user.setUser_id(1L);
        user.setRole(Roles.USER);

        product = new Product();
        product.setProductId(1L);
        product.setPrice(10.0);
        product.setStock(10);

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        orderItem.setItemPrice(product.getPrice());
        orderItem.setTotalPrice(20.0);
        orderItem.setOrder(order);

        order.setOrderItems(List.of(orderItem));

        orderItemsDto = new OrderItemsDto();
        orderItemsDto.setProductId(1L);
        orderItemsDto.setQuantity(2);
        orderItemsDto.setAdmin("testAdmin");

        createOrderDto = new CreateOrderDto();
        createOrderDto.setProducts(List.of(orderItemsDto));

        payment = new Payments();
        payment.setStatus(PaymentStatus.SUCCESS);

        updateOrdersDto = new UpdateOrdersDto();
        updateOrdersDto.setOrderId(1);
        updateOrdersDto.setStatus(OrderStatus.COMPLETED);


    }
    //Test creating a order successfully
    @Test
    void createOrder() {
        //Ararnge + Act
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(productRepo.findByProductId(1L)).thenReturn(product);
        when(orderRepo.save(any(Order.class))).thenReturn(order);
//        when(paymentService.processPayment(any(Order.class)))
  //              .thenReturn(payment);
        Order createdOrder = orderService.createOrder(createOrderDto, 1L);
        //Assert
        assertNotNull(createdOrder);
        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        assertEquals(10, product.getStock());
    }

    /**
     * Testing catching exception of insufficient stock
     */
    @Test
    void createOrderInsufficientStockException() {
        //Ararnge + Act
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(productRepo.findByProductId(1L)).thenReturn(product);
        product.setStock(1);
//        when(orderRepo.save(any(Order.class))).thenReturn(order);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(createOrderDto, 1L);
        });

        assertEquals("Insufficient stock for product: null", exception.getMessage());
    }
    //Test creating and order for which user does not exist
    @Test
    void createOrder_userNotFound() {
        //Act + Arrange
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        //Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(createOrderDto, 1L));
    }

    //Testing where user creates an order but the stock is in sufficient
    //Testing when user places and order but order creation fails
    //Test to get all user orders
    @Test
    void getUserOrders() {
        //Arrange
        when(orderRepo.findByUser(user)).thenReturn(List.of(order));
        //Act
        List<Order> orders = orderService.getUserOrders(user);
        //Assert
        assertNotNull(orders);
        assertEquals(1, orders.size());
    }
    //Successfully fetching order details
    @Test
    void getOrderDetails() {
        //Arrange
        when(orderRepo.findByIdAndUser(1L, user)).thenReturn(order);
        //Act
        Order fetchedOrder = orderService.getOrderDetails(1L, user);
        //Assert
        assertNotNull(fetchedOrder);
        assertEquals(1L, fetchedOrder.getId());
    }
    //Testing cancelling order
    @Test
    void cancelOrder() {
        //Arrange
        when(orderRepo.findByIdAndUser(1L, user)).thenReturn(order);
        when(productRepo.save(any(Product.class))).thenReturn(product);
        when(orderRepo.save(any(Order.class))).thenReturn(order);
        //Act
        Order cancelledOrder = orderService.cancelOrder(1L, user);
        //Assert
        assertNotNull(cancelledOrder);
        assertEquals(OrderStatus.CANCELLED, cancelledOrder.getStatus());
        assertEquals(12, product.getStock());
    }
    //Testing cancelling an order which is pending
    @Test
    void cancelOrder_orderNotPending() {
        //Arrange
        order.setStatus(OrderStatus.COMPLETED);
        //Act
        when(orderRepo.findByIdAndUser(1L, user)).thenReturn(order);
        //Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.cancelOrder(1L, user));
    }
    //Fetching all orders
    @Test
    void getAllOrders() {
        //Act + Arrange
        when(orderRepo.findAllOrdersWithItemsAndProducts()).thenReturn(List.of(order));
        List<Order> orders=orderService.getAllOrders();
        assertNotNull(orders);
        assertEquals(1, orders.size());

    }
    //Testing successfully fetching orders by ID
    @Test
    void fetchOrdersById() {
        //Arrange
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        //Act
        Order fetchedOrder = orderService.fetchOrdersById(1L);
        //Assert
        assertNotNull(fetchedOrder);
        assertEquals(1L, fetchedOrder.getId());
    }
    //Testing fetching order by id for which order doesn't exist
    @Test
    void fetchOrdersById_orderNotFound() {
        //Act + Arrange
        when(orderRepo.findById(1L)).thenReturn(Optional.empty());
        //Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.fetchOrdersById(1L));
    }
    //Deleting an order
    @Test
    void deleteOrder() {
        when(orderRepo.findById(1L)).thenReturn(
                Optional.of(order)
        );
        doNothing().when(orderRepo).deleteById(1L);
        doNothing().when(paymentRepo).deleteById(1);
        Order deletedOrder = orderService.deleteOrder(1L);

        assertNotNull(deletedOrder);
        assertEquals(1L, deletedOrder.getId());

    }
    //Admin successfully changing status order from pending to success
    @Test
    void updateOrderStatus() {
        //Arrange
        user.setRole(Roles.ADMIN);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepo.save(any(Order.class))).thenReturn(order);
        //Act
        Order updatedOrder = orderService.updateOrderStatus(1, updateOrdersDto);
        //Assert
        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.COMPLETED, updatedOrder.getStatus());
    }
    //When a user not admin tries to update or change status of payment
    @Test
    void updateOrderStatus_userNotAdmin() {
        //Act + Arrange
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        //Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.updateOrderStatus(1, updateOrdersDto));
    }
    //Trying to update order status for which order doesn't exist
    @Test
    void updateOrderStatus_orderNotFound() {
        //Act + Arrange
        user.setRole(Roles.ADMIN);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepo.findById(1L)).thenReturn(Optional.empty());
        //Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.updateOrderStatus(1, updateOrdersDto));
    }
    @Test
    void findOrdersByCustomerId_userFound_returnsOrders() {
        // Arrange
        List<Order> customerOrders = List.of(order);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepo.findByUserWithItems(user)).thenReturn(customerOrders);

        // Act
        List<Order> foundOrders = orderService.findOrdersByCustomerId(1L);

        // Assert
        assertNotNull(foundOrders);
        assertEquals(1, foundOrders.size());
        assertEquals(customerOrders, foundOrders);

    }

    /**
     * testing catching exception for id not found
     */
    @Test
    void findOrdersByCustomerId_userNotFound_throwsException() {
        // Arrange
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                orderService.findOrdersByCustomerId(1L)
        );
        assertEquals("User  not found", exception.getMessage());

    }

    /**
     * Testing order returning an empty list
     */
    @Test
    void findOrdersByCustomerId_userFound_noOrders_returnsEmptyList() {
        // Arrange
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepo.findByUserWithItems(user)).thenReturn(Collections.emptyList());

        // Act
        List<Order> foundOrders = orderService.findOrdersByCustomerId(1L);

        // Assert
        assertNotNull(foundOrders);
        assertTrue(foundOrders.isEmpty());

    }

    /**
     * Testing admin user not found
     */
    @Test
    void findOrdersByAdminId_adminFound_returnsOrders() {
        // Arrange
        List<Order> adminOrders = List.of(order);
        when(orderRepo.findOrdersByAdminId(2L)).thenReturn(adminOrders);

        // Act
        List<Order> foundOrders = orderService.findOrdersByAdminId(2L);

        // Assert
        assertNotNull(foundOrders);
        assertEquals(1, foundOrders.size());
        assertEquals(adminOrders, foundOrders);

    }

    /**
     * Testing id not found
     */
    @Test
    void findOrdersByAdminId_adminFound_noOrders_returnsEmptyList() {
        // Arrange
        when(orderRepo.findOrdersByAdminId(2L)).thenReturn(Collections.emptyList());

        // Act
        List<Order> foundOrders = orderService.findOrdersByAdminId(2L);

        // Assert
        assertNotNull(foundOrders);
        assertTrue(foundOrders.isEmpty());

    }

    /**
     * Testing returning an exception for throwing null order items
     */
    @Test
    void shouldThrowExceptionWhenOrderItemsIsNull() {
        
        assertThrows(IllegalArgumentException.class, () ->
                orderService.validateOrderItems(null));
    }

    /**
     * Testing throwing when order items is empty
     */
    @Test
    void shouldThrowExceptionWhenOrderItemsIsEmpty() {
        // Not Covered Line: if (orderItems.isEmpty())
        assertThrows(IllegalArgumentException.class, () ->
                orderService.validateOrderItems(new ArrayList<>()));
    }

    @Test
    void shouldThrowExceptionWhenProductIdIsNull() {
        // Test case: Product ID is null
        // Expected: IllegalArgumentException
        // Line covered: if (item.getProductId() == null)
        OrderItemsDto item = new OrderItemsDto();
        item.setQuantity(1);
        item.setAdmin("admin123");
        List<OrderItemsDto> items = List.of(item);

        assertThrows(IllegalArgumentException.class, () ->
                orderService.validateOrderItems(items));
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNull() {
        // Test case: Quantity is null
        // Expected: IllegalArgumentException
        // Line covered: if (item.getQuantity() == null)
        OrderItemsDto item = new OrderItemsDto();
        item.setProductId(10L);
        item.setAdmin("admin123");
        List<OrderItemsDto> items = List.of(item);

        assertThrows(IllegalArgumentException.class, () ->
                orderService.validateOrderItems(items));
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsZero() {
        // Test case: Quantity is zero
        // Expected: IllegalArgumentException
        // Line covered: if (item.getQuantity() <= 0)
        OrderItemsDto item = new OrderItemsDto();
        item.setProductId(10L);
        item.setQuantity(0);
        item.setAdmin("admin123");
        List<OrderItemsDto> items = List.of(item);

        assertThrows(IllegalArgumentException.class, () ->
                orderService.validateOrderItems(items));
    }

    @Test
    void shouldThrowExceptionWhenAdminIsNull() {
        // Test case: Admin is null
        // Expected: IllegalArgumentException
        // Line covered: if (item.getAdmin() == null)
        OrderItemsDto item = new OrderItemsDto();
        item.setProductId(10L);
        item.setQuantity(1);
        item.setAdmin(null);
        List<OrderItemsDto> items = List.of(item);

        assertThrows(IllegalArgumentException.class, () ->
                orderService.validateOrderItems(items));
    }

    @Test
    void shouldThrowExceptionWhenAdminIsEmpty() {
        // Test case: Admin is an empty string
        // Expected: IllegalArgumentException
        // Line covered: if (item.getAdmin().isEmpty())
        OrderItemsDto item = new OrderItemsDto();
        item.setProductId(10L);
        item.setQuantity(1);
        item.setAdmin("");
        List<OrderItemsDto> items = List.of(item);

        assertThrows(IllegalArgumentException.class, () ->
                orderService.validateOrderItems(items));
    }

    @Test
    void shouldPassForValidOrderItem() {
        // Happy path: All fields are valid
        // Expected: No exception thrown
        // All lines in validateOrderItems method are covered
        OrderItemsDto item = new OrderItemsDto();
        item.setProductId(10L);
        item.setQuantity(2);
        item.setAdmin("admin123");
        List<OrderItemsDto> items = List.of(item);

        assertDoesNotThrow(() ->
                orderService.validateOrderItems(items));
    }

}