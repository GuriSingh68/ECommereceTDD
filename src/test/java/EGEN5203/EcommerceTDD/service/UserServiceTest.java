package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.Logindto;
import EGEN5203.EcommerceTDD.dto.Signupdto;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        signupdto.setRole("USER");
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
        signupdto.setRole("USER");
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
        signupdto.setRole("");
        signupdto.setPassword("");
       Exception exception=assertThrows(IllegalArgumentException.class,()->{
           userService.userSignup(signupdto);
       });
       assertEquals("Enter valid input",exception.getMessage());
    }
}