package com.saake.invoicer.controller;

import com.saake.invoicer.controller.InvoiceController;
import com.saake.invoicer.entity.Customer;
import com.saake.invoicer.controller.masterdata.util.JsfUtil;
import com.saake.invoicer.entity.Vehicle;
import com.saake.invoicer.entity.Item;
import com.saake.invoicer.sessionbean.CustomerFacade;
import com.saake.invoicer.util.Utils;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.emptyType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "customerController")
@ViewScoped
public class CustomerController implements Serializable {

    private static final Log log = LogFactory.getLog(CustomerController.class);

    private Customer current;
    private List<Customer> items = null;
    List<Customer> suggestCustomerList = new ArrayList<>();
    
    @EJB
    private com.saake.invoicer.sessionbean.CustomerFacade ejbFacade;

    private int selectedItemIndex;
    private int rowKeyVar;

    @PostConstruct
    private void initialize() {
        if ( JsfUtil.getViewId().contains("create")){
            newInit();
        }
        else
        if ( JsfUtil.getViewId().contains("edit")){
            editInit();
        }
        else
        if ( JsfUtil.getViewId().contains("view")){
            viewInit();
        }
        else
        if ( JsfUtil.getViewId().contains("list")){
            prepareList();
        }        
        
    }
    
    public CustomerController() {
    }

    public Customer getSelected() {
        if (current == null) {
            current = new Customer();
            selectedItemIndex = -1;
        }
        return current;
    }

    private CustomerFacade getFacade() {
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
        current = new Customer();
        selectedItemIndex = -1;
        return "create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CustomerCreated"));
//            JsfUtil.addRequestObject("cust", current);
//            JsfUtil.addRequestParameter("cust", current.getCustomerId().toString());
            return "view.jsf?faces-redirect=true&id="+current.getCustomerId();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public Customer create(Customer cust) {
        try {
            getFacade().create(cust);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CustomerCreated"));
            return cust;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            log.error("Error creating customer",e);
            return null;
        }
    }

    public String prepareEdit() {
//        current = (Customer) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CustomerUpdated"));
            return "view.jsf?faces-redirect=true&id="+current.getCustomerId();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public Customer update(Customer cust) {
        return getFacade().edit(current);
    }

    public String softDelete() {
        try {
            getFacade().softDelete(current);

            prepareList();
            
            JsfUtil.addSuccessMessage("Customer Deleted");

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }

        return null; //"list?faces-redirect=true";
    }    
    
    private void newInit() {
        current = new Customer();
        current.setCreateTs(new Date());
    }

    private void viewInit() {
        String id = JsfUtil.getRequestParameter("id");
        
        if(Utils.notBlank(id)){
            current = getFacade().find(Integer.parseInt(id));                      
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

    public List<SelectItem> getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Customer getCustomer(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    public Vehicle getVehicle(java.lang.Integer id) {
        return ejbFacade.getVehicle(id);
    }

    public Vehicle saveCustomerVehicle(Vehicle custVehicle) {
        return ejbFacade.saveCustomerVehicle(custVehicle);
    }

    public List<Customer> getItems() {        
        if(items == null){
            items = ejbFacade.findAll();
        }
        return items;
    }

    public List<Customer> suggestCustomer(String inp){

        suggestCustomerList.clear();

        if (Utils.notBlank(inp)) {
            for (Customer party : getItems()) {
                if (Utils.notBlank(party.getCompanyName())
                        && party.getCompanyName().toLowerCase().contains(inp.toString().trim().toLowerCase())) {

                    suggestCustomerList.add(party);
                } else if (Utils.notBlank(party.getFirstName())
                        && party.getFirstName().toLowerCase().contains(inp.toString().trim().toLowerCase())) {

                    suggestCustomerList.add(party);
                } else if (Utils.notBlank(party.getLastName())
                        && party.getLastName().toLowerCase().contains(inp.toString().trim().toLowerCase())) {

                    suggestCustomerList.add(party);
                } else if (Utils.notBlank(party.getGivenName())
                        && party.getGivenName().toLowerCase().contains(inp.toString().trim().toLowerCase())) {

                    suggestCustomerList.add(party);
                }
            }
        }
        else{
            suggestCustomerList.addAll(getItems());
        }

        if (Utils.isEmpty(suggestCustomerList)) {
            JsfUtil.addInfoMessage("ordItemTable", "No Customers found");
        }
        
        return suggestCustomerList;
    }        
    
    public void setItems(List<Customer> items) {
        this.items = items;
    }

    public int getRowKeyVar() {
        return rowKeyVar;
    }

    public void setRowKeyVar(int rowKeyVar) {
        this.rowKeyVar = rowKeyVar;
    }

    public void setCurrent(Customer current) {
        this.current = current;
    }

    public Customer getCurrent() {
        return current;
    }
    
    public void addVehicleToCustomer(){
        if(current != null){
            if(Utils.isEmpty(current.getCustomerVehicles())){
                current.setCustomerVehicles(new ArrayList<Vehicle>());
            }
            
            Vehicle veh = new Vehicle();
            veh.setCustomerId(current);
            current.getCustomerVehicles().add(veh);
        }
    }
    
    public void removeCustomerVehicle(Vehicle veh){
        if(current != null){
            if(!Utils.isEmpty(current.getCustomerVehicles())){
                current.getCustomerVehicles().remove(veh);
            }            
        }
    }
}
