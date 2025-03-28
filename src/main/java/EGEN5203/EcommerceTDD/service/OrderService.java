package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.CreateOrderDto;
import EGEN5203.EcommerceTDD.dto.OrderItemsDto;
import EGEN5203.EcommerceTDD.enums.OrderStatus;
import EGEN5203.EcommerceTDD.enums.PaymentStatus;
import EGEN5203.EcommerceTDD.model.*;
import EGEN5203.EcommerceTDD.repo.OrderRepo;
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
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderRepo orderRepo;

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

    private String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 10000);
    }

    private void validateOrderItems(List<OrderItemsDto> orderItems) {
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

    public List<Order> findOrdersByCustomerId(Long customerId) {
        Users user = userRepo.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("User  not found"));
        return orderRepo.findByUserWithItems(user);
    }

    public List<Order> findOrdersByAdminId(Long adminId) {
        return orderRepo.findOrdersByAdminId(adminId);
    }
}