/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.beans;

import controller.Queries;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import model.Person;

/**
 *
 * @author fiktivni
 */
@SessionScoped
@ManagedBean(name = "contactBean")
public class ContactBean implements Serializable {

    @Inject
    DashboardBean dashboard;
    
    private Person contact;
    private int userID;

    @PostConstruct
    public void init() {
        userID = Integer.parseInt(dashboard.getLogedID());
        contact = new Person();
        contact.setAccountIdaccount(userID);
        contact.setIsowner(false);
    }

    public void saveContact() {
        if (contact.getId() == null) {
            Queries.createPerson(contact);
        } else {
            Queries.updatePerson(contact);
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Uloženo"));
        refreshContacts();
    }

    public String deleteContact() {
        Queries.deletePerson(contact);
        refreshContacts();
        return "contacts";
    }

    public Person getContact() {
        return contact;
    }

    public void setContact(Person contact) {
        this.contact = contact;
    }

    private void refreshContacts() {
        dashboard.setPersons(Queries.getPersonsAtAccountId(userID+""));
    }

}
