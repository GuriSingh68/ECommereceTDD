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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
        try {
            validateOrderItems(createOrderDto.getOrderItems());

            Users user = userRepo.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            validateStockAvailability(createOrderDto.getOrderItems());

            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(OrderStatus.PENDING);

            int randomDays = 3 + new Random().nextInt(5); // Random between 3-7 days
            order.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(randomDays));

            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemRequestDto itemDto : createOrderDto.getOrderItems()) {
                Product product = productRepo.findById(itemDto.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Product not found with ID: " + itemDto.getProductId()));

                product.setStock(product.getStock() - itemDto.getQuantity());
                productRepo.save(product);

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(itemDto.getQuantity());
                orderItem.setItemPrice(product.getPrice());
                orderItem.setTotalPrice(product.getPrice() * itemDto.getQuantity());
                orderItem.setOrder(order);
                orderItems.add(orderItem);
            }

            order.setOrderItems(orderItems);
            order.setTotalPrice(calculateTotalPrice(orderItems));

            Order savedOrder = orderRepo.save(order);

            // Process payment and set the bidirectional relationship
            Payments payment = paymentService.processPayment(savedOrder, createOrderDto.getPaymentMethod(), createOrderDto.getPaymentStatus());
            payment.setOrder(savedOrder);  // Set the order reference in payment
            System.out.println(payment);
            savedOrder.setPayments(payment);  // Set the payment reference in order

            if (payment.getStatus() == PaymentStatus.COMPLETED) {
                savedOrder.setStatus(OrderStatus.COMPLETED);
                return orderRepo.save(savedOrder);
            } else if (payment.getStatus() == PaymentStatus.PENDING && payment.getPaymentMethod().equals("cashondelivery")) {
                savedOrder.setStatus(OrderStatus.COMPLETED);
                return orderRepo.save(savedOrder);
            } else {
                restoreProductStock(savedOrder);
                savedOrder.setStatus(OrderStatus.CANCELLED);
                orderRepo.save(savedOrder);
                throw new PaymentException("Payment processing failed. Order cancelled and stock restored.");
            }
        } catch (Exception e) {
            System.err.println("Order creation failed: " + e);
            e.printStackTrace();
            throw new IllegalArgumentException("Order creation failed: " +
                    (e.getMessage() != null ? e.getMessage() : "Please check your order details and try again"));
        }
    }
    private List<OrderItem> processOrderItems(List<OrderItemRequestDto> orderItems, Order order) {
        return orderItems.stream().map(item -> {
            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product not found with ID: " + item.getProductId()));

            // Deduct stock
            product.setStock(product.getStock() - item.getQuantity());
            productRepo.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setItemPrice(product.getPrice());
            orderItem.setTotalPrice(product.getPrice() * item.getQuantity());
            orderItem.setOrder(order);
            return orderItem;
        }).toList();
    }

    private void validateStockAvailability(List<OrderItemRequestDto> orderItems) {
        for (OrderItemRequestDto item : orderItems) {
            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product not found with ID: " + item.getProductId()));

            if (product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException(
                        String.format("Insufficient stock for product %s (ID: %d). Available: %d, Requested: %d",
                                product.getName(), product.getProductId(), product.getStock(), item.getQuantity()));
            }
        }
    }

    private void restoreProductStock(Order order) {
        if (order.getOrderItems() != null) {
            order.getOrderItems().forEach(item -> {
                if (item.getProduct() != null) {
                    Product product = item.getProduct();
                    product.setStock(product.getStock() + item.getQuantity());
                    productRepo.save(product);
                }
            });
        }
    }

    private Double calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
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

class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
