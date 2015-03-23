/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Method;
import model.beans.InvoiceWizardBean;

@FacesConverter("methodWizardConverter")
public class MethodWizardConverter implements Converter {

    private List<Method> methods = new ArrayList<>();

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {

        if (!value.contains("null")) {

            int methodId = Integer.parseInt(value);

            InvoiceWizardBean service = (InvoiceWizardBean) fc.getExternalContext().getSessionMap().get("invoiceWizardBean");
            methods = service.getMethods();

            for (Method m : methods) {
                if (m.getId() == methodId) {
                    return m;
                }
            }
        }
        
        return new Method();
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Method) object).getId());
        } else {
            return null;
        }
    }
}
