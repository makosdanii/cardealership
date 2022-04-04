/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.controllers;

import com.makosdanii.cardealership.business.services.RoleService;
import com.makosdanii.cardealership.business.services.StoreService;
import com.makosdanii.cardealership.business.services.UserService;
import com.makosdanii.cardealership.data.entities.Brand;
import com.makosdanii.cardealership.data.entities.Store;
import com.makosdanii.cardealership.data.entities.StoreKeys;
import com.makosdanii.cardealership.data.entities.Users;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

/**
 *
 * @author user
 */
@Controller("stores")
@SessionScope
public class Stores {

    private final RoleService ros;
    private final UserService us;
    private final StoreService ss;

    private Set<Store> store;
    private List<Brand> availableBrands;

    private Users sessionUser;
    private boolean newRowOpened;

    @Autowired
    public Stores(RoleService ros, UserService us, StoreService ss) {
        this.ros = ros;
        this.us = us;
        this.ss = ss;
    }

    @PostConstruct
    public void init() {
        Object session = FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        if ((HttpSession) session == null) {
            return;
        }

        Object id = ((HttpSession) session).getAttribute("user");
        if (id != null) {
            sessionUser = ((Users) id);

            store = new HashSet<Store>();
            syncStore();

            availableBrands = ros.findBrandsofRole(((Users) id).getRole().getRoleName());
            newRowOpened = availableBrands.isEmpty();
        }

    }

    private void syncStore() {
        store.stream()
                .filter(store -> !store.getModel().equals("Unspecified"))
                .forEach(ss::addToStore);

        if (sessionUser.getRole()
                .getRoleName().equals("global")) {
            store = ss.listStores();
        } else {
            store = sessionUser.getStore();
        }
    }

    public List<Brand> getAvailableBrands() {
        return availableBrands;
    }

    public void setAvailableBrands(List<Brand> availableBrands) {
        this.availableBrands = availableBrands;
    }

    public Set<Store> getStore() {
        return store;
    }

    public void setStore(Set<Store> store) {
        this.store = store;
    }

    public boolean isNewRowOpened() {
        return newRowOpened;
    }

    public void setNewRowOpened(boolean newRowOpened) {
        this.newRowOpened = newRowOpened;
    }

    public void addItem() {
        if (!newRowOpened) {
            store.add(new Store(
                    "Unspecified",
                    sessionUser,
                    availableBrands.get(0), 0));
            newRowOpened = true;
        }
    }

    public void onRowEdit(RowEditEvent<Store> event) {
        FacesMessage msg;
        if (event.getObject().getModel().equals("Unspecified")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell couldn't be saved",
                    "Brand or model not specified");
        } else {

            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell changed", null);
            newRowOpened = false;
            syncStore();
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent<Store> event) {
        if (event.getObject().getModel().equals("Unspecified")) {
            store.remove(event.getObject());
            newRowOpened = false;
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Change cancelled", null);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
