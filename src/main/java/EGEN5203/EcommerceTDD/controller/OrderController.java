package EGEN5203.EcommerceTDD.controller;

import EGEN5203.EcommerceTDD.dto.AddToCartDto;
import EGEN5203.EcommerceTDD.dto.CreateOrderDto;
import EGEN5203.EcommerceTDD.model.Order;
import EGEN5203.EcommerceTDD.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders") public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderDto createOrderDto, @RequestParam Long userId){
        Order createdOrder = orderService.createOrder(createOrderDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
}
