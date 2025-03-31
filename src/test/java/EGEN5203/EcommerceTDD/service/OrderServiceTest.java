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
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class) class OrderServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private ProductRepo productRepo;
    @Mock
    private OrderRepo orderRepo;
    @Mock
    private PaymentService paymentService;
    @Mock
    private PaymentRepo paymentRepo;
    @Mock
    private OrderItemsrepo orderItemsrepo;

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
    @BeforeEach()
    void setUp() {
        user = new Users();
        user.setUser_id(1L);
        user.setRole(Roles.USER);

        product = new Product();
        product.setProduct_id(1L);
        product.setPrice(10.0);
        product.setQuantity(10);

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

        createOrderDto = new CreateOrderDto();
        createOrderDto.setOrderItems(List.of(orderItemsDto));

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
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepo.save(any(Order.class))).thenReturn(order);
        when(paymentService.processPayment(any(Order.class)))
                .thenReturn(payment);
        Order createdOrder = orderService.createOrder(createOrderDto, 1L);
        //Assert
        assertNotNull(createdOrder);
        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        assertEquals(8, product.getQuantity());
    }
    //Test creating and order for which user does not exist
    @Test
    void createOrder_userNotFound() {
        //Act + Arrange
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        //Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(createOrderDto, 1L));
    }
    //Testing creating an order for which a product doesn't exist
    @Test
    void createOrder_productNotFound() {
        //Act + Arrange
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(productRepo.findById(1L)).thenReturn(Optional.empty());
        //Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(createOrderDto, 1L));
    }
    //Testing where user creates an order but the stock is in sufficient
    @Test
    void createOrder_insufficientStock() {
        //Arrange
        product.setQuantity(1);
        //Act
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        //Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(createOrderDto, 1L));
    }
    //Testing when user places and order but order creation fails
    @Test
    void createOrder_paymentFailed() {
        //Arrange
        payment.setStatus(PaymentStatus.FAILED);
        //Act
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepo.save(any(Order.class))).thenReturn(order);
        when(paymentService.processPayment(any(Order.class))).thenReturn(payment);
        //Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(createOrderDto, 1L));
    }
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
        assertEquals(12, product.getQuantity());
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
        when(orderRepo.findById(1)).thenReturn(Optional.of(order));
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
    when(orderRepo.findById(1)).thenReturn(Optional.empty());
    //Assert
    assertThrows(IllegalArgumentException.class, () -> orderService.fetchOrdersById(1L));
}
//Deleting an order
    @Test
    void deleteOrder() {
      when(orderRepo.findById(1)).thenReturn(
              Optional.of(order)
      );
        doNothing().when(orderRepo).deleteById(1);
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
        when(orderRepo.findById(1)).thenReturn(Optional.of(order));
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
        when(orderRepo.findById(1)).thenReturn(Optional.empty());
        //Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.updateOrderStatus(1, updateOrdersDto));
    }
}