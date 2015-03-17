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
import model.Item;
import org.primefaces.context.RequestContext;

/**
 *
 * @author fiktivni
 */
@Named("itemBean")
@SessionScoped
public class ItemBean implements Serializable{
    
    private Item item;

    @PostConstruct
    public void init() {
        item = new Item();
        item.setAccountIdaccount(getUserID());
        item.setTitle("");
        item.setCode("");
        item.setTaxRate(Queries.getRates().get(0).getValue());
        item.setNetPrice(0);
        item.setFullPrice(0);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    
    public String saveItem(){
        if (item.getId() == null) {
            Queries.createItem(item);
        } else {
            Queries.updateItem(item);
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Uloženo"));
        
        return "items";
    }    
    
    public String deleteItem(){
        Queries.deleteItem(item);
        return "items";
    }
    
    public String clear(){
        init();
        RequestContext.getCurrentInstance().update("form:grid");
        return "item";
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
