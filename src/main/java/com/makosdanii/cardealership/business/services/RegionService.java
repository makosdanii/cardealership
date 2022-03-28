/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.business.services;

import com.makosdanii.cardealership.data.entities.Brand;
import com.makosdanii.cardealership.data.entities.Region;
import com.makosdanii.cardealership.data.entities.Roles;
import com.makosdanii.cardealership.data.repositories.RegionRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author user
 */
@Service
public class RegionService {

    final private RegionRepo rr;

    @Autowired
    public RegionService(RegionRepo rr) {
        this.rr = rr;
    }

    public List<Region> listRegions() {
        return (List<Region>) rr.findAll();
    }

    public List<Region> findRegions(String region_name) {
        return rr.findByRegionName(region_name);
    }

    public Region findById(int id) {
        return rr.findById(id).orElse(new Region());
    }

    public List<Brand> findBrandsofRegion(String brand_name) {
        List<Region> regions = rr.findByRegionName(brand_name);
        return regions.isEmpty() ? null : regions.get(0).getBrands();
    }
}
