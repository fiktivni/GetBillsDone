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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import model.Item;

/**
 *
 * @author fiktivni
 */
@SessionScoped
@ManagedBean(name = "itemBean")
public class ItemBean implements Serializable{
    
    @Inject
    DashboardBean dashboard;
    
    private Item item;
    private int userID;

    @PostConstruct
    public void init() {
        userID = Integer.parseInt(dashboard.getLogedID());
        item = new Item();
        item.setAccountIdaccount(userID);
        item.setTitle("");
        item.setCode("");
        item.setTaxRate(21);
        item.setNetPrice(0);
        item.setFullPrice(0);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    
    public void saveItem(){
        if (item.getId() == null) {
            Queries.createItem(item);
        } else {
            Queries.updateItem(item);
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Uloženo"));
        refreshItems();
    }    
    
    public String deleteItem(){
        Queries.deleteItem(item);
        refreshItems();
        return "items";
    }

    private void refreshItems() {
        dashboard.setItems(Queries.getItemsAtAccountId(userID+""));
    }
}
