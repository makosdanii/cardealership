/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.controllers;

import com.makosdanii.cardealership.business.services.RegionService;
import com.makosdanii.cardealership.business.services.RoleService;
import com.makosdanii.cardealership.data.entities.Region;
import com.makosdanii.cardealership.data.entities.Roles;
import com.makosdanii.cardealership.data.entities.Users;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

/**
 *
 * @author user
 */
@Controller
@SessionScope
public class Manages {

    private final RegionService res;
    private final RoleService ros;

    private List<Roles> roles;
    private List<Region> availableRegions;

    @Autowired
    public Manages(RegionService res, RoleService ros) {
        this.res = res;
        this.ros = ros;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    public List<Region> getAvailableRegions() {
        return availableRegions;
    }

    public void setAvailableRegions(List<Region> availableRegions) {
        this.availableRegions = availableRegions;
    }

    public void selectedOptionsChanged() {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Changed", null));
    }

    public void submitting() {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Changed", null));
    }

    @PostConstruct
    public void init() {
        availableRegions = res.listRegions();
        roles = ros.listRoles();
        return;
//        roles.add(new Roles("Undefined",
//                new ArrayList<Users>(),
//                new ArrayList<Region>()));
    }

}
