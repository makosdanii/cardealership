/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.controllers;

import com.makosdanii.cardealership.business.services.BrandService;
import com.makosdanii.cardealership.business.services.RegionService;
import com.makosdanii.cardealership.business.services.RoleService;
import com.makosdanii.cardealership.business.services.StoreService;
import com.makosdanii.cardealership.business.services.UserService;
import com.makosdanii.cardealership.data.entities.Brand;
import com.makosdanii.cardealership.data.entities.Region;
import com.makosdanii.cardealership.data.entities.Roles;
import com.makosdanii.cardealership.data.entities.Store;
import com.makosdanii.cardealership.data.entities.Users;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author user
 */
@Controller(value = "api")
@ResponseBody
public class EndpointsAPI {

    private final UserService us;
    private final RoleService ros;
    private final RegionService res;
    private final BrandService bs;
    private final StoreService ss;

    private Users user;

    public EndpointsAPI(UserService us, RoleService ros,
            RegionService res, BrandService bs, StoreService ss) {
        this.us = us;
        this.ros = ros;
        this.res = res;
        this.bs = bs;
        this.ss = ss;

    }

//    @PostConstruct
//    public void init() {
//        user = us.findUserFetchStores("globalmanager@mydomain.com");
//    }
    @GetMapping("/whoami")
    public String whoami() {
        return user == null ? "nobody" : user.getName();
    }

    @PostMapping("/authenticate")
    public String authenticate(
            @RequestBody(required = true) Map<String, String> json
    ) {
        Users u = this.us.findUserFetchStores(json.get("email"));

        if (u.getRole() == null) {
            return "Unregistered email";
        }
        if (!u.getPassword().equals(json.get("password"))) {
            return "Wrong password";
        }
        this.user = u;
        return "Success";
    }

    @GetMapping("/logout")
    public String logout() {
        this.user = null;
        return whoami();
    }

    @GetMapping("/users")
    public List<Users> users(Model model) {
        if (!us.managerInstance().equals(user)) {
            return new ArrayList<Users>();
        }
        return us.listUsers();
    }

    @PostMapping("/createDealer")
    public String createDealer(@RequestBody(required = true) Map<String, String> json) {
        if (!us.managerInstance().equals(user)) {
            return "Unauthorized";
        }
        String num = json.get("roleId");
        Roles role = ros.findbyId(Integer.parseInt(json.get("roleId")));
        if (role.getRoleName().isEmpty()) {
            return "Error picking role";
        }
        return us.addUser(new Users(
                json.get("email"),
                json.get("name"),
                Boolean.parseBoolean(json.get("driverslicense")),
                json.get("password"),
                role)) ? "User added" : "Email is already registered";
    }

    @GetMapping("/deleteDealer")
    public String deleteDealer(@RequestParam(value = "id") Integer id) {
        if (!us.managerInstance().equals(user)) {
            return "Unauthorized";
        }
        ss.returnStore(us.findbyId(id), us.managerInstance());
        return us.deleteUser(id) ? "User deleted" : "Invalid identifier";
    }

    @GetMapping("/roles")
    public List<Roles> roles() {

        if (!us.managerInstance().equals(user)) {
            return new ArrayList<Roles>();
        }
        return ros.listRoles();
    }

    @GetMapping("/roles4regions")
    public List<Roles> roles4Regions() {

        if (!us.managerInstance().equals(user)) {
            return new ArrayList<Roles>();
        }
        return ros.listRolesFetchRegions();
    }

    @GetMapping("/region")
    public List<Region> region() {

        if (!us.managerInstance().equals(user)) {
            return new ArrayList<Region>();
        }
        return res.listRegionsFetchRoles();
    }

    @GetMapping("/brand")
    public List<Brand> brand() {

        if (user == null) {
            return new ArrayList<Brand>();
        }
        return ros.findBrandsofRole(
                user.getRole().getRoleName());
    }

    @GetMapping("/stores")
    public List<Store> stores() {

        if (user == null) {
            return new ArrayList<Store>();
        }
        return user.equals(us.managerInstance())
                ? ss.listStoresOfRole(user.getRole().getId())
                : new ArrayList<>(user.getStore());
    }

    @GetMapping("/updateStore")
    public String updateStore(@RequestParam(name = "id") Integer id,
            @RequestParam(name = "brand") String brand,
            @RequestParam(name = "model") String model,
            @RequestParam(name = "quantity") Integer quantity) {
        if (user == null) {
            return "You need to be logged in";
        }

        Store store;
        if (id < 0) { //client-side indexing for new records
            store = new Store(model, user, bs.findbyBrandName(brand), quantity);
            user = us.findUserFetchStores(user.getEmail());
        } else {
            if (!user.equals(us.managerInstance()) //check for unauthorized modification
                    && !user.equals(ss.findById(id).getUser())) {
                return "Unauthorized modification";
            }

            store = new Store(id, user, bs.findbyBrandName(brand), model, quantity);
        }

        return ss.addToStore(store) ? "Store updated" : "Unauthorized modification";
    }

    @GetMapping("/updateRoles")
    public String updateRoles(@RequestParam(name = "id") Integer id,
            @RequestParam(name = "regionId", required = false) Integer regionId
    ) {
        if (!user.equals(us.managerInstance())) {
            return "Unauthorized";
        }
        Roles role = ros.listRolesFetchRegions().stream().filter(_role -> _role.getId() == id).findFirst().get();
        role.getRegions().add(res.findById(regionId));
        ros.save(role);
        return "Roles updated";
    }

    @GetMapping("/addRoles")
    public String addRoles(@RequestParam(name = "id") Integer id,
            @RequestParam(name = "roleName", required = false) String roleName) {
        if (!user.equals(us.managerInstance())) {
            return "Unauthorized";
        }

        Roles role = new Roles(roleName);

        ros.save(role);
        return "Roles updated";
    }

    @GetMapping("/deleteRoles")
    public String deleteRoles(@RequestParam(name = "id") Integer id,
            @RequestParam(name = "regionId", required = false) Integer regionId
    ) {
        if (!user.equals(us.managerInstance())) {
            return "Unauthorized";
        }
        Roles role = ros.listRolesFetchRegions().stream().filter(_role -> _role.getId() == id).findFirst().get();
        role.getRegions().remove(res.findById(regionId));
        ros.save(role);
        return "Roles updated";
    }
}
