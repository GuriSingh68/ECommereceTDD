package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.Logindto;
import EGEN5203.EcommerceTDD.dto.RoledetailsDTO;
import EGEN5203.EcommerceTDD.dto.Signupdto;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepo userRepo;
    @InjectMocks
    private UserService userService;
    @Test
    void userSignup() {
    }

    @Test
    void login() {
        Users user=new Users();
        user.setEmail("abc@xyz.com");
        user.setPassword("password");
        Logindto logindto=new Logindto();
        logindto.setEmail("abc@xyz.com");
        logindto.setPassword("password");
        when(userRepo.findByEmail(logindto.getEmail())).thenReturn(user);
        when(userRepo.existsByEmail(logindto.getEmail())).thenReturn(true);
        String result=userService.login(logindto);
        assertEquals("User login successfully",result);
    }
    @Test
    void invalidCredentials(){
        Users user=new Users();
        user.setEmail("abc@xyz.com");
        user.setPassword("password");
        Logindto logindto=new Logindto();
        logindto.setEmail("abc@xyz.com");
        logindto.setPassword("Invalid_password");
        when(userRepo.findByEmail(logindto.getEmail())).thenReturn(user);
        when(userRepo.existsByEmail(logindto.getEmail())).thenReturn(true);
        Exception exception=assertThrows(IllegalArgumentException.class, ()->
                userService.login(logindto));
        assertEquals("Bad credentials",exception.getMessage());
    }
    @Test
    void nullValuesLogin(){
        Users user=new Users();
        user.setEmail("abc@xyz.com");
        user.setPassword("password");
        Logindto logindto=new Logindto();
        logindto.setEmail("");
        logindto.setPassword("");
        when(userRepo.findByEmail(logindto.getEmail())).thenReturn(user);
        Exception exception=assertThrows(IllegalArgumentException.class, ()->
                userService.login(logindto));
        assertEquals("Enter valid credentials",exception.getMessage());
    }
    @Test
    void testValidSignup(){
        Signupdto signupdto=new Signupdto();
        signupdto.setEmail("user@example.com");
        signupdto.setFirstName("John");
        signupdto.setLastName("Doe");
        signupdto.setPhoneNumber("1234567890");
        signupdto.setRole(Roles.valueOf("USER"));
        signupdto.setPassword("password123");
        when(userRepo.existsByEmail(signupdto.getEmail())).thenReturn(false);
        String result=userService.userSignup(signupdto);
        assertEquals("User signed up successfully!",result);
    }
    @Test
    void userAlreadyExists(){
        Signupdto signupdto=new Signupdto();
        signupdto.setEmail("user@example.com");
        signupdto.setFirstName("John");
        signupdto.setLastName("Doe");
        signupdto.setPhoneNumber("1234567890");
        signupdto.setRole(Roles.valueOf("USER"));
        signupdto.setPassword("password123");
        when(userRepo.existsByEmail(signupdto.getEmail())).thenReturn(true);
        String result=userService.userSignup(signupdto);
        assertEquals("Email already registered",result);
    }
    @Test
    void userBlankValues(){
        Signupdto signupdto=new Signupdto();
        signupdto.setEmail("");
        signupdto.setFirstName("");
        signupdto.setLastName("");
        signupdto.setPhoneNumber("");
        signupdto.setRole(Roles.valueOf(""));
        signupdto.setPassword("");
       Exception exception=assertThrows(IllegalArgumentException.class,()->{
           userService.userSignup(signupdto);
       });
       assertEquals("Enter valid input",exception.getMessage());
    }
    @Test
    void testUpdateRoles(){
        RoledetailsDTO roledetailsDTO=new RoledetailsDTO();
        roledetailsDTO.setEmail("abc@xyz.com");
        roledetailsDTO.setRole(Roles.ADMIN);
        Users user=new Users();
        user.setEmail("abc@xyz.com");
        user.setRole(Roles.USER);
        when(userRepo.findByEmail(roledetailsDTO.getEmail())).thenReturn(user);
        String result=userService.updateRoles(roledetailsDTO);
        assertEquals("Role updated successfully for user :"+roledetailsDTO.getEmail(),result);
    }
    @Test
    void testBlankDetails(){
        RoledetailsDTO roledetailsDTO=new RoledetailsDTO();
        roledetailsDTO.setEmail("");
        roledetailsDTO.setRole(Roles.USER);
//        assertEquals("Enter valid details",result);
        Exception exception=assertThrows(IllegalArgumentException.class,() ->
              userService.updateRoles(roledetailsDTO)  );
        assertEquals("Enter valid details",exception.getMessage());
    }
    @Test
    void testSameRole(){
        RoledetailsDTO roledetailsDTO=new RoledetailsDTO();
        roledetailsDTO.setEmail("abc@xyz.com");
        roledetailsDTO.setRole(Roles.ADMIN);
        Users user=new Users();
        user.setEmail("abc@xyz.com");
        user.setRole(Roles.ADMIN);
        when(userRepo.findByEmail(roledetailsDTO.getEmail())).thenReturn(user);
        Exception exception=assertThrows(IllegalArgumentException.class,() ->
                userService.updateRoles(roledetailsDTO));
        assertEquals("Same role exist, choose a different role",exception.getMessage());
    }
    @Test
    void testUserNotFound(){
        RoledetailsDTO roledetailsDTO=new RoledetailsDTO();
        roledetailsDTO.setEmail("abc@xyz.com");
        roledetailsDTO.setRole(Roles.ADMIN);
        Exception exception=assertThrows(IllegalArgumentException.class,() ->
                userService.updateRoles(roledetailsDTO));
        assertEquals("User not found",exception.getMessage());
    }

}