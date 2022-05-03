/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.controllers;

import com.makosdanii.cardealership.business.services.UserService;
import com.makosdanii.cardealership.data.entities.Users;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;

/**
 *
 * @author user
 */
@Controller("login")
@RequestScope
public class Login {

    private String email;
    private String password;
    private boolean rememberme;

    private final UserService us;

    @Autowired
    public Login(UserService us) {
        this.us = us;
    }

    public boolean isRememberme() {
        return rememberme;
    }

    public void setRememberme(boolean rememberme) {
        this.rememberme = rememberme;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String signIn() {
        Users u = this.us.findUserFetchStores(email);

        if (u.getRole() == null) {
            FacesContext.getCurrentInstance().addMessage("authform:email",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "User not recognized", null));
            return "unsucessful";
        }
        if (!u.getPassword().equals(this.password)) {
            FacesContext.getCurrentInstance().addMessage("authform:password1",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Wrong password", null));
            return "unsucessful";
        }
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.setAttribute("user", u);

        return "/cardealership/homepage.xhtml";
    }

    public String signOut() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "/cardealership/login.xhtml";

    }
}
