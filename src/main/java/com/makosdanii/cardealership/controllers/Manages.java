/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.controllers;

import com.makosdanii.cardealership.business.services.RegionService;
import com.makosdanii.cardealership.business.services.RoleService;
import com.makosdanii.cardealership.data.entities.Region;
import com.makosdanii.cardealership.data.entities.Roles;
import com.makosdanii.cardealership.data.entities.Store;
import com.makosdanii.cardealership.data.entities.Users;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.event.RowEditEvent;
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
    private final Stores stores;

    private List<Roles> roles;
    private List<Region> availableRegions;

    private boolean newRowOpened = false;
    private String UNDEFINED = "Undefined";

    @Autowired
    public Manages(RegionService res, RoleService ros, Stores stores) {
        this.res = res;
        this.ros = ros;
        this.stores = stores;
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

    public boolean isNewRowOpened() {
        return newRowOpened;
    }

    public void setNewRowOpened(boolean newRowOpened) {
        this.newRowOpened = newRowOpened;
    }

    public void selectedOptionsChanged() {
        newRowOpened = !roles.stream().allMatch(role -> !role.getRoleName().equals(UNDEFINED));
        HashSet<Roles> unique = new HashSet(roles);

        unique.stream()
                .filter(role -> !role.getRoleName()
                .equals(UNDEFINED)).forEach(ros::save);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Changed", null));
        stores.init();
        if (unique.size() != roles.size()) {
            newRowOpened = true;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Some duplicates cannot be saved", null));
        }
    }

    @PostConstruct
    public void init() {
        String PU_NAME = "com.makosdanii_cardealership_jar_0.0.1-SNAPSHOTPU";
        EntityManagerFactory emf = Persistence
                .createEntityManagerFactory(PU_NAME);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Region> q = em
                .createQuery("SELECT DISTINCT r FROM Region r LEFT JOIN FETCH r.roles",
                        Region.class);
        availableRegions = q.getResultList();

        TypedQuery<Roles> qq = em
                .createQuery("SELECT DISTINCT r FROM Roles r LEFT JOIN FETCH r.regions",
                        Roles.class);
        roles = qq.getResultList();

        em.getTransaction().commit();
        em.close();
    }

    public void add() {
        if (newRowOpened) {
            return;
        }
        roles.add(new Roles(UNDEFINED));
        newRowOpened = true;
    }

    public void save(AjaxBehaviorEvent event) {
        FacesMessage msg;
        InputText query = (InputText) event.getSource();
        String text = (String) query.getValue();
        if (!text.equals(UNDEFINED)) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Changed", null);
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Set role name", null);
        }
    }
}
