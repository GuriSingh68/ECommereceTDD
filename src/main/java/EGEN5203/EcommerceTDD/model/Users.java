package EGEN5203.EcommerceTDD.model;

import EGEN5203.EcommerceTDD.enums.Roles;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="USERS") public class Users {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private long user_id;
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Roles role;
}
