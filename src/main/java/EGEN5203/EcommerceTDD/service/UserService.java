package EGEN5203.EcommerceTDD.service;


import EGEN5203.EcommerceTDD.dto.Logindto;
import EGEN5203.EcommerceTDD.dto.RoledetailsDTO;
import EGEN5203.EcommerceTDD.dto.Signupdto;
import EGEN5203.EcommerceTDD.enums.Roles;
import EGEN5203.EcommerceTDD.model.Users;
import EGEN5203.EcommerceTDD.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

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


    public String updateRoles(RoledetailsDTO roledetailsDTO) {
        if(roledetailsDTO.getRole().equals(Roles.ADMIN)) {
            if (roledetailsDTO.getRole().isBlank() || roledetailsDTO.getEmail().isBlank()) {
                throw new IllegalArgumentException("Enter valid details");
            }
            Users user = userRepo.findByEmail(roledetailsDTO.getEmail());
            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }
            if (user.getRole().equals(Roles.USER) || user.getRole().equals(Roles.ADMIN)) {
                if (user.getRole().equals(roledetailsDTO.getRole())) {
                    throw new IllegalArgumentException("Same role exist, choose a different role");
                }
                user.setRole(roledetailsDTO.getRole());
                userRepo.save(user);
                return "Role updated successfully for user :" + roledetailsDTO.getEmail();
            }
            throw new IllegalArgumentException("Error");
        }
        else {
            throw new IllegalArgumentException("Not Authorised");
        }
    }

    public String deleteUsers(String email,RoledetailsDTO roledetailsDTO) {
        if(roledetailsDTO.getRole().equals(Roles.ADMIN)){
            Users user=userRepo.findByEmail(email);
            if(user==null){
                throw new IllegalArgumentException("User not present");
            }
            userRepo.delete(user);
            return "User: "+ email+ " deleted successfully ";
        }
        throw new IllegalArgumentException("Unauthorised Access");
    }
}
