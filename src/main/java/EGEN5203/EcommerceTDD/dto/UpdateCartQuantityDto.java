package EGEN5203.EcommerceTDD.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCartQuantityDto {
    private Integer quantity;
}
