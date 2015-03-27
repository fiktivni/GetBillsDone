/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.beans;

import controller.HttpSessionUtil;
import controller.Queries;
import java.io.Serializable;
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
    private List<Item> items;
    private List<Method> methods;

    private List<Invoice> filteredInvoices;
    
    private String logedID = "0";

    @PostConstruct
    public void init() {
        HttpSession s = HttpSessionUtil.getSession();

        if (s != null) {
            setLogedID((s.getAttribute("logedid").toString()));
        }

        invoices = Queries.getInvoices(Integer.parseInt(logedID));
        methods = Queries.getMethods();

    }

    public List<Item> getItems() {
        items = Queries.getItemsAtAccountId(logedID);
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
        persons = Queries.getPersonsAtAccountId(logedID);
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

}
