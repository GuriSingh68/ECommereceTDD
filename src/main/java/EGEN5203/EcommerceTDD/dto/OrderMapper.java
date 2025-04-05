package EGEN5203.EcommerceTDD.dto;

import EGEN5203.EcommerceTDD.model.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {
    public OrderResponseDto toOrderResponseDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setUser(toUserDto(order.getUser()));
        dto.setOrderItems(order.getOrderItems().stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toList()));
        dto.setTotalPrice(order.getTotalPrice());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setEstimatedDeliveryDate(order.getEstimatedDeliveryDate());
        if (order.getPayments() != null) {
            dto.setPayment(toPaymentInfoDto(order.getPayments()));
        }
        return dto;
    }

    private UserDto toUserDto(Users user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUser_id());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        return dto;
    }

    private OrderItemsDto toOrderItemDto(OrderItem item) {
        OrderItemsDto dto = new OrderItemsDto();
        dto.setId(item.getId());
        dto.setProduct(toProductDto(item.getProduct()));
        dto.setQuantity(item.getQuantity());
        dto.setItemPrice(item.getItemPrice());
        dto.setTotalPrice(item.getTotalPrice());
        return dto;
    }

    private ProductDto toProductDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getStock());
        dto.setCategory(product.getCategory());
        return dto;
    }

    private PaymentInfoDto toPaymentInfoDto(Payments payment) {
        PaymentInfoDto dto = new PaymentInfoDto();
        dto.setId(payment.getId());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setCardLastFour(payment.getCardLastFour());
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setStatus(payment.getStatus());
        return dto;
    }
}
