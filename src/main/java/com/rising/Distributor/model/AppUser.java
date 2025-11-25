package com.rising.Distributor.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String number;

    private String role;

    private Boolean isVerified = false;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<Address> addressList;

    public AppUser(String uid, String name, String email, String number, String role, List<Address> addressList) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.number = number;
        this.role = role;
        this.addressList = addressList;
        this.isVerified = false;
    }

}
