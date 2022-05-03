/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.data.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "users")
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "drivers_license")
    private Boolean driversLicense;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @JoinColumn(name = "role_id")
    @ManyToOne
    @JsonManagedReference
    private Roles role;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Store> store;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isDrivers_license() {
        return driversLicense;
    }

    public void setDriversLicense(Boolean driversLicense) {
        this.driversLicense = driversLicense;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public Set<Store> getStore() {
        return store;
    }

    public void setStore(Set<Store> store) {
        this.store = store;
    }

    public Users(String email, String name, Boolean drivers_license,
            String password, Roles role) {
        this.email = email;
        this.name = name;
        this.driversLicense = drivers_license;
        this.password = password;
        this.role = role;
    }

    public Users() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        return ((Users) obj).getId() == this.getId();
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(this.getId()).hashCode();
    }
}
