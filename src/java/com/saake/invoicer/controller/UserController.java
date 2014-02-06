package com.saake.invoicer.controller;

import com.saake.invoicer.entity.Customer;
import com.saake.invoicer.controller.masterdata.util.JsfUtil;
import com.saake.invoicer.entity.User;
import com.saake.invoicer.entity.Vehicle;
import com.saake.invoicer.sessionbean.CustomerFacade;
import com.saake.invoicer.sessionbean.UserFacade;
import com.saake.invoicer.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "userCtrl")
@ViewScoped
public class UserController implements Serializable {

    private static final Log log = LogFactory.getLog(UserController.class);

    private User current;
    private List<User> items;
    List<User> suggestUserList = new ArrayList<>();
    
    @EJB
    private com.saake.invoicer.sessionbean.UserFacade ejbFacade;

//    @PostConstruct
//    private void initialize() {
//        if ( JsfUtil.getViewId().contains("create")){
//            newInit();
//        }
//        else
//        if ( JsfUtil.getViewId().contains("edit")){
//            editInit();
//        }
//        else
//        if ( JsfUtil.getViewId().contains("view")){
//            viewInit();
//        }
//        else
//        if ( JsfUtil.getViewId().contains("list")){
//            prepareList();
//        }        
//        
//    }
    
    public UserController() {
    }

    public User getSelected() {
        if (current == null) {
            current = new User();
        }
        return current;
    }

    private UserFacade getFacade() {
        return ejbFacade;
    }    

    public String prepareList() {
        recreateModel();
        return "list";
    }

    public String prepareView() {
//        current = (Customer) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "view?includeViewParams=true";
    }

    public String prepareCreate() {
        current = new User();
        return "create";
    }

//    public String create() {
//        try {
//            getFacade().create(current);
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CustomerCreated"));
////            JsfUtil.addRequestObject("cust", current);
////            JsfUtil.addRequestParameter("cust", current.getCustomerId().toString());
//            return "view.jsf?faces-redirect=true&id="+current.getCustomerId();
//        } catch (Exception e) {
//            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
//            return null;
//        }
//    }
    
//    public Customer create(Customer cust) {
//        try {
//            getFacade().create(cust);
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CustomerCreated"));
//            return cust;
//        } catch (Exception e) {
//            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
//            log.error("Error creating customer",e);
//            return null;
//        }
//    }

    public String prepareEdit() {
//        current = (Customer) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

//    public String update() {
//        try {
//            getFacade().edit(current);
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CustomerUpdated"));
//            return "view.jsf?faces-redirect=true&id="+current.getCustomerId();
//        } catch (Exception e) {
//            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
//            return null;
//        }
//    }

//    public Customer update(Customer cust) {
//        return getFacade().edit(current);
//    }

    public String softDelete() {
//        try {
//            getFacade().softDelete(current);
//
//            prepareList();
//            
//            JsfUtil.addSuccessMessage("Customer Deleted");
//
//        } catch (Exception e) {
//            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
//        }

        return null; //"list?faces-redirect=true";
    }    
    
    private void newInit() {
        current = new User();
//        current.setCreateTs(new Date());
    }

    private void viewInit() {
        String id = JsfUtil.getRequestParameter("id");
        
        if(Utils.notBlank(id)){
            current = getFacade().findUser(id);                      
        }   
    }

    private void editInit() {
        viewInit();
    }

    private void updateCurrentItem() {
//        int count = getFacade().count();
//        if (selectedItemIndex >= count) {
//            // selected index cannot be bigger than number of items:
//            selectedItemIndex = count - 1;
//            // go to previous page if last page disappeared:
//            if (pagination.getPageFirstItem() >= count) {
//                pagination.previousPage();
//            }
//        }
//        if (selectedItemIndex >= 0) {
//            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
//        }
    }

//    public DataModel getItems() {
//        if (items == null) {
//            items = getPagination().createPageDataModel();
//        }
//        return items;
//    }

    private void recreateModel() {
        items = null;
    }

//    private void recreatePagination() {
//        pagination = null;
//    }
//
//    public String next() {
//        getPagination().nextPage();
//        recreateModel();
//        return "List";
//    }

//    public String previous() {
//        getPagination().previousPage();
//        recreateModel();
//        return "List";
//    }

//    public SelectItem[] getItemsAvailableSelectMany() {
//        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
//    }

//    public List<SelectItem> getItemsAvailableSelectOne() {
//        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
//    }

    public User getUser(String userId){
        return ejbFacade.findUser(userId);
    }
    
    public List<User> getItems() {        
        if(items == null){
            items = ejbFacade.findAll();
        }
        return items;
    }

    public List<User> suggestUser(String inp){

        suggestUserList.clear();

        if (Utils.notBlank(inp)) {
            for (User user : getItems()) {
                if (Utils.notBlank(user.getFirstName())
                        && user.getFirstName().toLowerCase().contains(inp.toString().trim().toLowerCase())) {

                    suggestUserList.add(user);
                } else if (Utils.notBlank(user.getLastName())
                        && user.getLastName().toLowerCase().contains(inp.toString().trim().toLowerCase())) {

                    suggestUserList.add(user);
                } 
            }
        }
        else{
            suggestUserList.addAll(getItems());
        }

        if (Utils.isEmpty(suggestUserList)) {
            JsfUtil.addInfoMessage("No Users found");
        }
        
        return suggestUserList;
    }        
    
    public void setItems(List<User> items) {
        this.items = items;
    }

    public void setCurrent(User current) {
        this.current = current;
    }

    public User getCurrent() {
        return current;
    }
   
}
