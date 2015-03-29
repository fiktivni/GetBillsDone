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
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import model.Account;
import model.Person;

/**
 *
 * @author fiktivni
 */
@Named("settingsBean")
@SessionScoped
public class SettingsBean implements Serializable {

    @Inject
    DashboardBean dashboard;

    private Person user, editedUser = new Person();
    private Account account;

    @PostConstruct
    public void init() {
        
        String sessionId = dashboard.getLogedID();
        
        account = Queries.getAccount(sessionId);
        
        user = dashboard.getUser();
        
        editedUser.setAccountIdaccount(user.getAccountIdaccount());
        editedUser.setBankaccount(user.getBankaccount());
        editedUser.setCity(user.getCity());
        editedUser.setCompany(user.getCompany());
        editedUser.setDic(user.getDic());
        editedUser.setEmail(user.getEmail());
        editedUser.setFax(user.getFax());
        editedUser.setHouse(user.getHouse());
        editedUser.setIco(user.getIco());
        editedUser.setIsowner(user.getIsowner());
        editedUser.setLastname(user.getLastname());
        editedUser.setLocked(false);
        editedUser.setName(user.getName());
        editedUser.setPcode(user.getPcode());
        editedUser.setPhone(user.getPhone());
        editedUser.setState(user.getState());
        editedUser.setStreet(user.getStreet());
        editedUser.setWww(user.getWww());
    }

    public void updateUser() {
        user.setLocked(true);
        Queries.updatePerson(user);
        Queries.createPerson(editedUser);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Nastavení aktualizována"));
    }

    public String deleteUser() {
        Queries.deleteAccount(account);
        Queries.deletePerson(user);
        return logout();
    }

    private String logout() {
        HttpSessionUtil.getSession().invalidate();
        return "index";
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Person getEditedUser() {
        return editedUser;
    }

    public void setEditedUser(Person editedUser) {
        this.editedUser = editedUser;
    }

}
