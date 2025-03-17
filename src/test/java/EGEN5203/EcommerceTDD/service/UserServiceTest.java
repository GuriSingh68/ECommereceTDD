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
    private UserRepo userRepo; // Mocking the UserRepo dependency
    @InjectMocks
    private UserService userService; // Injecting the mocked UserRepo into UserService

    @Test
    void userSignup() {
        // Test case for userSignup method (currently empty)
        // Implementation needed
    }

    @Test
    void login() {
        // Arrange: Setting up the test data and mocking behavior
        Users user = new Users();
        user.setEmail("abc@xyz.com");
        user.setPassword("password");
        Logindto logindto = new Logindto();
        logindto.setEmail("abc@xyz.com");
        logindto.setPassword("password");
        when(userRepo.findByEmail(logindto.getEmail())).thenReturn(user); // Mocking the findByEmail method

        // Act: Calling the method to be tested
        String result = userService.login(logindto);

        // Assert: Verifying the result
        assertEquals("User login successfully", result);
    }

    @Test
    void invalidCredentials() {
        // Arrange
        Users user = new Users();
        user.setEmail("abc@xyz.com");
        user.setPassword("password");
        Logindto logindto = new Logindto();
        logindto.setEmail("abc@xyz.com");
        logindto.setPassword("Invalid_password");
        when(userRepo.findByEmail(logindto.getEmail())).thenReturn(user); // Mocking findByEmail

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.login(logindto));
        assertEquals("Bad credentials", exception.getMessage());
    }

    @Test
    void nullValuesLogin() {
        // Arrange
        Users user = new Users();
        user.setEmail("abc@xyz.com");
        user.setPassword("password");
        Logindto logindto = new Logindto();
        logindto.setEmail("");
        logindto.setPassword("");
        when(userRepo.findByEmail(logindto.getEmail())).thenReturn(user); // Mocking findByEmail

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.login(logindto));
        assertEquals("Enter valid credentials", exception.getMessage());
    }

    @Test
    void testValidSignup() {
        // Arrange
        Signupdto signupdto = new Signupdto();
        signupdto.setEmail("user@example.com");
        signupdto.setFirstName("John");
        signupdto.setLastName("Doe");
        signupdto.setPhoneNumber("1234567890");
        signupdto.setRole(Roles.valueOf("USER"));
        signupdto.setPassword("password123");
        when(userRepo.existsByEmail(signupdto.getEmail())).thenReturn(false); // Mocking existsByEmail

        // Act
        String result = userService.userSignup(signupdto);

        // Assert
        assertEquals("User signed up successfully!", result);
    }

    @Test
    void userAlreadyExists() {
        // Arrange
        Signupdto signupdto = new Signupdto();
        signupdto.setEmail("user@example.com");
        signupdto.setFirstName("John");
        signupdto.setLastName("Doe");
        signupdto.setPhoneNumber("1234567890");
        signupdto.setRole(Roles.valueOf("USER"));
        signupdto.setPassword("password123");
        when(userRepo.existsByEmail(signupdto.getEmail())).thenReturn(true); // Mocking existsByEmail

        // Act
        String result = userService.userSignup(signupdto);

        // Assert
        assertEquals("Email already registered", result);
    }

    @Test
    void userBlankValues() {
        // Arrange
        Signupdto signupdto = new Signupdto();
        signupdto.setEmail("");
        signupdto.setFirstName("");
        signupdto.setLastName("");
        signupdto.setPhoneNumber("");
        signupdto.setRole(Roles.USER);
        signupdto.setPassword("");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.userSignup(signupdto);
        });
        assertEquals("Enter valid input", exception.getMessage());
    }

    @Test
    void testUpdateRoles() {
        // Arrange
        RoledetailsDTO roledetailsDTO = new RoledetailsDTO();
        roledetailsDTO.setEmail("abc@xyz.com");
        roledetailsDTO.setRole(Roles.ADMIN);
        Users user = new Users();
        user.setEmail("abc@xyz.com");
        user.setRole(Roles.USER);
        when(userRepo.findByEmail(roledetailsDTO.getEmail())).thenReturn(user); // Mocking findByEmail

        // Act
        String result = userService.updateRoles(roledetailsDTO);

        // Assert
        assertEquals("Role updated successfully for user :" + roledetailsDTO.getEmail(), result);
    }

    @Test
    void testBlankDetails() {
        // Arrange
        RoledetailsDTO roledetailsDTO = new RoledetailsDTO();
        roledetailsDTO.setEmail("");
        roledetailsDTO.setRole(Roles.USER);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updateRoles(roledetailsDTO));
        assertEquals("Enter valid details", exception.getMessage());
    }

    @Test
    void testSameRole() {
        // Arrange
        RoledetailsDTO roledetailsDTO = new RoledetailsDTO();
        roledetailsDTO.setEmail("abc@xyz.com");
        roledetailsDTO.setRole(Roles.ADMIN);
        Users user = new Users();
        user.setEmail("abc@xyz.com");
        user.setRole(Roles.ADMIN);
        when(userRepo.findByEmail(roledetailsDTO.getEmail())).thenReturn(user); // Mocking findByEmail

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updateRoles(roledetailsDTO));
        assertEquals("Same role exist, choose a different role", exception.getMessage());
    }

    @Test
    void testUserNotFound() {
        // Arrange
        RoledetailsDTO roledetailsDTO = new RoledetailsDTO();
        roledetailsDTO.setEmail("abc@xyz.com");
        roledetailsDTO.setRole(Roles.ADMIN);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updateRoles(roledetailsDTO));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void deleteUser() {
        // Arrange
        Users user = new Users();
        user.setEmail("delete@user.com");

        // Act
        userRepo.delete(user); // Directly calling the delete method of the mocked repo.
    }

    @Test
    void adminDeletesNonExistingUser() {
        // Arrange
        RoledetailsDTO roledetailsDTO = new RoledetailsDTO();
        roledetailsDTO.setEmail("aaaaaa@aaa.com");
        roledetailsDTO.setRole(Roles.ADMIN);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUsers("nonexistent@user.com", roledetailsDTO);
        });
        assertEquals("User not present", exception.getMessage());
    }
    @Test

    void userRoleDeletesExistingUser(){

        RoledetailsDTO roledetailsDTO=new RoledetailsDTO();

        roledetailsDTO.setEmail("aaaaaa@aaa.com");

        roledetailsDTO.setRole(Roles.USER);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {

            userService.deleteUsers("nonexistent@user.com", roledetailsDTO);

        });

        assertEquals("Unauthorised Access", exception.getMessage());

    }

    

}