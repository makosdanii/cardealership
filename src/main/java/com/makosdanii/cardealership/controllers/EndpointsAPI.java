/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.controllers;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.SimpleDateFormat;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author user
 */
class TodoItem implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String description;
    private Users user;
    private Boolean isCompleted = false;
    private String timeStamp;

    @JsonSerialize
    private List<Store> shipment = new ArrayList<>();

    public TodoItem(int id, String description, Users user, Store store) {
        this.id = id;
        this.description = description;
        this.user = user;
        this.shipment.add(store);
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;

        if (isCompleted) {
            timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

        }
    }

    public Store getShipment(int id) {
        return this.shipment.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(new Store());
    }

    public void setShipment(Store shipment) {
        this.shipment.add(shipment);
    }

    public void setShipment(Users user) {
        for (int i = 0; i < this.shipment.size(); i++) {
            this.shipment.get(i).setUser(user);
        }
    }

    public String getTimeStamp() {
        return timeStamp;
    }

}

@Controller(value = "api")
@ResponseBody
public class EndpointsAPI {

    private final UserService us;
    private final RoleService ros;
    private final RegionService res;
    private final BrandService bs;
    private final StoreService ss;

    private Users user;
    private List<TodoItem> todos = new ArrayList<>();

    public EndpointsAPI(UserService us, RoleService ros,
            RegionService res, BrandService bs, StoreService ss) {
        this.us = us;
        this.ros = ros;
        this.res = res;
        this.bs = bs;
        this.ss = ss;
    }

    @PostConstruct
    public void init() {
        user = us.findUserFetchStores("globalmanager@mydomain.com");
        fillTodos();
    }

    private void fillTodos() {
        todos.add(
                new TodoItem(1, "Make a new user for Xin Sao",
                        us.findUserFetchStores("globalmanager@mydomain.com"),
                        new Store("Skyline",
                                new Users(), bs.findbyBrandName("Nissan"),
                                0)));
        todos.add(
                new TodoItem(2, "Make a new user for Master Yi",
                        us.findUserFetchStores("globalmanager@mydomain.com"),
                        new Store("Skyline",
                                new Users(), bs.findbyBrandName("Nissan"),
                                0)));
        todos.add(
                new TodoItem(3, "Make a new user for Master Yi",
                        us.findUserFetchStores("globalmanager@mydomain.com"),
                        new Store("Skyline",
                                new Users(), bs.findbyBrandName("Nissan"),
                                0)));
        todos.get(1).setIsCompleted(true);
    }

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

    @GetMapping(path = "/todos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> todos(@RequestParam(required = false, value = "username") String username) {
        if (!us.managerInstance().equals(user)) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
        }

        JSONArray responseArr = new JSONArray();
        todos.stream()
                .filter(todo -> (username == null || username.isEmpty())
                ? true
                : todo.getUser().getName().equals(username))
                .forEach(todo
                        -> responseArr.put(new JSONObject(String
                        .format("{'id':%d, 'description':%s, isComplete:%b}",
                                todo.getId(),
                                todo.getDescription(),
                                todo.getIsCompleted())).toMap()));
        return new ResponseEntity<>(responseArr.toList(), HttpStatus.OK);
    }

    @GetMapping("/todo-step")
    public TodoItem todoStep(@RequestParam(required = true, value = "id") Integer id,
            @RequestParam(required = false, value = "completed") Boolean completed) {
        TodoItem todo = todos.stream().filter(t -> t.getId() == id).findFirst().get();

        if (todo == null || (!todo.getUser().equals(user) && !us.managerInstance().equals(user))) {
            return null;
        }

        if (completed != null && completed) {
            todo.setIsCompleted(true);
        }
        return todo;
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
        Roles role = ros.findbyId(Integer.parseInt(json.get("roleId")));
        if (role == null || role.getRoleName().isEmpty()) {
            return "Error picking role";
        }

        Users u;
        if (!json.get("id").isEmpty()) {
            u = new Users(
                    Integer.parseInt(json.get("id")),
                    json.get("email"),
                    json.get("name"),
                    Boolean.parseBoolean(json.get("driverslicense")),
                    json.get("password"),
                    role);
        } else {
            u = new Users(
                    json.get("email"),
                    json.get("name"),
                    Boolean.parseBoolean(json.get("driverslicense")),
                    json.get("password"),
                    role);
        }

        if (json.containsKey("shipmentIdx2Assign")) {
            int idx = Integer.parseInt(json.get("shipmentIdx2Assign"));
            int id = -1;
            if ((id = us.addUser(u)) != -1) {
                String info = "but failed to be assigned";
                if (this.todos.size() > idx) {
                    todos.stream()
                            .filter(t -> t.getId() == idx)
                            .findFirst()
                            .get().setShipment(u);
                    info = "and assigned - id: ";
                }
                return "User added " + info + String.valueOf(id);
            } else {
                return "Email is already registered";
            }
        }
        return us.addUser(u) != -1 ? "User added" : "Email is already registered";
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
    public List<Brand> brand(@RequestParam(required = false, value = "role") Integer role) {

        if (user == null) {
            return new ArrayList<Brand>();
        }

        if (role != null && us.managerInstance().equals(user)) {
            return ros.findBrandsofRole(ros.findbyId(role).getRoleName());
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
                ? ros.listStoresOfRole(user.getRole().getId())
                : new ArrayList<>(ss.findByUser(this.user));
    }

    @GetMapping("/updateStore")
    public String updateStore(@RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "brand") String brand,
            @RequestParam(name = "model") String model,
            @RequestParam(name = "quantity") Integer quantity) {
        if (user == null) {
            return "You need to be logged in";
        }

        Store store;
        if (id == null) {
            store = new Store(model, user, bs.findById(Integer.parseInt(brand)), quantity);
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

    @GetMapping("/deleteStore")
    public String deleteStore(@RequestParam(name = "id") Integer id) {
        if (user == null) {
            return "You need to be logged in";
        }
        if (!user.equals(us.managerInstance()) //check for unauthorized modification
                && !user.equals(ss.findById(id).getUser())) {
            return "Unauthorized modification";
        }
        return us.deleteItsStore(this.user, id);
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
    public String addRoles(
            @RequestParam(name = "roleName") String roleName) {
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

        if (regionId == null) {
            return ros.deleteRole(id);
        }

        Roles role = ros.listRolesFetchRegions().stream().filter(_role -> _role.getId() == id).findFirst().get();
        role.getRegions().remove(res.findById(regionId));
        ros.save(role);
        return "Roles updated";
    }
}
