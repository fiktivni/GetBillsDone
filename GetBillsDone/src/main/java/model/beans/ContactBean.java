/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.beans;

import controller.HttpSessionUtil;
import controller.Queries;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import model.Person;
import org.primefaces.context.RequestContext;

/**
 *
 * @author fiktivni
 */
@Named("contactBean")
@SessionScoped
public class ContactBean implements Serializable {

    private Person contact;

    @PostConstruct
    public void init() {
        int userID = getUserID();
        contact = new Person();
        contact.setAccountIdaccount(userID);
        contact.setIsowner(false);
    }
    
    public String clear(){
        init();
        RequestContext.getCurrentInstance().update("form:grid");
        return "contact";
    }

    public void saveContact() {
        if (contact.getId() == null) {
            Queries.createPerson(contact);
        } else {
            Queries.updatePerson(contact);
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Uloženo"));
    }
    
    public void newUser() {
        contact = new Person();
        contact.setAccountIdaccount(getUserID());
        contact.setIsowner(false);
    }

    public String deleteContact() {
        Queries.deletePerson(contact);
        return "contacts";
    }

    public Person getContact() {
        return contact;
    }

    public void setContact(Person contact) {
        this.contact = contact;
    }

    private int getUserID() {
        HttpSession s = HttpSessionUtil.getSession();
        int userID = -1;
        if (s != null) {
            userID = Integer.parseInt((s.getAttribute("logedid").toString()));
        }
        return userID;
    }

}
