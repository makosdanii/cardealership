/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.data.repositories;

import com.makosdanii.cardealership.data.entities.Users;
import java.util.List;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public interface UserRepo extends CrudRepository<Users, Integer> {

    public List<Users> findByEmail(String email);

    public boolean existsByEmail(String email);

    public Set<Users> findByEmailContaining(String infix);

    public Set<Users> findByNameContaining(String infix);

}
