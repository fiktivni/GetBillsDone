package model;
// Generated 27.8.2014 20:47:12 by Hibernate Tools 3.6.0

/**
 * Person generated by hbm2java
 */
public class Person implements java.io.Serializable {

    private Integer id;
    private int accountIdaccount;
    private String name;
    private String lastname;
    private String wholename;
    private String company;
    private String street;
    private String house;
    private String city;
    private Integer pcode;
    private String state;
    private Boolean isowner;
    private Integer phone;
    private String email;
    private Integer fax;
    private String www;
    private String bankaccount;
    private Integer ico;
    private String dic;
    private String pscAndCityInOneLine;
    private Boolean locked;

    public Person() {
        this.name = "";
        this.lastname = "";
        locked = false;
     }

    public Person(int accountIdaccount) {
        this.accountIdaccount = accountIdaccount;
        locked = false;
    }

    public Person(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
        locked = false;
    }

    public Person(int accountIdaccount, String name, String lastname, String company, String street, String house, String city, Integer pcode, String state, Boolean isowner, Integer phone, String email, Integer fax, String www, String bankaccount, Integer ico, String dic) {
        this.accountIdaccount = accountIdaccount;
        this.name = name;
        this.lastname = lastname;
        this.company = company;
        this.street = street;
        this.house = house;
        this.city = city;
        this.pcode = pcode;
        this.state = state;
        this.isowner = isowner;
        this.phone = phone;
        this.email = email;
        this.fax = fax;
        this.www = www;
        this.bankaccount = bankaccount;
        this.ico = ico;
        this.dic = dic;
        locked = false;
    }

    public Person(int accountIdaccount, String name, String lastname,
            String company, String street, String house, String city, Integer pcode,
            Boolean isowner, String email, Integer ico) {
        this.accountIdaccount = accountIdaccount;
        this.name = name;
        this.lastname = lastname;
        this.company = company;
        this.street = street;
        this.house = house;
        this.city = city;
        this.pcode = pcode;
        this.state = "Česká Republika";
        this.isowner = isowner;
        this.email = email;
        this.ico = ico;
        locked = false;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAccountIdaccount() {
        return this.accountIdaccount;
    }

    public void setAccountIdaccount(int accountIdaccount) {
        this.accountIdaccount = accountIdaccount;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPcode() {
        return this.pcode;
    }

    public void setPcode(Integer pcode) {
        this.pcode = pcode;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getIsowner() {
        return this.isowner;
    }

    public void setIsowner(Boolean isowner) {
        this.isowner = isowner;
    }

    public Integer getPhone() {
        return this.phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getFax() {
        return this.fax;
    }

    public void setFax(Integer fax) {
        this.fax = fax;
    }

    public String getWww() {
        return this.www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public String getBankaccount() {
        return this.bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    public Integer getIco() {
        return this.ico;
    }

    public void setIco(Integer ico) {
        this.ico = ico;
    }

    public String getDic() {
        return this.dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    /**
     * @return the wholename
     */
    public String getWholename() {
        
        if (name.length() > 0 && lastname.length() > 0)
           wholename = name + " " + lastname;
        else
           wholename = "";
        
        return wholename;
    }

    /**
     * @param wholename the wholename to set
     */
    public void setWholename(String wholename) {
        this.wholename = wholename;
    }

    /**
     * @return the house
     */
    public String getHouse() {
        return house;
    }

    /**
     * @param house the house to set
     */
    public void setHouse(String house) {
        this.house = house;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getPscAndCityInOneLine() {
        if (pcode != null && city != null){
            return pcode + " " + city;
        } else {
            return "";
        }
    }

    public void setPscAndCityInOneLine(String pscAndCityInOneLine) {
        this.pscAndCityInOneLine = pscAndCityInOneLine;
    }

}
