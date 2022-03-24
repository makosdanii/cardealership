/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.data.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

/**
 *
 * @author user
 */
@Entity
public class Store implements Serializable {

    @EmbeddedId
    private StoreKeys id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("brandId")
    @JoinColumn(name = "brand_id", updatable = true)
    private Brand brand;

    @Column(name = "quantity")
    private int quantity;

    public StoreKeys getId() {
        return id;
    }

    public void setId(StoreKeys id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Store(StoreKeys id, Users user, Brand brand, int quantity) {
        this.id = id;
        this.quantity = quantity;
        this.user = user;
        this.brand = brand;
    }

    public Store() {
    }
}
