/*
 * Třída obsahující metody, jenž se dotazují na Account uživatele. 
 */
package controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import model.Account;
import model.HibernateUtil;
import model.Invoice;
import model.InvoiceHasItem;
import model.InvoiceHasPerson;
import model.Item;
import model.Method;
import model.Person;
import model.Rate;
import model.State;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Queries {

    static SessionFactory sessionFactory = null;

    public static void init() {
        sessionFactory = HibernateUtil.getSessionFactory();

    }

    /*
     Vyhledá v databázi kombinaci email/heslo. Pokud je nalezen jedem záznam, 
     vrátí true a zaznamená ID do session (TO DO)
     */
    public static boolean login(String email, String password) {
        Account logedAccount = null;
        Session session = sessionFactory.openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Account where password ='" + password + "' and email ='" + email + "'");
            logedAccount = (Account) q.uniqueResult();

            if (logedAccount != null) {

                //Get HTML Session and put there user's id as attribute "logedid"
                HttpSession s = HttpSessionUtil.getSession();
                s.setAttribute("logedid", logedAccount.getId());
                //
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
        } finally {
            session.close();
        }

        return false;

    }

    public static List<Invoice> getInvoices(int logedUserId) {
        List<Invoice> invoices = null;
        Session session = sessionFactory.openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();

            Query q = session.createQuery("from Invoice where account_idaccount ='" + logedUserId + "'");
            invoices = (List<Invoice>) q.list();

            return invoices;
        } catch (Exception e) {
        } finally {
            session.close();
        }

        return invoices;
    }

    public static List<Rate> getRates() {
        List<Rate> rates = null;
        Session session = sessionFactory.openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();

            Query q = session.createQuery("from Rate ");
            rates = (List<Rate>) q.list();

            return rates;
        } catch (Exception e) {
        } finally {
            session.close();
        }

        return rates;
    }

    public static Invoice getInvoiceAtId(int id) {

        Session session = sessionFactory.openSession();
        Invoice result = null;

        try {
            Transaction tx = session.beginTransaction();

            Query q = session.createQuery("from Invoice where "
                    + "id ='" + id + "'");
            result = (Invoice) q.uniqueResult();
            return result;
        } catch (HibernateException e) {
        } finally {
            session.close();
        }
        return result;
    }

    public static List<Person> getPersonsAtAccountId(String idaccount) {

        Session session = sessionFactory.openSession();
        List<Person> result = null;

        try {
            Transaction tx = session.beginTransaction();

            Query q = session.createQuery("from Person where "
                    + "account_idaccount ='" + idaccount + "' and isowner=false");
            result = (List<Person>) q.list();

            return result;
        } catch (HibernateException e) {
        } finally {
            session.close();
        }
        return result;
    }

    /**
     * Returns user's details.
     *
     * @param accountId
     * @return user
     */
    public static Person getUser(String accountId) {

        Session s = sessionFactory.openSession();
        Person user = new Person();

        try {
            Query q = s.createQuery("from Person where "
                    + "account_idaccount ='" + accountId + "' and isowner = " + true);
            user = (Person) q.uniqueResult();
            return user;
        } catch (HibernateException e) {
        } finally {
            s.close();
        }
        return user;
    }

    /**
     * Returns account with the id.
     *
     * @param id
     * @return account
     */
    public static Account getAccount(String id) {
        Account account = new Account();
        Session s = sessionFactory.openSession();

        try {
            Query q = s.createQuery("from Account where id = '" + id + "'");
            account = (Account) q.uniqueResult();
            return account;
        } catch (HibernateException e) {
        } finally {
            s.close();
        }
        return account;
    }

    /**
     * Returns method with the id.
     *
     * @param id
     * @return method
     */
    public static Method getMethod(String id) {
        Method method = new Method();
        Session s = sessionFactory.openSession();

        try {
            Query q = s.createQuery("from Method where id = '" + id + "'");
            method = (Method) q.uniqueResult();
            return method;
        } catch (HibernateException e) {
        } finally {
            s.close();
        }
        return method;
    }

    /**
     * End of line for the person.
     *
     * @param person
     */
    public static void deletePerson(Person person) {

        Session s = sessionFactory.openSession();
        try {
            Transaction tx = s.beginTransaction();
            s.delete(person);
            tx.commit();
        } catch (HibernateException e) {
        } finally {
            s.close();
        }
    }

    public static void deleteItem(Item item) {

        Session s = sessionFactory.openSession();
        try {
            Transaction tx = s.beginTransaction();
            s.delete(item);
            tx.commit();
        } catch (HibernateException e) {
            // TODO
        } finally {
            s.close();
        }
    }

    /**
     * End of line for the account.
     *
     * @param account
     */
    public static void deleteAccount(Account account) {

        Session s = sessionFactory.openSession();
        try {
            Transaction tx = s.beginTransaction();
            s.delete(account);
            tx.commit();
        } catch (HibernateException e) {
        } finally {
            s.close();
        }
    }

    /**
     * Updates the person.
     *
     * @param person
     */
    public static void updatePerson(Person person) {

        Session s = sessionFactory.openSession();
        try {
            Transaction tx = s.beginTransaction();
            s.update(person);
            tx.commit();
        } catch (HibernateException e) {
        } finally {
            s.close();
        }

    }

    public static void updateItem(Item item) {
        Session s = sessionFactory.openSession();
        try {
            Transaction tx = s.beginTransaction();
            s.update(item);
            tx.commit();
        } catch (HibernateException e) {
            // TODO
        } finally {
            s.close();
        }
    }

    public static State getStateAtId(int id) {

        Session session = sessionFactory.openSession();
        State result = null;

        try {
            Transaction tx = session.beginTransaction();

            Query q = session.createQuery("from State where "
                    + "id ='" + id + "'");
            result = (State) q.uniqueResult();
            return result;
        } catch (HibernateException e) {
        } finally {
            session.close();
        }
        return result;
    }

    public static Rate getRateAtId(int id) {

        Session session = sessionFactory.openSession();
        Rate result = null;

        try {
            Transaction tx = session.beginTransaction();

            Query q = session.createQuery("from Rate where "
                    + "id ='" + id + "'");
            result = (Rate) q.uniqueResult();
            return result;
        } catch (HibernateException e) {
        } finally {
            session.close();
        }
        return result;
    }

    public static String getName(int idInvoice) {

        InvoiceHasPerson personId;
        Person person;

        String name = null;
        int userId;

        try {
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();

            Query q = session.createQuery("from InvoiceHasPerson where "
                    + "invoice_idinvoice ='" + idInvoice + "' "
                    + "and relation ='" + 2 + "'");
            personId = (InvoiceHasPerson) q.uniqueResult();
            userId = personId.getPersonIdperson();
            session.close();

            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            q = session.createQuery("from Person where id ='" + userId + "'");
            person = (Person) q.uniqueResult();
            session.close();

            name = person.getName() + " " + person.getLastname();

            return name;
        } catch (HibernateException e) {
        }
        return name;
    }

    public static int createAccount(Account newAccount) {

        Session session = null;
        State result = null;

        try {
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            session.save(newAccount);
            tx.commit();
            session.close();

            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query q = session.createQuery("from Account where password ='"
                    + newAccount.getPassword() + "' and email ='" + newAccount.getEmail() + "'");
            newAccount = (Account) q.uniqueResult();
            session.close();

            return newAccount.getId();

        } catch (HibernateException e) {
        }
        return 0;
    }

    public static int createPerson(Person newPerson) {

        Session session = null;
        State result = null;
        int personId;

        try {

            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            session.save(newPerson);
            session.flush();
            personId = newPerson.getId();
            tx.commit();
            session.close();

            return personId;

        } catch (HibernateException e) {
        }
        return 0;
    }

    public static void createInvoiceHasPerson(InvoiceHasPerson invoiceHasPerson) {

        Session session = null;
        State result = null;

        try {
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            session.save(invoiceHasPerson);
            session.flush();
            tx.commit();
            session.close();
        } catch (HibernateException e) {
        }

    }

    public static void createInvoiceHasItem(InvoiceHasItem invoiceHasItem) {

        Session session = null;
        State result = null;

        try {
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            session.save(invoiceHasItem);
            session.flush();
            tx.commit();
            session.close();
        } catch (HibernateException e) {
        }

    }

    /**
     * Returns List of InvoiceHasItem related to the invoice
     * specified via parameter invoiceId.
     *
     * @param invoiceId
     * @return
     */
    public static List<InvoiceHasItem> getInvoiceHasItemList(int invoiceId) {

        List<InvoiceHasItem> result = new ArrayList<>();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query q = session.createQuery("from InvoiceHasItem where "
                    + "invoice_idinvoice ='" + invoiceId + "' ");
            return (List<InvoiceHasItem>) q.list();
        } catch (Exception e) {

        } finally {
            session.close();
        }
        return result;

    }
    
    /**
     * Returns List of InvoiceHasPerson related to the invoice
     * specified via parameter invoiceId.
     *
     * @param invoiceId
     * @return
     */
    public static List<InvoiceHasPerson> getInvoiceHasPersonList(int invoiceId) {

        List<InvoiceHasPerson> result = new ArrayList<>();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query q = session.createQuery("from InvoiceHasPerson where "
                    + "invoice_idinvoice ='" + invoiceId + "' ");
            return (List<InvoiceHasPerson>) q.list();
        } catch (Exception e) {

        } finally {
            session.close();
        }
        return result;

    }

    public static void createItem(Item item) {

        Session session = null;
        State result = null;

        try {
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            session.save(item);
            session.flush();
            tx.commit();
            session.close();
        } catch (HibernateException e) {
        }

    }

    public static int createInvoice(Invoice selectedInvoice) {
        Session session = null;
        State result = null;
        int invoiceId;

        try {
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            session.save(selectedInvoice);
            session.flush();
            invoiceId = selectedInvoice.getId();
            tx.commit();
            session.close();

            return invoiceId;

        } catch (HibernateException e) {
        }
        return 0;
    }

    public static List<Item> getItemsAtAccountId(String logedID) {
        Session session = sessionFactory.openSession();
        List<Item> result = null;

        try {
            Transaction tx = session.beginTransaction();

            Query q = session.createQuery("from Item where "
                    + "account_idaccount ='" + logedID + "'");
            result = (List<Item>) q.list();

            return result;
        } catch (HibernateException e) {
        } finally {
            session.close();
        }
        return result;
    }

    public static List<Method> getMethods() {
        List<Method> methods = null;
        Session session = sessionFactory.openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Method ");
            methods = (List<Method>) q.list();
            return methods;
        } catch (Exception e) {
        } finally {
            session.close();
        }
        return methods;
    }

}
