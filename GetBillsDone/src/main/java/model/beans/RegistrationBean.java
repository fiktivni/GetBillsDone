/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.beans;

import controller.Queries;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    private List<String> usedLogins;

    @PostConstruct
    public void init(){
        agreed = false;
        account = new Account();
        user = new Person();
        user.setIsowner(true);
        controlPassword = "";
        usedLogins = Queries.getUsedLogins();
    }
    
    public void registerNewUser() {
        for(String usedLogin : usedLogins){
            if(usedLogin == null ? account.getEmail() == null : usedLogin.equals(account.getEmail())){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Rezervovaný email!", "Tato emailová adresa je již používána"));
                return;
            }
        }
        account.setId(Queries.createAccount(account));
        user.setLocked(false);
        user.setAccountIdaccount(account.getId());
        user.setEmail(account.getEmail());
        Queries.createPerson(user);
        init();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Účet vytvořen"));
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
