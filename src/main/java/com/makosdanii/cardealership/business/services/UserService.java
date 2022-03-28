/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.business.services;

import com.makosdanii.cardealership.data.entities.Roles;
import com.makosdanii.cardealership.data.entities.Store;
import com.makosdanii.cardealership.data.entities.Users;
import com.makosdanii.cardealership.data.repositories.RoleRepo;
import com.makosdanii.cardealership.data.repositories.UserRepo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author user
 */
@Service
public class UserService {

    private final UserRepo ur;
    private final RoleRepo rr;

    @Autowired
    public UserService(UserRepo ur, RoleRepo rr) {
        this.ur = ur;
        this.rr = rr;
    }

    public Users findUser(String email) {
        List<Users> u = ur.findByEmail(email);
        return u.isEmpty() ? new Users() : u.get(0);
    }

    public List<Users> listUsers() {
        return ((List<Users>) ur.findAll()).stream()
                .filter(u -> !u.getRole().getRoleName().equals("admin"))
                .collect(Collectors.toList());
    }

    public boolean addUser(Users user) {
        if (!ur.existsByEmail(user.getEmail())) {
            ur.save(user);
            return true;
        }
        return false;
    }

    public Set<Users> searchUser(String infix) {
        Set<Users> result = new HashSet<>();
        Collection<? extends Users> var = ur.findByEmailContaining(infix);

        result.addAll(var);
        Collection<? extends Users> var2 = ur.findByNameContaining(infix);
        result.addAll(var2);

        Collection<? extends Roles> var3 = rr.findByRoleNameContaining(infix);
        var3.stream()
                .filter(role -> role.getRoleName().contains(infix))
                .forEach(role -> result.addAll(role.getUsers()));

        return result;
    }

    @Transactional
    public Set<Store> getUserStore(int id) {
        Users u = ur.findById(id).get();
        if (u == null) {
            return new HashSet<>();
        }
        return u.getStore();
    }

    public Users findbyId(int id) {
        return ur.findById(id).orElse(new Users());
    }

    public boolean deleteUser(int id) {
        if (!ur.existsById(id)) {
            return false;
        }
        ur.deleteById(id);
        return true;

    }

}
