/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.beans;

import controller.Queries;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import model.Item;

/**
 *
 * @author fiktivni
 */
@Named(value = "itemsBean")
@Dependent
public class ItemsBean {
    
    private Item item;

    /**
     * Creates a new instance of ItemsBean
     */
    public ItemsBean() {
        item = new Item();
        item.setRate(Queries.getRates().get(0));
        item.setTitle("");
        item.setCode("");
        item.setPrice(0);
        
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    
}
