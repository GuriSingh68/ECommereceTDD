package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.CreateOrderDto;
import EGEN5203.EcommerceTDD.dto.OrderItemRequestDto;
import EGEN5203.EcommerceTDD.dto.OrderItemsDto;
import EGEN5203.EcommerceTDD.dto.UpdateOrdersDto;
import EGEN5203.EcommerceTDD.enums.OrderStatus;
import EGEN5203.EcommerceTDD.enums.PaymentStatus;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.*;
import EGEN5203.EcommerceTDD.repo.OrderRepo;
import EGEN5203.EcommerceTDD.repo.PaymentRepo;
import EGEN5203.EcommerceTDD.repo.ProductRepo;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
@Autowired
    private UserRepo userRepo;
@Autowired
    private ProductRepo productRepo;
@Autowired
    private OrderRepo orderRepo;
@Autowired
    private PaymentService paymentService;
@Autowired
    private PaymentRepo paymentRepo;
    @Transactional
    public Order createOrder(CreateOrderDto createOrderDto, Long userId) {
        validateOrderItems(createOrderDto.getOrderItems());

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = createOrderDto.getOrderItems().stream().map(
                orderItemRequest -> {
                    Product product = productRepo.findById(orderItemRequest.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product);
                    orderItem.setQuantity(orderItemRequest.getQuantity());
                    orderItem.setItemPrice(product.getPrice());
                    orderItem.setTotalPrice(product.getPrice() * orderItemRequest.getQuantity());
                    orderItem.setOrder(order);
                    return orderItem;
                }
        ).toList();

        Double totalPrice = orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();

        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepo.save(order);
        Payments payment = paymentService.processPayment(savedOrder);

        if (payment.getStatus().equals(PaymentStatus.SUCCESS)) {
            order.setStatus(OrderStatus.COMPLETED);
            return savedOrder;
        }

        order.setStatus(OrderStatus.CANCELLED);
        throw new IllegalArgumentException("Checkout failed...retry again");
    }
private void validateOrderItems(List<OrderItemRequestDto> orderItems) {
    if (orderItems == null || orderItems.isEmpty()) {
        throw new IllegalArgumentException("Order must contain at least one item");
    }
}

    public List<Order> getUserOrdersById(Long userId) {
        return orderRepo.findOrdersByUserWithItemsAndProducts(userId);
    }

    public List<Order> getUserOrders(Users user) {
        return orderRepo.findByUser(user);
    }
    public Order getOrderDetails(Long orderId, Users user) {
        return orderRepo.findByIdAndUser(orderId, user);
    }
    @Transactional
    public Order cancelOrder(Long orderId, Users user) {
        Order order = getOrderDetails(orderId, user);

        // Only allow cancellation of pending orders
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot cancel processed order");
        }

        // Restore product inventory
        order.getOrderItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepo.save(product);
        });

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepo.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public List<Order> fetchOrdersById(Long userId) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User  not found"));
        return orderRepo.findByUser (user);
    }

    public Order deleteOrder(Long orderId) {
         paymentRepo.deleteById(Math.toIntExact(orderId));
        Order order=orderRepo.findById(Math.toIntExact(orderId))
                .orElseThrow(() -> new  IllegalArgumentException("Order not found"));

        orderRepo.deleteById(Math.toIntExact(orderId));
        return order;
    }

    public Order updateOrderStatus(Integer userId, UpdateOrdersDto orderStatus) {
        Users user=userRepo.findById(Long.valueOf(userId)).
                orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getRole().equals(Roles.ADMIN)){
            Order order=orderRepo.findById(orderStatus.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));
            order.setStatus(orderStatus.getStatus());
           return orderRepo.save(order);
        }
        throw new IllegalArgumentException("You are not authorised to update order status");
    }
}
