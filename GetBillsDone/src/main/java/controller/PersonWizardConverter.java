package controller;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Person;
import model.beans.InvoiceWizardBean;

@FacesConverter("personWizardConverter")
public class PersonWizardConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {

        if (!value.contains("null")) {
            
            int personId = Integer.parseInt(value);
            InvoiceWizardBean service = (InvoiceWizardBean) fc.getExternalContext().getSessionMap().get("invoiceWizardBean");
            List<Person> persons = service.getContacts();
            
            for (Person p : persons) {
                if (p.getId() == personId) {
                    return p;
                }
            }

        } 
        
        return new Person();
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Person) object).getId());
        } else {
            return null;
        }
    }
}
