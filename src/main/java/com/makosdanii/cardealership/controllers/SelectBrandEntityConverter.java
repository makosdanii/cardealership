/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.makosdanii.cardealership.controllers;

import com.makosdanii.cardealership.business.services.BrandService;
import com.makosdanii.cardealership.data.entities.Brand;
import com.makosdanii.cardealership.data.entities.Store;
import com.makosdanii.cardealership.data.repositories.BrandRepo;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

/**
 *
 * @author user
 */
//@FacesConverter(forClass = Store.class, value = "selectEntityConverter")
@Controller
public class SelectBrandEntityConverter implements Converter {

    private final BrandService br;

    @Autowired
    public SelectBrandEntityConverter(BrandService br) {
        super();
        this.br = br;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        if (modelValue == null) {
            return ""; // Never return null here!
        }

        if (modelValue instanceof Brand) {
            return String.valueOf(((Brand) modelValue).getId());
        } else {
            throw new ConverterException(new FacesMessage(modelValue + " store closed"));
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.isEmpty()) {
            return null;
        }

        try {
            return br.findById(Integer.valueOf(submittedValue));
        } catch (NumberFormatException e) {
            throw new ConverterException(new FacesMessage(submittedValue + " store closed"), e);
        }
    }
}
