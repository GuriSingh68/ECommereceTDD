package EGEN5203.EcommerceTDD.controller;

import EGEN5203.EcommerceTDD.dto.CreateOrderDto;
import EGEN5203.EcommerceTDD.dto.OrderMapper;
import EGEN5203.EcommerceTDD.dto.OrderResponseDto;
import EGEN5203.EcommerceTDD.dto.UpdateOrdersDto;

import EGEN5203.EcommerceTDD.model.Order;
import EGEN5203.EcommerceTDD.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders") public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestBody CreateOrderDto createOrderDto,
            @RequestParam Long userId) {
        Order createdOrder = orderService.createOrder(createOrderDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderMapper.toOrderResponseDto(createdOrder));
    }

    @GetMapping("/orderlist")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderResponseDto> dtos = orders.stream()
                .map(orderMapper::toOrderResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/myOrders")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@RequestParam Long userId) {
        List<Order> orders = orderService.getUserOrdersById(userId);
        List<OrderResponseDto> dtos = orders.stream()
                .map(orderMapper::toOrderResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/admin/allOrders")
    public ResponseEntity<List<OrderResponseDto>> getAllOrdersForAdmin() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderResponseDto> dtos = orders.stream()
                .map(orderMapper::toOrderResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/deleteOrder")
    public ResponseEntity<String> deleteOrder(@RequestParam Long orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Order Deleted Successfully\"}");
    }
    //Admin can update order status only
    @PatchMapping("/updateOrder")
    public ResponseEntity<Order> updateOrderStatus(@RequestParam Integer userId, @RequestBody UpdateOrdersDto orderStatus){
        Order updateOrder=orderService.updateOrderStatus(userId,orderStatus);
                return ResponseEntity.status(HttpStatus.OK).body(updateOrder);
    }

}
