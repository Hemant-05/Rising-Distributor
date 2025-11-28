package com.rising.Distributor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "admins")
@NoArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String role;

    @Column(nullable = false)
    private String password;

    public Admin(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = "ADMIN";
    }
}
