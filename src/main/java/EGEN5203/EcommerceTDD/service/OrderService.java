package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.CreateOrderDto;
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

@Service
public class OrderService {
    /**
     * User repository to connect with database
     */
    @Autowired
    private UserRepo userRepo;
    /**
     * Product repository to connect with database
     */
    @Autowired
    private ProductRepo productRepo;
    /**
     * Order repository to connect with database
     */
    @Autowired
    private OrderRepo orderRepo;
    /**
     * Payment repository to connect with database
     */
    @Autowired
        private PaymentRepo paymentRepo;

    /**
     * Logic for creating order
     * @param createOrderDto
     * @param userId
     * @return
     */
    @Transactional
    public Order createOrder(CreateOrderDto createOrderDto, Long userId) {
        // Validate user first
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User  not found"));

        // Validate order items
        validateOrderItems(createOrderDto.getProducts());

        Order order = new Order();
        order.setUser (user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setTransactionId(generateTransactionId());

        List<OrderItem> orderItems = createOrderDto.getProducts().stream().map(orderItemsDto -> {
            Product product = productRepo.findByProductId(orderItemsDto.getProductId());
//                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + orderItemsDto.getProductId()));

            // Validate stock
            if (product.getStock() < orderItemsDto.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemsDto.getQuantity());
            orderItem.setItemPrice(product.getPrice());
            orderItem.setTotalPrice(product.getPrice() * orderItemsDto.getQuantity());
            orderItem.setOrder(order);
            return orderItem;
        }).toList();

        Double totalPrice = orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();

        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        return orderRepo.save(order);
    }

    /**
     * Logic to generate a transaction ID
     * @return
     */
    private String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 10000);
    }

    /**
     * Logic to create order items
     * @param orderItems
     */
    void validateOrderItems(List<OrderItemsDto> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product");
        }

        for (int i = 0; i < orderItems.size(); i++) {
            OrderItemsDto item = orderItems.get(i);
            if (item.getProductId() == null) {
                throw new IllegalArgumentException(
                        String.format("Missing productId for item at position %d", i + 1));
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException(
                        String.format("Invalid quantity for productId %d. Must be positive number",
                                item.getProductId()));
            }
            if (item.getAdmin() == null || item.getAdmin().isEmpty()) {
                throw new IllegalArgumentException(
                        String.format("Missing admin/farmer reference for productId %d",
                                item.getProductId()));
            }
        }
    }

    /**
     * List order items by customer id
     * @param customerId
     * @return
     */
    public List<Order> findOrdersByCustomerId(Long customerId) {
        Users user = userRepo.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("User  not found"));
        return orderRepo.findByUserWithItems(user);
    }

    /**
     * List All orders for admin
     * @param adminId
     * @return
     */
    public List<Order> findOrdersByAdminId(Long adminId) {
        return orderRepo.findOrdersByAdminId(adminId);
    }

    /**
     * Delete any order by id
     * @param orderId
     * @return
     */
    public Order deleteOrder(Long orderId) {
        paymentRepo.deleteById(Math.toIntExact(orderId));
        Order order=orderRepo.findById((long) Math.toIntExact(orderId))
                .orElseThrow(() -> new  IllegalArgumentException("Order not found"));

        orderRepo.deleteById(orderId);
        return order;
    }

    /**
     * Get list of all orders
     * @param user
     * @return
     */
    public List<Order> getUserOrders(Users user) {
        return orderRepo.findByUser(user);
    }

    /**
     * Get all order details by Id and user de
     * @param orderId
     * @param user
     * @return
     */
    public Order getOrderDetails(Long orderId, Users user) {
        return orderRepo.findByIdAndUser(orderId, user);
    }

    /**
     * Change status of order to cacel or pending by user
     * @param orderId
     * @param user
     * @return
     */
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

    /**
     * Update order status of processed orders by user
     * @param userId
     * @param orderStatus
     * @return
     */
    public Order updateOrderStatus(Integer userId, UpdateOrdersDto orderStatus) {
        Users user=userRepo.findById(Long.valueOf(userId)).
                orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getRole().equals(Roles.ADMIN)){
            Order order=orderRepo.findById(Long.valueOf(orderStatus.getOrderId()))
                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));
            order.setStatus(orderStatus.getStatus());
            return orderRepo.save(order);
        }
        throw new IllegalArgumentException("You are not authorised to update order status");
    }

    /**
     * List all orders
     * @return
     */
    public List<Order> getAllOrders() {
        return orderRepo.findAllOrdersWithItemsAndProducts();
    }

    /**
     * List orders by ID
     * @param id
     * @return
     */
    public Order fetchOrdersById(Long id) {
        Order order=orderRepo.findById(id)
                .orElseThrow(() ->
                        new  IllegalArgumentException("Order not found")
                );
        return order;
    }
}