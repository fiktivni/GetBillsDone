package controller;
 
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Item;
import model.beans.InvoiceBean;
 

 
@FacesConverter("itemConverter")
public class ItemConverter implements Converter {
 
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
      
        if(!value.contains("null")) {
             InvoiceBean service = (InvoiceBean) fc.getExternalContext().getSessionMap().get("invoiceBean");
           int i;
           for (i =0; i < service.getItems().size()-1; i++ ){
                 if(service.getItems().get(i).getId() == Integer.parseInt(value)) break;              
            }
           service.getItems().get(i).setRate(Queries.getRateAtId(
                                    service.getItems().get(i).getRateIdrate()));
           service.getItems().get(i).setPriceWithoutVat(service.getItems().get(i).getPrice());
                       
           return service.getItems().get(i);
        
        }
        else {
            return new Item();
        }
        
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((Item) object).getId());
        }
        else {
            return null;
        }
    }   
}   