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
import model.Method;
import model.beans.InvoiceBean;

@FacesConverter("methodConverter")
public class MethodConverter implements Converter {

    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        
        if (!value.contains("null")) {
            InvoiceBean service = (InvoiceBean) fc.getExternalContext().getSessionMap().get("invoiceBean");
            int i = 0;
            for (i = 0; i < service.getMethods().size()-1; i++) {
                if (service.getMethods().get(i).getId() == Integer.parseInt(value)) {
                    break;
                }
            }
            return service.getMethods().get(i);
        } else {
            return new Method();
        }
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Method) object).getId());
        } else {
            return null;
        }
    }
}
