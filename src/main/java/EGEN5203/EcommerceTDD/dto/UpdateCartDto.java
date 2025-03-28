package EGEN5203.EcommerceTDD.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCartDto {
    private List<ProductDto> products;
    private Long customerId;
}