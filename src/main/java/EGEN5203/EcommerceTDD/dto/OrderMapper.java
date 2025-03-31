package EGEN5203.EcommerceTDD.dto;

import EGEN5203.EcommerceTDD.model.Order;
import EGEN5203.EcommerceTDD.model.OrderItem;
import EGEN5203.EcommerceTDD.model.Product;
import EGEN5203.EcommerceTDD.model.Users;
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
}
