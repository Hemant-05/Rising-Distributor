package com.rising.Distributor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String details;

    @ManyToOne
    @JoinColumn(name = "customer_uid")
    private User user;
}
