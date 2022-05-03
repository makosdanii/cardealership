/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.data.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "brand")
public class Brand implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "brand_name", nullable = false, unique = true, updatable = true)
    private String brandName;

    @JoinColumn(name = "region_id")
    @ManyToOne
    @JsonBackReference
    private Region region;
    @JsonBackReference
    @OneToMany(mappedBy = "brand")
    private List<Store> store;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public List<Store> getStore() {
        return store;
    }

    public void setStore(List<Store> store) {
        this.store = store;
    }

    public Brand(int id, String brand_name, Region region, List<Store> users) {
        this.id = id;
        this.brandName = brand_name;
        this.region = region;
        this.store = users;
    }

    public Brand() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return ((Brand) obj).id == this.id;
    }

}
