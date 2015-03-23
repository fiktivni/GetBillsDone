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
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;
import model.Invoice;
import model.InvoiceHasItem;
import model.Item;
import model.Method;
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
    private Method method = new Method();
    private Item item = new Item();
    private final int userId = getUserID();
    private List<Person> contacts = new ArrayList<>();
    private List<Method> methods = new ArrayList();
    private List<Invoice> invoices = new ArrayList();
    private List<Item> allItems = new ArrayList<>();
    private List<Item> addedItems = new ArrayList<>();

    private String customerOption;
    private boolean renderSearchCustomer;
    private boolean renderCreateCustomer;

    private String addressOption;
    private boolean renderSearchAddress;
    private boolean renderCreateAddress;

    private String itemOption;
    private boolean renderSearchItem;
    private boolean renderCreateItem;

    private boolean skip;

    private double sumNetPrice = 0;
    private double sumFullPrice = 0;

    @PostConstruct
    public void init() {
        invoice = new Invoice();
        customer = new Person();
        address = new Person();
        item = new Item();
        contacts = Queries.getPersonsAtAccountId("" + userId);
        methods = Queries.getMethods();
        invoices = Queries.getInvoices(userId);
        allItems = Queries.getItemsAtAccountId("" + userId);
        addedItems = new ArrayList<>();

        sumNetPrice = 0;
        sumFullPrice = 0;

        customerOption = "search";
        renderSearchCustomer = customerOption.equals("search");
        renderCreateCustomer = customerOption.equals("create");

        addressOption = "search";
        renderSearchAddress = addressOption.equals("search");
        renderCreateAddress = addressOption.equals("create");

        itemOption = "search";
        renderSearchItem = itemOption.equals("search");
        renderCreateItem = itemOption.equals("create");

        skip = false;

        Calendar cal = Calendar.getInstance();
        invoice.setCreated(cal.getTime());
        invoice.setDuzp(cal.getTime());
        cal.add(Calendar.DATE, 14);
        invoice.setDue(cal.getTime());
        invoice.setAccountIdaccount(userId);
        invoice.setStateIdstate(1);
        invoice.setInvoicenumber(invoices.size() + 1);

        address.setAccountIdaccount(userId);
        customer.setAccountIdaccount(userId);
        item.setAccountIdaccount(userId);
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

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setAddress(Person address) {
        this.address = address;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Person> getContacts() {
        return contacts;
    }

    public void setContacts(List<Person> contacts) {
        this.contacts = contacts;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public List<Item> getAllItems() {
        return allItems;
    }

    public void setAllItems(List<Item> allItems) {
        this.allItems = allItems;
    }

    public List<Item> getAddedItems() {
        return addedItems;
    }

    public void setAddedItems(List<Item> addedItems) {
        this.addedItems = addedItems;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
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

    public String getItemOption() {
        return itemOption;
    }

    public void setItemOption(String itemOption) {
        this.itemOption = itemOption;
    }

    public boolean isRenderSearchItem() {
        return renderSearchItem;
    }

    public void setRenderSearchItem(boolean renderSearchItem) {
        this.renderSearchItem = renderSearchItem;
    }

    public boolean isRenderCreateItem() {
        return renderCreateItem;
    }

    public void setRenderCreateItem(boolean renderCreateItem) {
        this.renderCreateItem = renderCreateItem;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public double getSumNetPrice() {

        sumNetPrice = 0;
        for (Item i : addedItems) {
            sumNetPrice += i.getNetPrice() * i.getInvoiceHasItem().getCount();
        }
        return sumNetPrice;
    }

    public void setSumNetPrice(double sumNetPrice) {
        this.sumNetPrice = sumNetPrice;
    }

    public double getSumFullPrice() {

        sumFullPrice = 0;
        for (Item i : addedItems) {
            sumFullPrice += i.getFullPrice() * i.getInvoiceHasItem().getCount();
        }
        return sumFullPrice;
    }

    public void setSumFullPrice(double sumFullPrice) {
        this.sumFullPrice = sumFullPrice;
    }

    private int getUserID() {
        HttpSession s = HttpSessionUtil.getSession();
        int userID = -1;
        if (s != null) {
            userID = Integer.parseInt((s.getAttribute("logedid").toString()));
        }
        return userID;
    }

    public void addItem() {
        Item newItem = new Item();
        newItem.setAccountIdaccount(userId);

        if (!item.equals(newItem)) {
            newItem.setCode(item.getCode());
            newItem.setFullPrice(item.getFullPrice());
            newItem.setId(item.getId());
            InvoiceHasItem ihi = new InvoiceHasItem();
            ihi.setCount(item.getInvoiceHasItem().getCount());
            newItem.setInvoiceHasItem(ihi);
            newItem.setNetPrice(item.getNetPrice());
            newItem.setTaxRate(item.getTaxRate());
            newItem.setTitle(item.getTitle());
            addedItems.add(newItem);
            item = new Item();
            item.setAccountIdaccount(userId);
        }
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

    public List<Method> completeMethod(String query) {

        List<Method> filterMethods = new ArrayList<>();

        int validResultsCount = 0;

        for (Method m : methods) {
            if (m.getTitle().toLowerCase().contains(query)) {
                validResultsCount++;
                if (validResultsCount <= 10) {
                    filterMethods.add(m);
                }
            }
        }

        return filterMethods;

    }

    public List<Item> completeItemCode(String query) {

        List<Item> found = new ArrayList<>();

        int validResultsCount = 0;

        for (Item i : allItems) {
            if (i.getCode().toLowerCase().contains(query)) {
                validResultsCount++;
                if (validResultsCount <= 10) {
                    found.add(i);
                }
            }
        }

        return found;

    }

    public List<Item> completeItemTitle(String query) {

        List<Item> found = new ArrayList<>();

        int validResultsCount = 0;

        for (Item i : allItems) {
            if (i.getTitle().toLowerCase().contains(query)) {
                validResultsCount++;
                if (validResultsCount <= 10) {
                    found.add(i);
                }
            }
        }

        return found;

    }

    public String onFlowProcess(FlowEvent event) {
        if (skip) {
            skip = false;   //reset in case user goes back
            address = customer;
            return "itemsTab";
        } else {
            return event.getNewStep();
        }
    }

    public void switchCustomerOption() {
        renderSearchCustomer = customerOption.equals("search");
        renderCreateCustomer = customerOption.equals("create");
        customer = new Person();
        customer.setAccountIdaccount(userId);

        RequestContext.getCurrentInstance().update("form:customerPanel");
    }

    public void switchAddressOption() {
        renderSearchAddress = addressOption.equals("search");
        renderCreateAddress = addressOption.equals("create");
        address = new Person();
        address.setAccountIdaccount(userId);

        RequestContext.getCurrentInstance().update("form:addressPanel");
    }

    public void switchItemOption() {
        renderSearchItem = itemOption.equals("search");
        renderCreateItem = itemOption.equals("create");
        item = new Item();
        item.setAccountIdaccount(userId);

        RequestContext.getCurrentInstance().update("form:itemPanel");
    }
    
    public void updateItemPanel(){
        RequestContext.getCurrentInstance().update("form:itemPanel");
    }
}
