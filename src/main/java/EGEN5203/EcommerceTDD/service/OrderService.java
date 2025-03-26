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
@Transactional
public Order createOrder(CreateOrderDto createOrderDto, Long userId) {
    // Validate order items
    validateOrderItems(createOrderDto.getOrderItems());

    Users users = userRepo.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    Order order = new Order();
    order.setUser(users);
    order.setOrderDate(LocalDateTime.now());
    order.setStatus(OrderStatus.PENDING);

    // Listing order items
    List<OrderItem> orderItems = createOrderDto.getOrderItems().stream().map(
            orderItemsDto -> {
                Product product = productRepo.findById(orderItemsDto.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Product not found"));

                // Validate product availability
                if (product.getQuantity() < orderItemsDto.getQuantity()) {
                    throw new IllegalArgumentException("Insufficient Stock");
                }

                // Update product quantity
                product.setQuantity(product.getQuantity() - orderItemsDto.getQuantity());
                productRepo.save(product);

                // Create order items
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(orderItemsDto.getQuantity());
                orderItem.setItemPrice(product.getPrice());

                // Calculate and set total price
                double totalItemPrice = product.getPrice() * orderItemsDto.getQuantity();
                orderItem.setTotalPrice(totalItemPrice);

                orderItem.setOrder(order);
                return orderItem;
            }
    ).toList();

    // Calculate total order price
    Double totalPrice = orderItems.stream()
            .mapToDouble(OrderItem::getTotalPrice)
            .sum();

    order.setTotalPrice(totalPrice);
    order.setOrderItems(orderItems);

    // Save Order
    Order savedOrder = orderRepo.save(order);
    // Process Payment
    Payments payment=paymentService.processPayment(savedOrder);
    if (payment.getStatus().equals(PaymentStatus.SUCCESS)) {
        order.setStatus(OrderStatus.COMPLETED);
        return savedOrder;
    }
    order.setStatus(OrderStatus.CANCELLED);
    throw new IllegalArgumentException("Checkout failed...retry again");
}
    private void validateOrderItems(List<OrderItemsDto> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
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
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepo.save(product);
        });

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepo.save(order);
    }
}
