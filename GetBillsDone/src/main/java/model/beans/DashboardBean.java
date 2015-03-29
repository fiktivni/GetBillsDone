/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.beans;

import controller.HttpSessionUtil;
import controller.Queries;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import model.Invoice;
import model.Item;
import model.Method;
import model.Person;

@SessionScoped
@Named("dashboardBean")
public class DashboardBean implements Serializable {

    private List<Invoice> invoices;
    private List<Person> persons;
    private List<Person> unlockedPersons;
    private List<Item> items;
    private List<Item> unlockedItems;
    private List<Method> methods;
    

    private List<Invoice> filteredInvoices;
    private List<Item> filteredItems;
    private List<Person> filteredPersons;
    
    private String logedID = "0";
    private Person user;

    @PostConstruct
    public void init() {
        
        HttpSession s = HttpSessionUtil.getSession();

        if (s != null) {
            setLogedID((s.getAttribute("logedid").toString()));
        }

        invoices = Queries.getInvoices(Integer.parseInt(logedID));
        methods = Queries.getMethods();
        persons = Queries.getPersonsAtAccountId(logedID);
        items = Queries.getItemsAtAccountId(logedID);
        user = Queries.getAccountUser(logedID);

    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    /**
     * @return the persons
     */
    public List<Person> getPersons() {
        return persons;
    }

    /**
     * @param aPersons the persons to set
     */
    public void setPersons(List<Person> aPersons) {
        persons = aPersons;
    }

    /**
     * @return the logedID
     */
    public String getLogedID() {
        return logedID;
    }

    /**
     * @param logedID the logedID to set
     */
    public void setLogedID(String logedID) {
        this.logedID = logedID;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public List<Invoice> getFilteredInvoices() {
        return filteredInvoices;
    }

    public void setFilteredInvoices(List<Invoice> filteredInvoices) {
        this.filteredInvoices = filteredInvoices;
    }

    public List<Item> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<Item> filteredItems) {
        this.filteredItems = filteredItems;
    }

    public List<Person> getFilteredPersons() {
        return filteredPersons;
    }

    public void setFilteredPersons(List<Person> filteredPersons) {
        this.filteredPersons = filteredPersons;
    }
    
    public String getUsersWholeName(int personId){
        for(Person user : persons){
            if (user.getId() == personId)
                return user.getWholename();
        }
        return null;
    }
    
    public String getMethodTitle(int methodId){
        for(Method method : methods){
            if(method.getId() == methodId){
                return method.getTitle();
            }
        }
        return null;
    }
    
    public Method getMethod(int methodId){
        for(Method method : methods){
            if(method.getId() == methodId){
                return method;
            }
        }
        return null;
    }

    public List<Person> getUnlockedPersons() {
        
        unlockedPersons = new ArrayList<>();
        for(Person person : persons){
            if (!person.getLocked())
                unlockedPersons.add(person);
        }
        
        return unlockedPersons;
    }

    public void setUnlockedPersons(List<Person> unlockedPersons) {
        this.unlockedPersons = unlockedPersons;
    }

    public List<Item> getUnlockedItems() {
        
        unlockedItems = new ArrayList<>();
        for(Item item : items){
            if (!item.getLocked())
                unlockedItems.add(item);
        }
        
        return unlockedItems;
    }

    public void setUnlockedItems(List<Item> unlockedItems) {
        this.unlockedItems = unlockedItems;
    }

}
