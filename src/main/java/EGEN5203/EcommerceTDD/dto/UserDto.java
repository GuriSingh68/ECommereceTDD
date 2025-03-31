package EGEN5203.EcommerceTDD.dto;

import EGEN5203.EcommerceTDD.enums.Roles;
import lombok.Data;

@Data
public class UserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Roles role;
}
