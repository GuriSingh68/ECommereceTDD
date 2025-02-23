package EGEN5203.EcommerceTDD.dto;

import EGEN5203.EcommerceTDD.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoledetailsDTO {
    String email;
    Roles role;
}
