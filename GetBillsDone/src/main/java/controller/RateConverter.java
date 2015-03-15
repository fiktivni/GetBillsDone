/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Rate;
import model.beans.InvoiceBean;

@FacesConverter("rateConverter")
public class RateConverter implements Converter {

    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        
        if (!value.contains("null")) {
            InvoiceBean service = (InvoiceBean) fc.getExternalContext().getSessionMap().get("invoiceBean");
            int i = 0;
            for (i = 0; i < service.getRates().size(); i++) {
                if (service.getRates().get(i).getId() == Integer.parseInt(value)) {
                    break;
                }
            }
            service.getItem().setRate(service.getRates().get(i));
            double price = service.getItem().getPriceWithoutVat();
            service.getItem().setPriceWithVat(price  * ((double)service.getRates().get(i).getValue()/(double)100) + price); 
            return service.getRates().get(i);
        } else {
            return new Rate();
        }
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Rate) object).getId());
        } else {
            return null;
        }
    }
}
