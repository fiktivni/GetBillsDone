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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;
import model.Invoice;
import model.Person;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author tempest
 */
@SessionScoped
@ManagedBean(name = "invoiceWizardBean")
public class InvoiceWizardBean implements Serializable {

    private Invoice invoice = new Invoice();
    private Person customer = new Person();
    private Person address = new Person();
    private final int userId = getUserID();
    private List<Person> contacts = new ArrayList<>();

    private String customerOption;
    private boolean renderSearchCustomer;
    private boolean renderCreateCustomer;
    
    private String addressOption;
    private boolean renderSearchAddress;
    private boolean renderCreateAddress;
    
    private boolean skip;

    @PostConstruct
    public void init() {
        invoice = new Invoice();
        customer = new Person();
        address = new Person();
        contacts = Queries.getPersonsAtAccountId("" + userId);
        
        customerOption = "search";
        renderSearchCustomer = customerOption.equals("search");
        renderCreateCustomer = customerOption.equals("create");
        
        addressOption = "search";
        renderSearchAddress = customerOption.equals("search");
        renderCreateAddress = customerOption.equals("create");
        
        skip = false;
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

    public Person getAddress() {
        return address;
    }

    public void setAddress(Person address) {
        this.address = address;
    }

    public List<Person> getContacts() {
        return contacts;
    }

    public void setContacts(List<Person> contacts) {
        this.contacts = contacts;
    }

    public String getCustomerOption() {
        return customerOption;
    }

    public void setCustomerOption(String customerOption) {
        this.customerOption = customerOption;
    }

    public boolean isRenderSearchCustomer() {
        return renderSearchCustomer;
    }

    public void setRenderSearchCustomer(boolean renderSearchCustomer) {

        this.renderSearchCustomer = renderSearchCustomer;
    }

    public boolean isRenderCreateCustomer() {
        return renderCreateCustomer;
    }

    public void setRenderCreateCustomer(boolean renderCreateCustomer) {
        this.renderCreateCustomer = renderCreateCustomer;
    }

    public String getAddressOption() {
        return addressOption;
    }

    public void setAddressOption(String addressOption) {
        this.addressOption = addressOption;
    }

    public boolean isRenderSearchAddress() {
        return renderSearchAddress;
    }

    public void setRenderSearchAddress(boolean renderSearchAddress) {
        this.renderSearchAddress = renderSearchAddress;
    }

    public boolean isRenderCreateAddress() {
        return renderCreateAddress;
    }

    public void setRenderCreateAddress(boolean renderCreateAddress) {
        this.renderCreateAddress = renderCreateAddress;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
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
    
    public String onFlowProcess(FlowEvent event) {
        if(skip) {
            skip = false;   //reset in case user goes back
            address = customer;
            return "itemsTab";
        }
        else {
            return event.getNewStep();
        }
    }

    public void switchCustomerOption() {
        renderSearchCustomer = customerOption.equals("search");
        renderCreateCustomer = customerOption.equals("create");
        customer = new Person();
        RequestContext.getCurrentInstance().update("form:customerPanel");

    }
    
    public void switchAddressOption() {
        renderSearchAddress = addressOption.equals("search");
        renderCreateAddress = addressOption.equals("create");
        address = new Person();
        RequestContext.getCurrentInstance().update("form:addressPanel");

    }
}
