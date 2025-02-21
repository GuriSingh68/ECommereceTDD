package EGEN5203.EcommerceTDD.service;


import EGEN5203.EcommerceTDD.dto.Logindto;
import EGEN5203.EcommerceTDD.dto.Signupdto;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    public String userSignup(Signupdto signupdto){
    if(userRepo.existsByEmail(signupdto.getEmail())){
        return "Email already registered";
    }
    if(signupdto.getEmail().isBlank()||signupdto.getFirstName().isBlank()||signupdto.getLastName().isBlank()||signupdto.getPassword().isBlank()||signupdto.getRole().isBlank()||signupdto.getPhoneNumber().isBlank()){
        throw new IllegalArgumentException("Enter valid input");
    }
        Users user=new Users();
        user.setEmail(signupdto.getEmail());
        user.setFirstName(signupdto.getFirstName());
        user.setLastName(signupdto.getLastName());
        user.setPhoneNumber(signupdto.getPhoneNumber());
        user.setRole(signupdto.getRole());
        user.setPassword(signupdto.getPassword());
        userRepo.save(user);
        return "User signed up successfully!";
    }

    public String login(Logindto logindto) {
        Users user=userRepo.findByEmail(logindto.getEmail());
        if (user ==null){
            throw new IllegalArgumentException("User not found");
        }
        if(logindto.getEmail().isBlank()|| logindto.getPassword().isBlank()){
            throw new IllegalArgumentException("Enter valid credentials");
        }

        if(user.getEmail().equals(logindto.getEmail())){
            if( user.getPassword().equals(logindto.getPassword())){
                return "User login successfully";
            }
        }
        throw new IllegalArgumentException("Bad credentials");
    }
}
