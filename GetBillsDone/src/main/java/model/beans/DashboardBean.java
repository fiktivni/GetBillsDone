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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import model.Invoice;
import model.Item;
import model.Person;

@ManagedBean(name = "dashboardBean")
@ViewScoped
public class DashboardBean implements Serializable {

    private static List<Invoice> invoices;
    private static List<Person> persons;
    private static List<Item> items;

    private String logedID = "0";

    @PostConstruct
    public void init() {
        HttpSession s = HttpSessionUtil.getSession();

        if (s != null) {
            setLogedID((s.getAttribute("logedid").toString()));
        }

        invoices = Queries.getInvoices(Integer.parseInt(logedID));

    }

    public List<Item> getItems() {
        items = Queries.getItemsAtAccountId(logedID);
        return items;
    }

    public void setItems(List<Item> items) {
        DashboardBean.items = items;
    }

    public List<Invoice> getInvoices() {
        return invoices;
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

}
