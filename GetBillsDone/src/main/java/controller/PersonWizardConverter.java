package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpSession;
import model.Person;

@FacesConverter("personWizardConverter")
@ManagedBean
@SessionScoped
public class PersonWizardConverter implements Converter, Serializable {

    private List<Person> contacts = new ArrayList<>();
    private final String userId = getUserID();
    
    @PostConstruct
    public void init() {
        contacts = Queries.getPersonsAtAccountId(userId);
    }
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {

        if (!value.contains("null")) {
            
            int personId = Integer.parseInt(value);
            
            for (Person p : contacts) {
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
    
    private String getUserID() {
        HttpSession s = HttpSessionUtil.getSession();
        if (s != null) {
            return s.getAttribute("logedid").toString();
        }
        return null;
    }
    
}
