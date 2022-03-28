/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.controllers;

import com.makosdanii.cardealership.business.services.RegionService;
import com.makosdanii.cardealership.business.services.RoleService;
import com.makosdanii.cardealership.business.services.StoreService;
import com.makosdanii.cardealership.business.services.UserService;
import com.makosdanii.cardealership.data.entities.Region;
import com.makosdanii.cardealership.data.entities.Roles;
import com.makosdanii.cardealership.data.entities.Store;
import com.makosdanii.cardealership.data.entities.Users;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.component.inputtext.InputText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.RequestScope;

/**
 *
 * @author user
 */
@RequestScope
@Controller("data")
public class Data {

    private final UserService us;
    private final RoleService ros;
    private final RegionService res;
    private final StoreService ss;

    private List<Users> users;
    private List<Roles> roles;
    private List<Region> regions;

    private String email;
    private String name;
    private Boolean driverslicense = false;
    private String password;
    private int role;

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDriverslicense() {
        return driverslicense;
    }

    public void setDriverslicense(Boolean driverslicense) {
        this.driverslicense = driverslicense;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Autowired
    public Data(UserService us, RoleService ros, RegionService res, StoreService ss) {
        this.us = us;
        this.ros = ros;
        this.res = res;
        this.ss = ss;
    }

    @PostConstruct
    public void init() {
        users = us.listUsers();
        roles = ros.listRoles();
    }

    public void addUser() {
        FacesMessage message;
        Roles role = ros.findbyId(this.role);
        if (role.getRoleName().isEmpty()) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unsuccessful", null);
            FacesContext.getCurrentInstance().addMessage("email2", message);
            return;
        }

        if (us.addUser(new Users(email, name, driverslicense, password, role))) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", null);
            users = us.listUsers();
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unsuccessful", null);
        }
        FacesContext.getCurrentInstance().addMessage("email2", message);

    }

    public void deleteUser() {
        HttpServletRequest query = (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequest();

        FacesMessage msg;
        if (us.deleteUser(Integer.valueOf(query.getParameter("id")))) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted", null);
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cannot be deleted", null);
        }

        FacesContext.getCurrentInstance().addMessage("msgs", msg);
    }

    public void onSearchInputChanged(AjaxBehaviorEvent event) {
        InputText query = (InputText) event.getSource();
        String text = (String) query.getValue();
        users = new ArrayList<Users>(us.searchUser((String) query.getValue()));
        return;
    }

}
