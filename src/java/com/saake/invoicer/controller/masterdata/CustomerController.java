package com.saake.invoicer.controller.masterdata;

import com.saake.invoicer.controller.InvoiceController;
import com.saake.invoicer.entity.Customer;
import com.saake.invoicer.controller.masterdata.util.JsfUtil;
import com.saake.invoicer.entity.CustomerVehicle;
import com.saake.invoicer.sessionbean.CustomerFacade;
import com.saake.invoicer.util.Utils;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.emptyType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
        return "List";
    }

    public String prepareView() {
//        current = (Customer) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View?includeViewParams=true";
    }

    public String prepareCreate() {
        current = new Customer();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CustomerCreated"));
            JsfUtil.addRequestObject("cust", current);
            JsfUtil.addRequestParameter("cust", current.getCustomerId().toString());
            return prepareView();
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
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public Customer update(Customer cust) {
        return getFacade().edit(current);
    }

    public String destroy() {
//        current = (Customer) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
//        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CustomerDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
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

    public CustomerVehicle saveCustomerVehicle(CustomerVehicle custVehicle) {
        return ejbFacade.saveCustomerVehicle(custVehicle);
    }

    @FacesConverter(forClass = Customer.class)
    public static class CustomerControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CustomerController controller = (CustomerController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "customerController");
            return controller.getCustomer(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Customer) {
                Customer o = (Customer) object;
                return getStringKey(o.getCustomerId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Customer.class.getName());
            }
        }
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
    
}
