package EGEN5203.EcommerceTDD.controller;

import EGEN5203.EcommerceTDD.dto.Logindto;
import EGEN5203.EcommerceTDD.dto.RoledetailsDTO;
import EGEN5203.EcommerceTDD.dto.Signupdto;
import EGEN5203.EcommerceTDD.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    /**
     * Service containing logic of managing users
     */
    @Autowired
    private UserService userService;

    /**
     * End point for handling request for user signup
     * @param signupdto
     * @return
     */
    @PostMapping("/signup")
    public String signup(@RequestBody Signupdto signupdto){
        return  userService.userSignup(signupdto);
    }

    /**
     * End point for handling request for user login
     * @param logindto
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/login")
    public String login(@RequestBody Logindto logindto) throws JsonProcessingException {
        return userService.login(logindto);
    }

    /**
     * End point for updating roles by Admin
     * @param roledetailsDTO
     * @return
     */
    @PatchMapping("/updateRole")
    public String updateRole(@RequestBody RoledetailsDTO roledetailsDTO){
        return userService.updateRoles(roledetailsDTO);
    }

    /**
     * End point for deleting users by admin
     * @param email
     * @param roledetailsDTO
     * @return
     */
    @DeleteMapping("/users/{email}")
    public String deleteUsers(@PathVariable String email,RoledetailsDTO roledetailsDTO){
        return userService.deleteUsers(email,roledetailsDTO);
    }
}
