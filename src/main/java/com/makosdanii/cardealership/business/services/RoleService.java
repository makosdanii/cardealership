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
import java.util.Optional;
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
    private final RegionService rs;

    @Autowired
    public RoleService(RoleRepo rr, RegionService rs) {
        this.ror = rr;
        this.rs = rs;
    }

    public List<Roles> listRoles() {
        return ((List<Roles>) ror.findAll()).stream()
                .filter(u -> !u.getRoleName().equals("admin"))
                .collect(Collectors.toList());
    }

    public List<Roles> listRolesFetchRegions() {
        return ((List<Roles>) ror.findAllFetchRegions()).stream()
                .filter(u -> !u.getRoleName().equals("admin"))
                .collect(Collectors.toList());
    }

    public List<Roles> findRoles(String role_name) {
        return ror.findByRoleName(role_name);
    }

    public Roles findbyId(int id) {
        return ror.findById(id).orElse(new Roles());
    }

    public List<Users> findUsersOfRole(String role_name) {
        Optional<Roles> roles = ror.findAllFetchUsers()
                .stream()
                .filter(role -> role.getRoleName().equals(role_name))
                .findFirst();
        return roles.isPresent() ? roles.get().getUsers() : null;
    }

    public List<Region> findRegionsofRole(String role_name) {
        Optional<Roles> roles = ror.findAllFetchRegions()
                .stream()
                .filter(role -> role.getRoleName().equals(role_name))
                .findFirst();
        return roles.isPresent() ? roles.get().getRegions() : null;
    }

    public List<Brand> findBrandsofRole(String role_name) {
        List<Region> regions = findRegionsofRole(role_name);
        List<Brand> result = new ArrayList<Brand>();
        regions.forEach(region -> result
                .addAll(rs.findBrandsofRegion(region.getRegionName())));

        return result;
    }

    public void save(Roles role) {
        ror.save(role);
    }

    public boolean notExists(String roleName) {
        return ror.findByRoleName(roleName).isEmpty();
    }
}
