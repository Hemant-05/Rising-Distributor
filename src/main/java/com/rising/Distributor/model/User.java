package com.rising.Distributor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "customers")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String mobileNumber;
    
    private Boolean isMobileVerified = false;

    @Column(nullable = false)
    private String password;

    private String otp;
    private LocalDateTime otpExpiryTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addressList;

    public User(String name, String email, String mobileNumber, String password) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.isMobileVerified = false;
    }
}
