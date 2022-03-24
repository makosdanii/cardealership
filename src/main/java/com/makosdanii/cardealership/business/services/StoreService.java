/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.business.services;

import com.makosdanii.cardealership.data.entities.Store;
import com.makosdanii.cardealership.data.entities.StoreKeys;
import com.makosdanii.cardealership.data.repositories.StoreRepo;
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
    private final UserService us;
    private final RoleService rs;

    @Autowired
    public StoreService(StoreRepo sr, UserService us, RoleService rs) {
        this.sr = sr;
        this.us = us;
        this.rs = rs;
    }

    public Store findById(StoreKeys id) {
        return sr.findById(id).orElse(new Store());
    }

    public Set<Store> listStores() {
        return (Set<Store>) sr.findAll();
    }

    public boolean addToStore(Store store) {
        String roleid = us.findbyId(store.getId().getUserId())
                .getRole()
                .getRoleName();

        //check whether the region of the brand which is to be added to store 
        //is in the list of the regions managed by the dealer
        if (rs.findBrandsofRole(roleid)
                .stream()
                .filter(r -> r.getId() == store.getId().getBrandId())
                .collect(Collectors.toList())
                .isEmpty()) {
            return false;
        }

        sr.save(store);
        return true;
    }
}
