package EGEN5203.EcommerceTDD.controller;

import EGEN5203.EcommerceTDD.dto.CreateOrderDto;
import EGEN5203.EcommerceTDD.dto.UpdateOrderDto;
import EGEN5203.EcommerceTDD.model.Order;
import EGEN5203.EcommerceTDD.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    /**
     * Order service containing logic of all the orders
     */
    @Autowired
    private OrderService orderService;

    /**
     * Create order endpoint
     * @param createOrderDto
     * @param userId
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderDto createOrderDto, @RequestParam Long userId) {
        Order createdOrder = orderService.createOrder(createOrderDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    /**
     * This end point returns all the orders by Admin only
     * @param adminId
     * @return
     */
    @GetMapping("/admin-orders")
    public ResponseEntity<List<Order>> getOrdersByAdmin(@RequestParam Long adminId) {
        List<Order> orders = orderService.findOrdersByAdminId(adminId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Returns all the customer orders
     * @param customerId
     * @return
     */
    @GetMapping("/customer-orders")
    public ResponseEntity<List<Order>> getCustomerOrders(@RequestParam Long customerId) {
        List<Order> orders = orderService.findOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }
}