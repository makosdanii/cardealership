/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.data.repositories;

import com.makosdanii.cardealership.data.entities.Brand;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public interface BrandRepo extends CrudRepository<Brand, Integer> {

    List<Brand> findByBrandName(String brandName);
}
