/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.beans;

import controller.Queries;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import model.Invoice;
import model.InvoiceHasItem;
import model.InvoiceHasPerson;
import model.Item;
import model.Method;
import model.Person;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author tempest
 */
@SessionScoped
@ManagedBean(name = "invoiceWizardBean")
public class InvoiceWizardBean implements Serializable {

    @Inject
    DashboardBean dashboard;

    private Invoice invoice = new Invoice();
    private Person customer = new Person();
    private Person address = new Person();
    private Method method = new Method();
    private Item item = new Item();
    private int userId;
    private Person user = new Person();
    private Person invoiceOwner = new Person();
    private List<Person> contacts = new ArrayList<>();
    private List<Person> unlockedContacts = new ArrayList();
    private List<Method> methods = new ArrayList();
    private List<Invoice> invoices = new ArrayList();
    private List<Item> allItems = new ArrayList<>();
    private List<Item> addedItems = new ArrayList<>();
    private List<Item> unlockedItems = new ArrayList();
    private String customerOption;
    private boolean renderSearchCustomer;
    private boolean renderCreateCustomer;
    private String addressOption;
    private boolean renderSearchAddress;
    private boolean renderCreateAddress;
    private String itemOption;
    private boolean renderSearchItem;
    private boolean renderCreateItem;
    private boolean isSingleAddress;
    private double sumNetPrice = 0;
    private double sumFullPrice = 0;
    private double sumTax = 0;

    @PostConstruct
    public void init() {

        userId = Integer.parseInt(dashboard.getLogedID());
        invoice = new Invoice();
        customer = new Person();
        address = new Person();
        item = new Item();
        contacts = dashboard.getPersons();
        unlockedContacts = dashboard.getUnlockedPersons();
        methods = dashboard.getMethods();
        invoices = dashboard.getInvoices();
        allItems = dashboard.getItems();
        unlockedItems = dashboard.getUnlockedItems();
        addedItems = new ArrayList<>();
        user = dashboard.getUser();
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
        isSingleAddress = true;
        Calendar cal = Calendar.getInstance();
        invoice.setCreated(cal.getTime());
        invoice.setDuzp(cal.getTime());
        cal.add(Calendar.DATE, 14);
        invoice.setDue(cal.getTime());
        invoice.setAccountIdaccount(userId);
        invoice.setStateIdstate(1);
        invoice.setInvoicenumber(cal.get(Calendar.YEAR) * 100000 + invoices.size() + 1);
        invoice.setConstantsymbol(308);
        invoice.setVariablesymbol(invoice.getInvoicenumber());
        method = methods.get(1);
        address.setAccountIdaccount(userId);
        address.setIsowner(false);
        customer.setAccountIdaccount(userId);
        customer.setIsowner(false);
        item.setAccountIdaccount(userId);
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
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

    public Person getInvoiceOwner() {
        return invoiceOwner;
    }

    public void setInvoiceOwner(Person invoiceOwner) {
        this.invoiceOwner = invoiceOwner;
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

    public boolean isIsSingleAddress() {
        return isSingleAddress;
    }

    public void setIsSingleAddress(boolean isSingleAddress) {
        this.isSingleAddress = isSingleAddress;
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

    public double getSumTax() {
        sumTax = sumFullPrice - sumNetPrice;
        return sumTax;
    }

    public void setSumTax(double sumTax) {
        this.sumTax = sumTax;
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

    public List<Person> getUnlockedContacts() {
        return unlockedContacts;
    }

    public void setUnlockedContacts(List<Person> unlockedContacts) {
        this.unlockedContacts = unlockedContacts;
    }

    public List<Item> getUnlockedItems() {
        return unlockedItems;
    }

    public void setUnlockedItems(List<Item> unlockedItems) {
        this.unlockedItems = unlockedItems;
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

    public void addNewItem() {
        saveItem();
        addItem();
        renderSearchItem = true;
        renderCreateItem = false;
        itemOption = "search";
    }

    public void saveItem() {
        if (item.getId() == null) {
            Queries.createItem(item);
        } else {
            Queries.updateItem(item);
        }
        allItems = Queries.getItemsAtAccountId("" + userId);
        dashboard.setItems(allItems);
        unlockedItems = dashboard.getUnlockedItems();
    }

    public List<Person> completeContact(String query) {

        List<Person> found = new ArrayList<>();
        int validResultsCount = 0;

        for (Person person : unlockedContacts) {
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

        for (Item i : unlockedItems) {
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

        for (Item i : unlockedItems) {
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
        if (isSingleAddress && (event.getOldStep().equals("customerTab")) && (!event.getNewStep().equals("headTab"))) {
            updateAddress();
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
        isSingleAddress = true;
        RequestContext.getCurrentInstance().update("form:addressPanel");
    }

    public void switchItemOption() {
        renderSearchItem = itemOption.equals("search");
        renderCreateItem = itemOption.equals("create");
        item = new Item();
        item.setAccountIdaccount(userId);

        RequestContext.getCurrentInstance().update("form:itemPanel");
    }

    public void updateItemPanel() {
        RequestContext.getCurrentInstance().update("form:itemPanel");
    }

    public void updateAddress() {
        if (isSingleAddress) {
            address = customer;

        } else {
            address = new Person();
            address.setAccountIdaccount(userId);
        }
        RequestContext.getCurrentInstance().update("form:addressPanel");
    }

    public String convertDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(date);
    }

    public String saveInvoice() {

        Double total = getSumFullPrice();

        invoice.setStateIdstate(1);
        invoice.setMethodIdmethod(method.getId());
        invoice.setTotal(total.intValue());
        invoice.setId(Queries.createInvoice(invoice));
        Queries.createInvoiceHasPerson(new InvoiceHasPerson(invoice.getId(), user.getId(), 1));

        customer.setIsowner(false);
        savePerson(customer, 2);
        if (isSingleAddress) {
            Queries.createInvoiceHasPerson(new InvoiceHasPerson(invoice.getId(), customer.getId(), 3));
        } else {
            address.setIsowner(false);
            savePerson(address, 3);
        }
        contacts = Queries.getPersonsAtAccountId(userId + "");
        dashboard.setPersons(contacts);
        unlockedContacts = dashboard.getUnlockedPersons();

        for (Item i : addedItems) {
            cloneItem(i);
            i.setLocked(true);
            i.getInvoiceHasItem().setInvoiceIdinvoice(invoice.getId());
            i.getInvoiceHasItem().setItemIditem(i.getId());
            Queries.updateItem(i);
            Queries.createInvoiceHasItem(i.getInvoiceHasItem());
        }
        allItems = Queries.getItemsAtAccountId(userId + "");
        dashboard.setItems(allItems);
        unlockedItems = dashboard.getUnlockedItems();

        invoices.add(invoice);
        dashboard.setInvoices(invoices);
        return "invoices";
    }

    public void printInvoice(ActionEvent actionEvent) throws IOException, JRException {

        if (invoice.getId() == null) {
            saveInvoice();
        }

        if(invoiceOwner == new Person()){
            controller.Printer.printInvoice(actionEvent, invoice, addedItems, user, customer, address);
        } else {
            controller.Printer.printInvoice(actionEvent, invoice, addedItems, invoiceOwner, customer, address);
        }
        
    }

    /**
     * Loads coplete invoice from database and displays it in a preview mode.
     *
     * @param invoice
     * @return
     */
    public String previewInvoice(Invoice invoice) {
        init();
        this.invoice = invoice;

        List<InvoiceHasItem> invoiceHasItems = Queries.getInvoiceHasItemList(invoice.getId());
        for (InvoiceHasItem invoiceHasItem : invoiceHasItems) {
            for (Item aitem : allItems) {
                if (invoiceHasItem.getItemIditem() == aitem.getId()) {
                    aitem.setInvoiceHasItem(invoiceHasItem);
                    addedItems.add(aitem);
                }
            }
        }

        getSumNetPrice();
        getSumFullPrice();
        getSumTax();

        List<InvoiceHasPerson> invoiceHasPersons = Queries.getInvoiceHasPersonList(invoice.getId());
        for (InvoiceHasPerson invoiceHasPerson : invoiceHasPersons) {
            if (invoiceHasPerson.getRelation() == 1) {
                invoiceOwner = Queries.getPerson(invoiceHasPerson.getPersonIdperson());
            } else {
                for (Person contact : contacts) {
                    if (invoiceHasPerson.getPersonIdperson() == contact.getId()) {
                        switch (invoiceHasPerson.getRelation()) {
                            case 1: // We already know who the owner is + this should not happen, ever.
                                break;
                            case 2:
                                customer = contact;
                                break;
                            case 3:
                                address = contact;
                                break;
                        }
                    }
                }
            }

        }

        return "invoicePreview";
    }

    /**
     * Clones the given Person at first, than locks the original one and
     * updates/creates it, finally, creates a relation entity InvoiceHasPerson
     * binding the global invoice and the locked Person using relation specified
     * as the second parameter.
     *
     * @param person
     * @param relation
     */
    private void savePerson(Person person, int relation) {

        clonePerson(person);
        person.setLocked(true);

        if (person.getId() == null) {
            person.setId(Queries.createPerson(person));
        } else {
            Queries.updatePerson(person);
        }

        Queries.createInvoiceHasPerson(new InvoiceHasPerson(invoice.getId(), person.getId(), relation));

    }

    private void cloneItem(Item i) {
        Item clone = new Item();
        clone.setAccountIdaccount(i.getAccountIdaccount());
        clone.setCode(i.getCode());
        clone.setFullPrice(i.getFullPrice());
        clone.setInvoiceHasItem(i.getInvoiceHasItem());
        clone.setLocked(false);
        clone.setNetPrice(i.getNetPrice());
        clone.setTaxRate(i.getTaxRate());
        clone.setTitle(i.getTitle());
        Queries.createItem(clone);

    }

    private void clonePerson(Person p) {
        Person clone = new Person();
        clone.setAccountIdaccount(p.getAccountIdaccount());
        clone.setBankaccount(p.getBankaccount());
        clone.setCity(p.getCity());
        clone.setCompany(p.getCompany());
        clone.setDic(p.getDic());
        clone.setEmail(p.getEmail());
        clone.setFax(p.getFax());
        clone.setHouse(p.getHouse());
        clone.setIco(p.getIco());
        clone.setIsowner(false);
        clone.setLastname(p.getLastname());
        clone.setLocked(false);
        clone.setName(p.getName());
        clone.setPcode(p.getPcode());
        clone.setPhone(p.getPhone());
        clone.setState(p.getState());
        clone.setStreet(p.getStreet());
        clone.setWww(p.getWww());
        Queries.createPerson(clone);
    }
}
