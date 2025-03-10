package EGEN5203.EcommerceTDD.dto;

import EGEN5203.EcommerceTDD.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Signupdto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private Roles role;
}
