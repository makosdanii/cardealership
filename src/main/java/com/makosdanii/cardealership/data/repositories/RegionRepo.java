/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.data.repositories;

import com.makosdanii.cardealership.data.entities.Roles;
import com.makosdanii.cardealership.data.entities.Users;
import com.makosdanii.cardealership.data.entities.Region;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public interface RegionRepo extends CrudRepository<Region, Integer> {

    List<Region> findByRegionName(String region_name);

    @Query("SELECT DISTINCT r FROM Region r LEFT JOIN FETCH r.brands")
    List<Region> findAllFetchBrands();

    @Query("SELECT DISTINCT r FROM Region r LEFT JOIN FETCH r.roles")
    List<Region> findAllFetchRoles();

    public void save(Roles role);
}
