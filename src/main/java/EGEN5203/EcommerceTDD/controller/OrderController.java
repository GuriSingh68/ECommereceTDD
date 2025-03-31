package EGEN5203.EcommerceTDD.controller;

import EGEN5203.EcommerceTDD.dto.CreateOrderDto;
import EGEN5203.EcommerceTDD.dto.UpdateOrdersDto;

import EGEN5203.EcommerceTDD.model.Order;
import EGEN5203.EcommerceTDD.model.OrderItem;
import EGEN5203.EcommerceTDD.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders") public class OrderController {
    @Autowired
    private OrderService orderService;
    //Create orders
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderDto createOrderDto, @RequestParam Long userId){
        Order createdOrder = orderService.createOrder(createOrderDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
    //Get order list
    @GetMapping("/orderlist")
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> getAllOrders= orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(getAllOrders);
    }
    @GetMapping("/")
    public ResponseEntity<Order> getOrdersById(@RequestParam Long id){
        Order ordersById=orderService.fetchOrdersById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ordersById);
    }
    @GetMapping("users")
    public ResponseEntity<List<Order>> getOrdersByUserId(@RequestParam Long userId){
        List<Order> order=orderService.getUserOrdersById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }
    @DeleteMapping("/deleteOrder")
    public ResponseEntity<String> deleteOrder(@RequestParam Long orderId){
        Order deleteOrder=orderService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("Order deleted successfully");
    }
    //Admin can update order status only
    @PatchMapping("/updateOrder")
    public ResponseEntity<Order> updateOrderStatus(@RequestParam Integer userId, @RequestBody UpdateOrdersDto orderStatus){
        Order updateOrder=orderService.updateOrderStatus(userId,orderStatus);
                return ResponseEntity.status(HttpStatus.OK).body(updateOrder);
    }

}
