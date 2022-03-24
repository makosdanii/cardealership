/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.data.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Embeddable
public class StoreKeys implements Serializable {

    @NotNull
    @Column(name = "user_id")
    private int userId;

    @NotNull
    @Column(name = "brand_id")
    private int brandId;

    @NotNull
    @Column(name = "model", nullable = false)
    private String model;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brand_id) {
        this.brandId = brand_id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public StoreKeys(int user_id, int brand_id, String model) {
        this.userId = user_id;
        this.brandId = brand_id;
        this.model = model;
    }

    public StoreKeys() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return (((StoreKeys) obj).brandId == this.brandId
                && ((StoreKeys) obj).userId == this.userId
                && ((StoreKeys) obj).model == this.model);
    }

    @Override
    public int hashCode() {
        return brandId * userId * model.hashCode();
    }
}
