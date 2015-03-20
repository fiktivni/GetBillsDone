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
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import model.Invoice;
import model.Person;

/**
 *
 * @author tempest
 */
@SessionScoped
@Named("invoiceWizardBean")
public class InvoiceWizardBean implements Serializable{

    private Invoice invoice = new Invoice();
    private Person customer = new Person();
    private final int userId = getUserID();
    private List<Person> contacts = new ArrayList<>();
    private String customerTabOption = "search";
    
    @PostConstruct
    public void init() {
        invoice = new Invoice();
        customer = new Person();
        contacts = Queries.getPersonsAtAccountId("" + userId);
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Person getCustomer() {
        return customer;
    }

    public void setCustomer(Person customer) {
        this.customer = customer;
    }

    public List<Person> getContacts() {
        return contacts;
    }

    public void setContacts(List<Person> contacts) {
        this.contacts = contacts;
    }

    public String getCustomerTabOption() {
        return customerTabOption;
    }

    public void setCustomerTabOption(String customerTabOption) {
        this.customerTabOption = customerTabOption;
    }

    private int getUserID() {
        HttpSession s = HttpSessionUtil.getSession();
        int userID = -1;
        if (s != null) {
            userID = Integer.parseInt((s.getAttribute("logedid").toString()));
        }
        return userID;
    }
    
    public List<Person> completeContact(String query) {

        List<Person> found = new ArrayList<>();
        int validResultsCount = 0;

        for (Person person : contacts) {
            if (person.getWholename().toLowerCase().contains(query)) {
                if (!Objects.equals(person.getId(), customer.getId())) {
                    validResultsCount++;
                    if (validResultsCount <= 10) {
                        found.add(person);
                    }
                }
            }
        }

        return found;
    }

}
