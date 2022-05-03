/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.business.services;

import com.makosdanii.cardealership.controllers.Stores;
import com.makosdanii.cardealership.data.entities.Region;
import com.makosdanii.cardealership.data.entities.Store;
import com.makosdanii.cardealership.data.entities.StoreKeys;
import com.makosdanii.cardealership.data.entities.Roles;
import com.makosdanii.cardealership.data.entities.Users;
import com.makosdanii.cardealership.data.repositories.StoreRepo;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author user
 */
@Service
public class StoreService {

    private final StoreRepo sr;
    private final RoleService rs;

    @Autowired
    public StoreService(StoreRepo sr, RoleService rs) {
        this.sr = sr;
        this.rs = rs;
    }

    public Store findById(Integer id) {
        return sr.findById(id).orElse(new Store());
    }

    public Set<Store> listStores() {
        return (Set<Store>) sr.findAll();
    }

    public List<Store> listStoresOfRole(Integer roleId) {
        List<Region> regions = rs.listRolesFetchRegions()
                .stream()
                .filter(role -> role.getId() == roleId)
                .collect(Collectors.toList())
                .get(0).getRegions();
        return listStores()
                .stream()
                .filter(store -> regions
                .contains(store.getBrand().getRegion()))
                .collect(Collectors.toList());
    }

    public boolean addToStore(Store store) {
        String roleid = store.getUser().getRole().getRoleName();

        //check whether the region of the brand which is to be added to store 
        //is in the list of the regions managed by the dealer
        if (rs.findBrandsofRole(roleid)
                .stream()
                .filter(r -> r.getId() == store.getBrand().getId())
                .collect(Collectors.toList())
                .isEmpty()) {
            return false;
        }

        sr.save(store);
        return true;
    }

    public void returnStore(Users from, Users to) {
        Set<Store> stores = sr.findByUser(from);
        stores.forEach(store -> {
            store.setUser(to);
            sr.save(store);
        });
    }

}
