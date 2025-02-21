package EGEN5203.EcommerceTDD.stepDefinitions;

import EGEN5203.EcommerceTDD.dto.Logindto;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Ensures mocks are initialized
public class stepDefinitions {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService; // Inject mock repo into service

    private Users user;
    private Logindto logindto;
    private String loginResult;

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
        Assertions.assertEquals("User login successfully", loginResult);
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

        Assertions.assertEquals("User not found",loginResult);
    }
}
