/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.business.services;

import com.makosdanii.cardealership.data.entities.Brand;
import com.makosdanii.cardealership.data.repositories.BrandRepo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author user
 */
@Service
public class BrandService {

    final private BrandRepo rr;

    @Autowired
    public BrandService(BrandRepo rr) {
        this.rr = rr;
    }

    public Brand findById(int id) {
        return rr.findById(id).orElse(new Brand());

    }

    public Brand findbyBrandName(String brandName) {
        List<Brand> list = rr.findByBrandName(brandName);
        return list.isEmpty() ? new Brand() : list.get(0);
    }

}
