package controller;

import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Item;
import model.beans.InvoiceWizardBean;

@FacesConverter("itemWizardConverter")
public class ItemWizardConverter implements Converter {

    private List<Item> items = new ArrayList<>();
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {

        if (!value.contains("null")) {
            
            int itemId = Integer.parseInt(value);
            
            InvoiceWizardBean service = (InvoiceWizardBean) fc.getExternalContext().getSessionMap().get("invoiceWizardBean");
            items = service.getAllItems();
            
            for (Item i : items){
                if (i.getId() == itemId){
                    return i;
                }
            }
        }
        
        return new Item();
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Item) object).getId());
        } else {
            return null;
        }
    }
}
