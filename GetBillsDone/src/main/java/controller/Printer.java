/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import model.Invoice;
import model.Item;
import model.Person;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

/**
 *
 * @author Michal
 */
public class Printer {

    public static JasperPrint jasperPrint;

    public static void printInvoice(ActionEvent actionEvent, Invoice invoice, List<Item> items, Person contractor, Person customer, Person recipient) throws JRException, IOException {

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRENCH);
        DecimalFormat formatter = (DecimalFormat)nf;    
        Collection col = new java.util.Vector();
        DateFormat df = new SimpleDateFormat("MM.dd.yyyy");
        Map properties = new HashMap();
        Map map;
        double basePrice = 0;
        double price = 0;
        
        contractor = clearNulls(contractor);
        recipient = clearNulls(recipient);
        customer = clearNulls(customer);
        
        for (Item i : items){
            basePrice +=i.getPrice() * i.getInvoiceHasItem().getCount();
            price += basePrice * ((double)i.getRate().getValue()/100) + basePrice;
        }
        
        

        // Vypsání položek
        for (int i = 0; i < items.size(); i++) {
            Double itemBasePrice = items.get(i).getPrice() * items.get(i).getInvoiceHasItem().getCount();
            int rate = items.get(i).getRate().getValue();
            map = new HashMap();
            map.put("ITEM_CODE", items.get(i).getCode());
            map.put("ITEM_TITLE", items.get(i).getTitle());
            map.put("ITEM_QUANTITY", Integer.toString(items.get(i).getInvoiceHasItem().getCount()));
            map.put("ITEM_UNIT_PRICE", formatter.format(items.get(i).getPrice()));
            map.put("ITEM_BASE_PRICE", formatter.format(itemBasePrice));
            map.put("ITEM_DPH_PERCENTAGE", Integer.toString(rate) + "%");
            map.put("ITEM_FINAL_PRICE", formatter.format((itemBasePrice * ((double)rate/100)) + itemBasePrice));
            col.add(map);
        }
        //Empty row trick
        map = new HashMap();
        map.put("ITEM_CODE", "");
        map.put("ITEM_TITLE", "");
        map.put("ITEM_QUANTITY", "");
        map.put("ITEM_UNIT_PRICE", "");
        map.put("ITEM_BASE_PRICE", "");
        map.put("ITEM_DPH_PERCENTAGE", "");
        map.put("ITEM_FINAL_PRICE", "");
        col.add(map);

        properties.put("INVOICE_NUMBER", Integer.toString(invoice.getInvoicenumber()));
        properties.put("INVOICE_CREATED", df.format(invoice.getCreated()));
        properties.put("INVOICE_DUZP", df.format(invoice.getDuzp()));
        properties.put("INVOICE_DUE", df.format(invoice.getDue()));
        properties.put("INVOICE_METHOD",
                Queries.getMethod(Integer.toString(invoice.getMethodIdmethod())).getTitle());
        properties.put("INVOICE_BANK",  contractor.getBankaccount());
        if (invoice.getVariablesymbol() != null) {
        properties.put("INVOICE_VARIABLE", Integer.toString(invoice.getVariablesymbol()));
        }else{
        properties.put("INVOICE_VARIABLE", "");    
        }
        if (invoice.getConstantsymbol() != null) {
        properties.put("INVOICE_CONSTANT", Integer.toString(invoice.getConstantsymbol()));
        }else{
         properties.put("INVOICE_CONSTANT", "");   
        }
        
        properties.put("INVOICE_CREATOR", contractor.getWholename());

        properties.put("CONTRACTOR_NAME", contractor.getWholename());
        properties.put("CONTRACTOR_STREET", contractor.getStreet() + " " + contractor.getHouse());
        properties.put("CONTRACTOR_CITY", contractor.getCity());
        properties.put("CONTRACTOR_DIC", contractor.getDic());

        if (contractor.getPcode() != null) {
            properties.put("CONTRACTOR_PCODE", Integer.toString(contractor.getPcode()));
        } else {
            properties.put("CONTRACTOR_PCODE", "");
        }

        if (contractor.getIco() != null) {
            properties.put("CONTRACTOR_ICO", Integer.toString(contractor.getIco()));
        } else {
            properties.put("CONTRACTOR_ICO", "");
        }

        properties.put("CUSTOMER_NAME", customer.getWholename());
        properties.put("CUSTOMER_STREET", customer.getStreet() + " " + customer.getHouse());
        properties.put("CUSTOMER_CITY", customer.getCity());
        properties.put("CUSTOMER_DIC", customer.getDic());

        if (customer.getPcode() != null) {
            properties.put("CUSTOMER_PCODE", Integer.toString(customer.getPcode()));
        } else {
            properties.put("CUSTOMER_PCODE", "");
        }

        if (customer.getIco() != null) {
            properties.put("CUSTOMER_ICO", Integer.toString(customer.getIco()));
        } else {
            properties.put("CUSTOMER_ICO", "");
        }

        properties.put("RECIPIENT_NAME", recipient.getWholename());
        properties.put("RECIPIENT_STREET", recipient.getStreet() + " " + customer.getHouse());
        properties.put("RECIPIENT_CITY", recipient.getCity());

        if (recipient.getPcode() != null) {
            properties.put("RECIPIENT_PCODE", Integer.toString(recipient.getPcode()));
        } else {
            properties.put("RECIPIENT_PCODE", "");
        }

        
        properties.put("SUMMARY_BASE_PRICE", formatter.format(basePrice) + " Kč");
        properties.put("SUMMARY_DPH", formatter.format(price - basePrice) + " Kč");
        properties.put("SUMMARY_PRICE", formatter.format(price) + " Kč");

        JRDataSource dataSource = new JRMapCollectionDataSource(col);
        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/invoice.jasper");
        jasperPrint = JasperFillManager.fillReport(reportPath, properties, dataSource);

        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=invoice.pdf");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        FacesContext.getCurrentInstance().responseComplete();

    }

    private static Person clearNulls(Person p) {
        if (p.getName() == null) {
            p.setName("");
        }
        if (p.getLastname() == null) {
            p.setLastname("");
        }
        if (p.getCity() == null) {
            p.setCity("");
        }
        if (p.getStreet() == null) {
            p.setStreet("");
        }
        if (p.getHouse() == null) {
            p.setHouse("");
        }
        if (p.getDic() == null) {
            p.setDic("");
        }
        
        if (p.getBankaccount()== null) {
            p.setBankaccount("");
        }

        return p;
    }

}
