/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.data.repositories;

import com.makosdanii.cardealership.data.entities.Roles;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public interface RoleRepo extends CrudRepository<Roles, Integer> {

    List<Roles> findByRoleName(String role_name);

    @Query("SELECT DISTINCT r FROM Roles r LEFT JOIN FETCH r.regions")
    List<Roles> findAllFetchRegions();

    @Query("SELECT DISTINCT r FROM Roles r LEFT JOIN FETCH r.users")
    Set<Roles> findAllFetchUsers();
}
