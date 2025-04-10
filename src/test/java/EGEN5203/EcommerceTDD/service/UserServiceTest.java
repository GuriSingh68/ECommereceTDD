package EGEN5203.EcommerceTDD.service;

import EGEN5203.EcommerceTDD.dto.Logindto;
import EGEN5203.EcommerceTDD.dto.RoledetailsDTO;
import EGEN5203.EcommerceTDD.dto.Signupdto;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    /**
     * Mocking user repo
     */
    @Mock
    private UserRepo userRepo;
    /**
     * Mocking user service
     */
    @InjectMocks
    private UserService userService;

    @Test
    void userSignup() {
    }

    /**
     * Testing user login
     * @throws JsonProcessingException
     */
    @Test
    void login() throws JsonProcessingException {
        Users user = new Users();
        user.setEmail("abc@xyz.com");
        user.setPassword("password");
        Logindto logindto = new Logindto();
        logindto.setEmail("abc@xyz.com");
        logindto.setPassword("password");
        when(userRepo.findByEmail(logindto.getEmail())).thenReturn(user);

        String result = userService.login(logindto);

        String expectedJson = "{\"user\": {\"user_id\":0,\"firstName\":null,\"lastName\":null,\"email\":\"abc@xyz.com\",\"phoneNumber\":null,\"password\":\"password\",\"role\":null}}";
        assertEquals(expectedJson, result);
    }

    /**
     * Testing invalid credentials entered by user
     */
    @Test
    void invalidCredentials() {
        Users user = new Users();
        user.setEmail("abc@xyz.com");
        user.setPassword("password");
        Logindto logindto = new Logindto();
        logindto.setEmail("abc@xyz.com");
        logindto.setPassword("Invalid_password");
        when(userRepo.findByEmail(logindto.getEmail())).thenReturn(user); // Mocking findByEmail

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.login(logindto));
        assertEquals("Bad credentials", exception.getMessage());
    }

    /**
     * Testing null values passed during login
     */
    @Test
    void nullValuesLogin() {
        Users user = new Users();
        user.setEmail("abc@xyz.com");
        user.setPassword("password");
        Logindto logindto = new Logindto();
        logindto.setEmail("");
        logindto.setPassword("");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.login(logindto));
        assertEquals("Enter valid credentials", exception.getMessage());
    }

    /**
     * Test valid signup
     */
    @Test
    void testValidSignup() {
        Signupdto signupdto = new Signupdto();
        signupdto.setEmail("user@example.com");
        signupdto.setFirstName("John");
        signupdto.setLastName("Doe");
        signupdto.setPhoneNumber("1234567890");
        signupdto.setRole(Roles.valueOf("USER"));
        signupdto.setPassword("password123");
        when(userRepo.existsByEmail(signupdto.getEmail())).thenReturn(false); // Mocking existsByEmail

        String result = userService.userSignup(signupdto);

        assertEquals("{\"message\": \"User signed up successfully!\"}", result);
    }

    /**
     * Testing user already exists
     */
    @Test
    void userAlreadyExists() {
        Signupdto signupdto = new Signupdto();
        signupdto.setEmail("user@example.com");
        signupdto.setFirstName("John");
        signupdto.setLastName("Doe");
        signupdto.setPhoneNumber("1234567890");
        signupdto.setRole(Roles.valueOf("USER"));
        signupdto.setPassword("password123");
        when(userRepo.existsByEmail(signupdto.getEmail())).thenReturn(true);
        String result = userService.userSignup(signupdto);

        assertEquals("{\"error\": \"Email already registered\"}", result);
    }

    /**
     * Testing blank values
     */
    @Test
    void userBlankValues() {
        Signupdto signupdto = new Signupdto();
        signupdto.setEmail("");
        signupdto.setFirstName("");
        signupdto.setLastName("");
        signupdto.setPhoneNumber("");
        signupdto.setRole(Roles.USER);
        signupdto.setPassword("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.userSignup(signupdto);
        });
        assertEquals("Enter valid input", exception.getMessage());
    }

    /**
     * Testing updating roles by admin
     */
    @Test
    void testUpdateRoles() {
        RoledetailsDTO roledetailsDTO = new RoledetailsDTO();
        roledetailsDTO.setEmail("abc@xyz.com");
        roledetailsDTO.setRole(Roles.ADMIN);
        Users user = new Users();
        user.setEmail("abc@xyz.com");
        user.setRole(Roles.USER);
        when(userRepo.findByEmail(roledetailsDTO.getEmail())).thenReturn(user); // Mocking findByEmail

        String result = userService.updateRoles(roledetailsDTO);

        assertEquals("Role updated successfully for user :" + roledetailsDTO.getEmail(), result);
    }

    /**
     * Testing blank details enters while processing
     */
    @Test
    void testBlankDetails() {
        RoledetailsDTO roledetailsDTO = new RoledetailsDTO();
        roledetailsDTO.setEmail("");
        roledetailsDTO.setRole(Roles.USER);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updateRoles(roledetailsDTO));
        assertEquals("Not Authorised", exception.getMessage());

        roledetailsDTO.setEmail("");
        roledetailsDTO.setRole(Roles.ADMIN);

        exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updateRoles(roledetailsDTO));
        assertEquals("Enter valid details", exception.getMessage());
    }

    /**
     * Testing updating to same role
     */
    @Test
    void testSameRole() {
        RoledetailsDTO roledetailsDTO = new RoledetailsDTO();
        roledetailsDTO.setEmail("abc@xyz.com");
        roledetailsDTO.setRole(Roles.ADMIN);
        Users user = new Users();
        user.setEmail("abc@xyz.com");
        user.setRole(Roles.ADMIN);
        when(userRepo.findByEmail(roledetailsDTO.getEmail())).thenReturn(user); // Mocking findByEmail

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updateRoles(roledetailsDTO));
        assertEquals("Same role exist, choose a different role", exception.getMessage());
    }

    /**
     * Testing user not found
     */
    @Test
    void testUserNotFound() {
        RoledetailsDTO roledetailsDTO = new RoledetailsDTO();
        roledetailsDTO.setEmail("abc@xyz.com");
        roledetailsDTO.setRole(Roles.ADMIN);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updateRoles(roledetailsDTO));
        assertEquals("User not found", exception.getMessage());
    }

    /**
     * Testing deleting a user
     */
    @Test
    void deleteUser() {
        Users user = new Users();
        user.setEmail("delete@user.com");

        userRepo.delete(user); // Directly calling the delete method of the mocked repo.
    }

    /**
     * Testing trying to delete non-existent user
     */
    @Test
    void adminDeletesNonExistingUser() {
        RoledetailsDTO roledetailsDTO = new RoledetailsDTO();
        roledetailsDTO.setEmail("aaaaaa@aaa.com");
        roledetailsDTO.setRole(Roles.ADMIN);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUsers("nonexistent@user.com", roledetailsDTO);
        });
        assertEquals("User not present", exception.getMessage());
    }

    /**
     * Testing deleting an eisting user
     */
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

    /**
     * Testing user not found during deleting
     */
    @Test
    void testUserNotFoundThrowsException() {
        // Given
        String email = "abc@xyz.com";
        Logindto loginDTO = new Logindto();
        loginDTO.setEmail(email);
        loginDTO.setPassword("Aaaaa");

        // Mock: userRepo returns null for the email
        when(userRepo.findByEmail(email)).thenReturn(null);

        // When + Then: assert that exception is thrown
        Exception exception = assertThrows(NullPointerException.class, () ->
                userService.login(loginDTO)
        );

        assertEquals("User not found", exception.getMessage());
    }

    /**
     * TEsting  user with any other role than admin or user
     */
    @Test
    void testUpdateRoles_ThrowsGenericErrorWhenUserRoleIsUnexpected() {
        // Arrange
        RoledetailsDTO roledetailsDTO = new RoledetailsDTO();
        roledetailsDTO.setEmail("abc@xyz.com");
        roledetailsDTO.setRole(Roles.ADMIN);

        Users user = new Users();
        user.setEmail("abc@xyz.com");
        user.setRole(Roles.TESTROLE); // Or any role not USER or ADMIN

        when(userRepo.findByEmail("abc@xyz.com")).thenReturn(user);

        // Act + Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateRoles(roledetailsDTO);
        });

        assertEquals("Error", exception.getMessage());
    }

    /**
     * Testing user delete successfully
     */
    @Test
    void testDeleteUsers_SuccessfullyDeletesUser() {
        // Arrange
        String email = "test@example.com";

        RoledetailsDTO dto = new RoledetailsDTO();
        dto.setRole(Roles.ADMIN); // Authorized

        Users user = new Users();
        user.setEmail(email);
        user.setRole(Roles.USER); // Could be anything

        when(userRepo.findByEmail(email)).thenReturn(user);

        // Act
        String result = userService.deleteUsers(email, dto);

        // Assert
        assertEquals("User: test@example.com deleted successfully.", result);
    }

}