/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.beans;

import controller.Queries;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.validation.constraints.AssertTrue;
import model.Account;
import model.Person;

/**
 *
 * @author fiktivni
 */
@SessionScoped
@Named("registrationBean")
public class RegistrationBean implements Serializable{
    
    @AssertTrue
    private boolean agreed;
    private Account account;
    private Person user;
    private String controlPassword;

    @PostConstruct
    public void init(){
        agreed = false;
        account = new Account();
        user = new Person();
        user.setIsowner(true);
        controlPassword = "";
    }
    
    public String registerNewUser() {
        account.setId(Queries.createAccount(account));
        user.setAccountIdaccount(account.getId());
        user.setEmail(account.getEmail());
        Queries.createPerson(user);
        return "index";
    }
    
    public boolean isAgreed() {
        return agreed;
    }

    public void setAgreed(boolean agreed) {
        this.agreed = agreed;
    }
    
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public String getControlPassword() {
        return controlPassword;
    }

    public void setControlPassword(String controlPassword) {
        this.controlPassword = controlPassword;
    }
    
}
