/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.beans;

import controller.HttpSessionUtil;
import controller.Queries;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import model.Invoice;
import model.InvoiceHasItem;
import model.InvoiceHasPerson;
import model.Item;
import model.Method;
import model.Person;
import model.Rate;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "invoiceBean")
@SessionScoped
public class InvoiceBean implements Serializable {

    private boolean singleContact;
    private boolean newCustomerContact;
    private boolean newRecipientContact;
    private Person recipient;
    private Person customer;
    private Method method;
    private Item item;
    private InvoiceHasItem invoiceHasItem;
    private List<Item> items;
    private List<Item> invoiceItems;
    private List<Person> persons;
    private List<Method> methods;
    private String logedID = "0";
    private Invoice selectedInvoice;

    private UIComponent recipientFields;
    private List<Rate> rates;

    public void printInvoice(ActionEvent actionEvent) throws IOException, JRException {
        
         if (!singleContact) {
             recipient = customer;
         }
         
         selectedInvoice.setMethodIdmethod(method.getId());
         controller.Printer.printInvoice(actionEvent, getSelectedInvoice(), 
                 getInvoiceItems(), Queries.getUser(logedID), getCustomer(), getRecipient());
       
         if (!singleContact) {
             recipient = new Person();
         }
    }

    /**
     * @return the selectedInvoice
     */
    public Invoice getSelectedInvoice() {
        return selectedInvoice;
    }

    /**
     * @param aSelectedInvoice the selectedInvoice to set
     */
    public void setSelectedInvoice(Invoice aSelectedInvoice) {
        selectedInvoice = aSelectedInvoice;
    }

    @PostConstruct
    public void init() {

        HttpSession s = HttpSessionUtil.getSession();

        //Get users ID from session
        if (s != null) {
            setLogedID((s.getAttribute("logedid").toString()));
        }
        //Init variables
        persons = Queries.getPersonsAtAccountId(logedID);
        rates = Queries.getRates();
        items = Queries.getItemsAtAccountId(logedID);
        methods = Queries.getMethods();
        invoiceItems = new ArrayList<>();

        selectedInvoice = new Invoice();
        customer = new Person();
        recipient = new Person();
        setMethod(methods.get(0));
        setItem(new Item());
        getItem().setRate(rates.get(0));

        Calendar cal = Calendar.getInstance();
        selectedInvoice.setCreated(cal.getTime());
        selectedInvoice.setDuzp(cal.getTime());
        cal.add(Calendar.DATE, 14);
        selectedInvoice.setDue(cal.getTime());

        /*
         Fill invoice, recipient, customer with ID
         */
        selectedInvoice.setAccountIdaccount(Integer.parseInt(logedID));
        selectedInvoice.setStateIdstate(1);
        recipient.setAccountIdaccount(Integer.parseInt(logedID));
        customer.setAccountIdaccount(Integer.parseInt(logedID));
        /*
         */

    }

    public void flushForm() {
        selectedInvoice = new Invoice();
    }

    public List<Person> completeContact(String query) {
        
        if (!isSingleContact())
                    recipient = new Person();

        List<Person> filterPersons = new ArrayList<>();

        int validResultsCount = 0;

        for (Person person : persons) {
            if (person.getWholename().toLowerCase().contains(query)) {
                if ((person.getId() != customer.getId()) && (person.getId() != recipient.getId())) {
                    validResultsCount++;
                    if (validResultsCount <= 10) {
                        filterPersons.add(person);
                    }
                }
            }
        }
        
        
        return filterPersons;
    }

    public List<Item> completeItemCode(String query) {

        List<Item> filterItems = new ArrayList<>();

        int validResultsCount = 0;

        for (Item item : items) {
            if (item.getCode().toLowerCase().contains(query)) {
                validResultsCount++;
                if (validResultsCount <= 10) {
                    filterItems.add(item);
                }
            }
        }

        return filterItems;
    }

    public List<Item> completeItemTitle(String query) {

        List<Item> filterItems = new ArrayList<>();

        int validResultsCount = 0;

        for (Item item : items) {
            if (item.getTitle().toLowerCase().contains(query)) {
                validResultsCount++;
                if (validResultsCount <= 10) {
                    filterItems.add(item);
                }
            }
        }

        return filterItems;
    }

    public List<Method> completeMethod(String query) {

        List<Method> filterMethods = new ArrayList<>();

        int validResultsCount = 0;

        for (Method method : getMethods()) {
            if (method.getTitle().toLowerCase().contains(query)) {
                validResultsCount++;
                if (validResultsCount <= 10) {
                    filterMethods.add(method);
                }
            }
        }

        return filterMethods;

    }

    public String saveInvoice() {

        int total = 0;

        for (Item i : invoiceItems) {
            total += i.getPriceWithVat();
        }

        /*
         Save invoice and return her ID to variable
         */
        selectedInvoice.setAccountIdaccount(Integer.parseInt(logedID));
        selectedInvoice.setStateIdstate(1);
        selectedInvoice.setMethodIdmethod(method.getId());
        selectedInvoice.setVariablesymbol(777); //TO DO
        selectedInvoice.setTotal(total);
        int savedInvoiceID = Queries.createInvoice(selectedInvoice);

        Queries.createInvoiceHasPerson(new InvoiceHasPerson(savedInvoiceID, Queries.getUser(logedID).getId(), 1));

        /*
         Save recipient, customer and fill invoice with their ID and return her ID to variable
         */
        customer.setAccountIdaccount(Integer.parseInt(logedID));
        customer.setIsowner(false);
        int savedCustomerID;
        if (isNewCustomerContact()) {
            savedCustomerID = Queries.createPerson(customer);
        } else {
            savedCustomerID = customer.getId();
        }
        Queries.createInvoiceHasPerson(new InvoiceHasPerson(savedInvoiceID, savedCustomerID, 2));

        int savedRecipientID = savedCustomerID;
        if (singleContact) {
            recipient.setAccountIdaccount(Integer.parseInt(logedID));
            recipient.setIsowner(false);
            if (isNewRecipientContact()) {
                savedRecipientID = Queries.createPerson(recipient);
            } else {
                savedRecipientID = recipient.getId();
            }
        }
        Queries.createInvoiceHasPerson(new InvoiceHasPerson(savedInvoiceID, savedRecipientID, 3));
        
        for(Item i : invoiceItems){           
            Queries.createItem(i);
            i.getInvoiceHasItem().setInvoiceIdinvoice(savedInvoiceID);
            i.getInvoiceHasItem().setItemIditem(i.getId());
            Queries.createInvoiceHasItem(i.getInvoiceHasItem());            
        }
        
        

        return "dashboard";
    }

    /*
     public void createNew() {
     if (items.contains(item)) {
     FacesMessage msg = new FacesMessage("Dublicated", "This item has already been added");
     FacesContext.getCurrentInstance().addMessage(null, msg);
     } else {
     items.add(item);
     item = new Item();
     }
     }*/
    public String reinit() {
        setItem(new Item());
        return null;
    }

    public boolean isSingleContact() {
        return singleContact;
    }

    public void setSingleContact(boolean singleContact) {
        this.singleContact = singleContact;
    }

    /**
     * @return the persons
     */
    public List<Person> getPersons() {
        return persons;
    }

    /**
     * @param persons the persons to set
     */
    public void setPersons(List<Person> persons) {
        this.persons = persons;
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

    public Person getRecipient() {
        return recipient;
    }

    public void setRecipient(Person recipient) {
        this.recipient = recipient;
    }

    public Person getCustomer() {
        return customer;
    }

    public void setCustomer(Person customer) {
        this.customer = customer;
    }

    public void updateRecipientFields() {
        RequestContext.getCurrentInstance().update("form:recipientFields");
    }

    /**
     * @return the invoiceHasItem
     */
    public InvoiceHasItem getInvoiceHasItem() {
        return invoiceHasItem;
    }

    /**
     * @param invoiceHasItem the invoiceHasItem to set
     */
    public void setInvoiceHasItem(InvoiceHasItem invoiceHasItem) {
        this.invoiceHasItem = invoiceHasItem;
    }

    public UIComponent getRecipientFields() {
        return recipientFields;
    }

    public void setRecipientFields(UIComponent recipientFields) {
        this.recipientFields = recipientFields;
    }

    /**
     * @return the newCustomerContact
     */
    public boolean isNewCustomerContact() {
        return newCustomerContact;
    }

    /**
     * @param newCustomerContact the newCustomerContact to set
     */
    public void setNewCustomerContact(boolean newCustomerContact) {
        this.newCustomerContact = newCustomerContact;
    }

    /**
     * @return the newRecipientContact
     */
    public boolean isNewRecipientContact() {
        return newRecipientContact;
    }

    /**
     * @param newRecipientContact the newRecipientContact to set
     */
    public void setNewRecipientContact(boolean newRecipientContact) {
        this.newRecipientContact = newRecipientContact;
    }

    /**
     * @return the rates
     */
    public List<Rate> getRates() {
        return rates;
    }

    /**
     * @param rates the rates to set
     */
    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    /**
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * @return the items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * @return the methods
     */
    public List<Method> getMethods() {
        return methods;
    }

    /**
     * @param methods the methods to set
     */
    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    /**
     * @return the invoiceItems
     */
    public List<Item> getInvoiceItems() {
        return invoiceItems;
    }

    /**
     * @param invoiceItems the invoiceItems to set
     */
    public void setInvoiceItems(List<Item> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    /**
     * @return the method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(Method method) {
        this.method = method;
    }

}
