package EGEN5203.EcommerceTDD.stepDefinitions;

import EGEN5203.EcommerceTDD.dto.Logindto;
import EGEN5203.EcommerceTDD.dto.RoledetailsDTO;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import EGEN5203.EcommerceTDD.service.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Ensures mocks are initialized
public class stepDefinitions {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService; // Inject mock repo into service

    private Users user;
    private Logindto logindto;
    private RoledetailsDTO roledetailsDTO;
    private String loginResult;
    private String userRole;
    private String deleteResult;
    private Exception exception;

    public stepDefinitions() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Given("A user with email {string} and password {string}")
    public void testLoginForExistingUser(String email, String password) {
        user = new Users();
        user.setEmail(email);
        user.setPassword(password);

        when(userRepo.findByEmail(email)).thenReturn(user);
        when(userRepo.existsByEmail(email)).thenReturn(true);
    }

    @When("The user tries to log in with email {string} and password {string}")
    public void userLoginsWithCred(String email, String password) {

        logindto = new Logindto();
        logindto.setEmail(email);
        logindto.setPassword(password);

        loginResult = userService.login(logindto);
    }

    @Then("The login should be successful")
    public void successfulLogin() {
        assertEquals("User login successfully", loginResult);
    }

    @Given("No user exists with email {string}")
    public void userNotExists(String email){
        when(userRepo.findByEmail(email)).thenReturn(null);
    }

    @When("The user tries to log in with invalid email {string} and password {string}")
    public void userEntersInvalidCred(String email,String password){
        logindto=new Logindto();
        logindto.setEmail(email);
        logindto.setPassword(password);
        Exception e = Assertions.assertThrows(IllegalArgumentException.class,()->
                userService.login(logindto) );
        loginResult=e.getMessage();
    }
    @Then("The login should fail with an error message {string}")
    public void invalidLoginException(String message){

        assertEquals("User not found",loginResult);
    }
    @Given("an existing user with email {string} and role {string}")
    public void an_existing_user_with_email_and_role(String email, String role) {
        // Create user object with given email and role
        user = new Users();
        user.setEmail(email);
        user.setRole(Roles.valueOf(role));

        // Mock repository behavior
        when(userRepo.findByEmail(email)).thenReturn(user);

        // Create role details DTO
        roledetailsDTO = new RoledetailsDTO();
        roledetailsDTO.setEmail(email);
        roledetailsDTO.setRole(Roles.valueOf(role));
    }

    @When("the admin updates the role of {string} to {string}")
    public void the_admin_updates_the_role_of_to(String email, String newRole) {
        // Set the new role in the DTO
        roledetailsDTO.setRole(Roles.valueOf(newRole));
        userRole = userService.updateRoles(roledetailsDTO);
    }

    @Then("the system should return {string}")
    public void the_system_should_return(String expectedMessage) {
        // Assert expected message and role update
        assertEquals(expectedMessage, userRole);
        assertEquals(roledetailsDTO.getRole(), user.getRole());
    }
//
//    @When("the admin attempts to update the role of {string} to {string}")
//    public void whenAdminAttemptsToUpdateNonExistentUser(String email, String newRole) {
//        roledetailsDTO.setEmail(email);
//        try {
//            roledetailsDTO.setRole(Roles.valueOf(newRole));
//            userRole = userService.updateRoles(roledetailsDTO);
//        } catch (Exception e) {
//            exception = e;
//        }
//    }
//
//    @Then("the system should return an error message {string}")
//    public void thenSystemReturnsError(String expectedMessage) {
//        assertNotNull(exception);
//        assertEquals(expectedMessage, exception.getMessage());
//    }
    @Given("User exist in our database with email {string}")
   public void userExistsInOurDatabase(String email){
        Users user=new Users();
        user.setEmail("abc@xyz.com");
        when(userRepo.findByEmail(user.getEmail())).thenReturn(user);
    }
    @When("Admin tries to delete the {string} from our database")
   public void adminDeleteUser(String email){
        RoledetailsDTO roledetailsDTO1=new RoledetailsDTO();
        roledetailsDTO1.setRole(Roles.ADMIN);
        roledetailsDTO1.setEmail("user@xyz.com");
        deleteResult=userService.deleteUsers(email,roledetailsDTO1);
    }
    @Then("the system successfully deletes the user")
   public void successfulDeleteUser(){
        Assertions.assertEquals("User: abc@xyz.com deleted successfully.",deleteResult);
    }
}

