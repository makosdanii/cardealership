/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.business.services;

import com.makosdanii.cardealership.data.entities.Brand;
import com.makosdanii.cardealership.data.entities.Region;
import com.makosdanii.cardealership.data.entities.Roles;
import com.makosdanii.cardealership.data.entities.Users;
import com.makosdanii.cardealership.data.repositories.RegionRepo;
import com.makosdanii.cardealership.data.repositories.RoleRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author user
 */
@Service
public class RoleService {

    private final RoleRepo ror;

    @Autowired
    public RoleService(RoleRepo rr) {
        this.ror = rr;
    }

    public List<Roles> listRoles() {
        return ((List<Roles>) ror.findAll()).stream()
                .filter(u -> !u.getRoleName().equals("admin"))
                .collect(Collectors.toList());
    }

    public Set<Roles> findRoles(String role_name) {
        return ror.findByRoleName(role_name);
    }

    public Roles findbyId(int id) {
        return ror.findById(id).orElse(new Roles());
    }

    public Set<Users> findUsersOfRole(String role_name) {
        Set<Roles> roles = ror.findByRoleName(role_name);
        return roles.isEmpty() ? null : roles.iterator().next().getUsers();
    }

    public List<Region> findRegionsofRole(String role_name) {
        Set<Roles> roles = ror.findByRoleName(role_name);
        return roles.isEmpty() ? null : roles.iterator().next().getRegions();
    }

    public List<Brand> findBrandsofRole(String role_name) {
        List<Region> regions = findRegionsofRole(role_name);
        List<Brand> result = new ArrayList<Brand>();
        regions.forEach(reg -> result.addAll(reg.getBrands()));

        return result;
    }

}
