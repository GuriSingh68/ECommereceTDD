package EGEN5203.EcommerceTDD.controller;

import EGEN5203.EcommerceTDD.dto.Logindto;
import EGEN5203.EcommerceTDD.dto.RoledetailsDTO;
import EGEN5203.EcommerceTDD.dto.Signupdto;
import EGEN5203.EcommerceTDD.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/signup")
    public String signup(@RequestBody Signupdto signupdto){
        return  userService.userSignup(signupdto);
    }
    @PostMapping("/login")
    public String login(@RequestBody Logindto logindto){
        return userService.login(logindto);
    }
    @PatchMapping("/updateRole")
    public String updateRole(@RequestBody RoledetailsDTO roledetailsDTO){
        return userService.updateRoles(roledetailsDTO);
    }
}
