/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.controller;

import com.saake.invoicer.entity.Customer;
import com.saake.invoicer.entity.Invoice;
import com.saake.invoicer.entity.Vehicle;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import com.saake.invoicer.entity.WorkOrder;
import com.saake.invoicer.entity.WorkOrderItems;
import com.saake.invoicer.entity.Item;
import com.saake.invoicer.entity.Transaction;
import com.saake.invoicer.model.WorkOrderDataModel;
import com.saake.invoicer.model.WorkOrderDataModel;
import com.saake.invoicer.reports.ReportHelper;
import com.saake.invoicer.sessionbean.WorkOrderFacade;
import com.saake.invoicer.util.JsfUtil;
import com.saake.invoicer.util.TransTypeEnum;
import com.saake.invoicer.util.Utils;
import com.saake.invoicer.util.WorkOrderStatusEnum;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jn
 */
@ManagedBean(name = "workOrderCtrl")
@ViewScoped
public class WorkOrderController implements Serializable {

    private static final Log log = LogFactory.getLog(WorkOrderController.class);
    private WorkOrderDataModel model;
    private WorkOrder current;
    private WorkOrderItems currentInvItem;
//    private SearchWorkOrderVO filterCriteria = new SearchWorkOrderVO();
//    private Orders invoiceOrder;
    private Transaction currentTransaction = new Transaction();
//    private Customer orderCustomer;
//    private CustomerVehicle custVehicle = new CustomerVehicle();
    private List<WorkOrder> originalWorkOrderList = null;
    private List<WorkOrder> workOrderList = null;
    private List<Vehicle> suggestVehicleList = new ArrayList<>();
    @Inject
    CustomerController custCtrl;
    
    @Inject
    ItemController itemCtrl;
    
    @EJB
    private com.saake.invoicer.sessionbean.WorkOrderFacade ejbFacade;
    
    @EJB
    private com.saake.invoicer.sessionbean.ItemFacade itemFacade;
    
    private boolean redirect = false;
    private Boolean addNewVechicle = false;
    public Integer noOfRowsToAdd = 1;

    public WorkOrderController() {
        log.info("Inside WorkOrderController!!!");
        Object inv = JsfUtil.getRequestObject("item");

        if (inv != null && inv instanceof WorkOrder) {
            current = (WorkOrder) inv;
        }
//        String action = JsfUtil.getRequestParameter("action");
//        String invoiceId = JsfUtil.getRequestParameter("invoiceId");
//        
//        if("view".equalsIgnoreCase(action)){
//            if(Utils.notBlank(invoiceId)){
//                current = getFacade().find(Long.parseLong(invoiceId));
//            }            
//        }        
    }

    @PostConstruct
    private void initialize() {
        if (JsfUtil.getViewId().contains("create")) {
            initNewWorkOrder();
        } else if (JsfUtil.getViewId().contains("edit")) {
            editWorkOrderInit();
        } else if (JsfUtil.getViewId().contains("view")) {
            viewWorkOrderInit();
        } else if (JsfUtil.getViewId().contains("list")) {
            prepareList();
        }

        redirect = false;
    }

    public WorkOrder getSelected() {
        if (current == null) {
            initNewWorkOrder();
        }
        return current;
    }

    public String prepareList() {
        recreateModel();
        return deriveReturnString("list", false);
    }

    public String redirectToList() {
        return "list.jsf?faces-redirect=true";
    }

    public String prepareView() {
        return deriveReturnString("view", true);

    }

    private void recreateModel() {
        workOrderList = null;
    }

    public void initNewWorkOrder() {
        current = new WorkOrder();
        current.setWorkOrderItems(new ArrayList<WorkOrderItems>());
        addNewItemToWorkOrder();
        current.setWorkOrderDate(new Date());
        current.setAmount(0.0);
    }

    public String prepareCreate() {
        initNewWorkOrder();
        return deriveReturnString("create", false);
    }

    public String save() {
        if (current != null) {
            if (validated()) {
                if (Utils.notEmpty(current.getWorkOrderItems())) {
                    List<WorkOrderItems> emptyList = new ArrayList<>();
                    for (WorkOrderItems items : current.getWorkOrderItems()) {
                        if (items.isEmptyForUse()) {
                            emptyList.add(items);
                        }
                    }
                    current.getWorkOrderItems().removeAll(emptyList);
                }

                if (current.getWorkOrderId() != null) {
                    return update();
                } else {
                    return create();
                }
            }
        }

        return null;
    }

    public String create() {
        try {
            
            getFacade().createWorkOrder(current);
            JsfUtil.addSuccessMessage("Work Order Created!");
            redirect = true;
            return prepareView();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
//        current = (Customer) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();

        if (current != null) {
            if (Utils.notEmpty(current.getWorkOrderItems())) {
                for (WorkOrderItems woItm : current.getWorkOrderItems()) {
                    if (woItm.getItem() == null) {
                        woItm.setItem(new Item(0.00));
                    }
                }
            }
        }
        JsfUtil.addAttributeInRequest("item", current);

        redirect = true;

        return deriveReturnString("edit", true);

    }

    public String update() {
        try {
                       
            current = getFacade().updateWorkOrder(current);

            JsfUtil.addRequestObject("item", current);
            JsfUtil.addSuccessMessage("Work Order Updated!");

            redirect = true;
            return prepareView();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String delete() {
        try {
            getFacade().remove(current);

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("WorkOrderDeleted"));
            prepareList();

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }

        return deriveReturnString("list", false);
    }

    public String softDelete() {
        try {
            getFacade().softDelete(current);

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("WorkOrderDeleted"));
            prepareList();

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }

        return deriveReturnString("list", false);
    }

    public void addNewRowsToWorkOrder(Integer noOfRowsToAdd ) {
        for(int i = 0; i< noOfRowsToAdd; i++){
            addNewItemToWorkOrder();
        }
    }
    
    public String addNewItemToWorkOrder() {
        WorkOrderItems it = new WorkOrderItems();
        it.setAmount(0.00);
        it.setDiscount(0.00);
        it.setUnitPrice(0.00);
        it.setItem(new Item());
        it.getItem().setUnitPrice(0.00);
        it.setWorkOrderId(current); 
        it.setQuantity(1);
        current.getWorkOrderItems().add(it);
        return null;
    }

    public String removeOrderItem(WorkOrderItems ordItm) {
        if (ordItm != null) {
            if (current.getWorkOrderItems().size() > 1) {
                current.getWorkOrderItems().remove(ordItm);
            }

            calOrderPrice();
        }

        return null;
    }

    public String calOrderPrice() {
        Double amt = current.getItemTotalAmount();

        if (amt != null) {
            if (current.getDiscount() != null && current.getDiscount() > 0) {
                amt = amt - current.getDiscount();
            }
            current.setAmount(amt);
        } else {
            current.setAmount(0.0);
        }

        return null;
    }

    public void calculateOrderItemPrice(WorkOrderItems ordItm) {
        if (ordItm != null && ordItm.getQuantity() != null) {
            ordItm.setAmount((ordItm.getUnitPrice() * ordItm.getQuantity()) - (ordItm.getDiscount() != null ? ordItm.getDiscount() * ordItm.getQuantity() : 0));
        }

        calOrderPrice();
    }
    
    public void populateItemOnWorkOrder(WorkOrderItems ordItm) {
        if(ordItm != null){
            if(ordItm.getItem() != null){
                ordItm.setUnitPrice(ordItm.getItem().getUnitPrice());
                ordItm.setDescription(ordItm.getItem().getDescription());
            }
        }
        
        calculateOrderItemPrice(ordItm);
    }    

//    public List<Item> suggestCustomer(String inp){
//
//        List<Customer> filteredCustomerList = new ArrayList<>();
//
//        if (Utils.notBlank(inp)) {
//            for (Item item : getItems()) {
//                if (Utils.notBlank(item.getCompanyName())
//                        && item.getCompanyName().toLowerCase().contains(inp.toString().trim().toLowerCase())) {
//
//                    filteredCustomerList.add(item);
//                } else if (Utils.notBlank(item.getFirstName())
//                        && item.getFirstName().toLowerCase().contains(inp.toString().trim().toLowerCase())) {
//
//                    filteredCustomerList.add(item);
//                } else if (Utils.notBlank(item.getLastName())
//                        && item.getLastName().toLowerCase().contains(inp.toString().trim().toLowerCase())) {
//
//                    filteredCustomerList.add(item);
//                } else if (Utils.notBlank(item.getGivenName())
//                        && item.getGivenName().toLowerCase().contains(inp.toString().trim().toLowerCase())) {
//
//                    filteredCustomerList.add(item);
//                }
//            }
//        }
//
//        if (Utils.isEmpty(filteredCustomerList)) {
//            JsfUtil.addInfoMessage("ordItemTable", "No Customers found");
//        }
//        
//        return filteredCustomerList;
//    }
    private WorkOrderFacade getFacade() {
        return ejbFacade;
    }

    public List<WorkOrder> getWorkOrderList() {
        if (workOrderList == null) {
            workOrderList = ejbFacade.findAll();
            originalWorkOrderList = new ArrayList(workOrderList);
        }
        return workOrderList;
    }

    public void setWorkOrderList(List<WorkOrder> workOrderList) {
        this.workOrderList = workOrderList;
    }

    public WorkOrderFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(WorkOrderFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public WorkOrder getCurrent() {
        if (current == null) {
            initNewWorkOrder();
        }
        return current;
    }

    public void setCurrent(WorkOrder current) {
        this.current = current;
    }

//    public Orders getWorkOrderOrder() {
//        return invoiceOrder;
//    }
//
//    public void setWorkOrderOrder(Orders invoiceOrder) {
//        this.invoiceOrder = invoiceOrder;
//    }
//
//    public Transactions getOrderTransaction() {
//        return orderTransaction;
//    }
//
//    public void setOrderTransaction(Transactions orderTransaction) {
//        this.orderTransaction = orderTransaction;
//    }
//
//    public Customer getOrderCustomer() {
//        return orderCustomer;
//    }
//
//    public void setOrderCustomer(Customer orderCustomer) {
//        this.orderCustomer = orderCustomer;
//    }
    private void editWorkOrderInit() {
        viewWorkOrderInit();
    }

    private void viewWorkOrderInit() {
        String id = JsfUtil.getRequestParameter("id");

        if (Utils.notBlank(id)) {
            current = getFacade().getWorkOrder(Integer.parseInt(id));
        }

        if (current != null) {
            if (current.getVehicle() == null) {
                initNewVehicleToWorkOrder();
            }
        }
    }

    public void filterList() {
//        if (!filterCriteria.empty()) {
//            workOrderList.clear();
//
//            for (WorkOrder inv : originalWorkOrderList) {
//                if (Utils.notBlank(filterCriteria.getWorkOrderPeriod())) {
//                    if ("today".equalsIgnoreCase(filterCriteria.getWorkOrderPeriod())) {
//                        if (inv.getWorkOrderDate() != null && (inv.getWorkOrderDate().equals(new Date()))) {
//                            workOrderList.add(inv);
//                        }
//                    } else if ("yest".equalsIgnoreCase(filterCriteria.getWorkOrderPeriod())) {
////                            if(Utils.notBlank(inv.getStatus()) && inv.getStatus().equalsIgnoreCase(filterCriteria.getStatus())){
////                                workOrderList.add(inv);
////                            }
//                    }
//                } else if (Utils.notBlank(filterCriteria.getStatus())) {
//                    if (Utils.notBlank(inv.getStatus()) && inv.getStatus().equalsIgnoreCase(filterCriteria.getStatus())) {
//                        workOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getCustomer() != null && filterCriteria.getCustomer().getCustomerId() != null) {
//                    if (inv.getCustomerId() != null && inv.getCustomerId().getCustomerId() != null
//                            && inv.getCustomerId().getCustomerId().equals(filterCriteria.getCustomer().getCustomerId())) {
//                        workOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getFromAmount() != null && filterCriteria.getToAmount() != null) {
//                    if (inv.getAmount() != null && inv.getAmount() >= filterCriteria.getFromAmount()
//                            && inv.getAmount() <= filterCriteria.getToAmount()) {
//                        workOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getFromAmount() != null) {
//                    if (inv.getAmount() != null && inv.getAmount() >= filterCriteria.getFromAmount()) {
//                        workOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getToAmount() != null) {
//                    if (inv.getAmount() != null && inv.getAmount() <= filterCriteria.getToAmount()) {
//                        workOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getFromDate() != null && filterCriteria.getToDate() != null) {
//                    if (inv.getWorkOrderDate() != null
//                            && (filterCriteria.getFromDate().after(inv.getWorkOrderDate()) || filterCriteria.getFromDate().equals(inv.getWorkOrderDate()))
//                            && (filterCriteria.getToDate().before(inv.getWorkOrderDate()) || filterCriteria.getToDate().equals(inv.getWorkOrderDate()))) {
//                        workOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getFromDate() != null) {
//                    if (inv.getWorkOrderDate() != null
//                            && (filterCriteria.getFromDate().after(inv.getWorkOrderDate()) || filterCriteria.getFromDate().equals(inv.getWorkOrderDate()))) {
//                        workOrderList.add(inv);
//                    }
//                } else if (filterCriteria.getToDate() != null) {
//                    if (inv.getWorkOrderDate() != null
//                            && (filterCriteria.getToDate().before(inv.getWorkOrderDate()) || filterCriteria.getToDate().equals(inv.getWorkOrderDate()))) {
//                        workOrderList.add(inv);
//                    }
//                }
//            }
//        }
    }

    public void resetSearch() {
        workOrderList = new ArrayList(originalWorkOrderList);
//        filterCriteria = new SearchWorkOrderVO();
    }
//
//    public SearchWorkOrderVO getFilterCriteria() {
//        return filterCriteria;
//        return filterCriteria;
//    }

//    public void setFilterCriteria(SearchWorkOrderVO filterCriteria) {
//        this.filterCriteria = filterCriteria;
//    }

    public WorkOrderDataModel getModel() {
        if (model == null) {
            model = new WorkOrderDataModel(getWorkOrderList());
        }
        return model;
    }

    public void setModel(WorkOrderDataModel model) {
        this.model = model;
    }

    public void addNewCustomerToWorkOrder() {
        log.info("inside addNewCustomerToWorkOrder");

        Customer cust = custCtrl.create(custCtrl.getCurrent());
        current.setCustomerId(cust);

        if (Utils.notEmpty(cust.getCustomerVehicles())) {
            current.setVehicle(cust.getCustomerVehicles().get(cust.getCustomerVehicles().size() - 1));
        } else {
            current.setVehicle(new Vehicle());
        }
    }

    public void createNewItemAndAddToWorkOrder() {
        log.info("inside createNewItemAndAddToWorkOrder");

//        Item item = itemCtrl.create(itemCtrl.getCurrent());
        currentInvItem.setItem(itemCtrl.create(itemCtrl.getCurrent()));

        calculateOrderItemPrice(currentInvItem);
    }

    public ItemController getItemCtrl() {
        return itemCtrl;
    }

    public void setItemCtrl(ItemController itemCtrl) {
        this.itemCtrl = itemCtrl;
    }

    public CustomerController getCustCtrl() {
        return custCtrl;
    }

    public void setCustCtrl(CustomerController custCtrl) {
        this.custCtrl = custCtrl;
    }

    public void generateWorkOrderPdfAction() {
        JsfUtil.addAttributeInSession("invoice", current);
    }

    public void downloadPdf() {
        try {
            ReportHelper.downloadWorkOrderPDF(current);
        } catch (IOException ex) {
            Logger.getLogger(WorkOrderController.class.getName()).log(Level.SEVERE, "Error downloading pdf", ex);
        }
    }

    public void viewPdf() {
        try {
            ReportHelper.viewWorkOrderPDF(current);
        } catch (IOException ex) {
            Logger.getLogger(WorkOrderController.class.getName()).log(Level.SEVERE, "Error viewing pdf", ex);
        }
    }

    public void printWorkOrder() {
        try {
            ReportHelper.printWorkOrder(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error printing invoice");
            log.error("Error printing invoice", e);
        }
    }

    public String copyWorkOrder() {
        try {
            if(current != null){
                current = WorkOrder.copy(current, new WorkOrder());
                current.setWorkOrderDate(new Date());
                current.setCreateTs(new Date());
                current.setWorkOrderId(null);
                current.setInvoicedTs(null);
                current.setIsInvoiced("N");
                current.setStatus(WorkOrderStatusEnum.DRAFT.name());
                
                redirect = false;
                
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error printing invoice");
            log.error("Error printing invoice", e);
        }
        return deriveReturnString("edit", true);

    }

    private String deriveReturnString(String viewString, boolean addId) {
        if (redirect) {
            String id = "";
            if (addId && current != null && current.getWorkOrderId() != null) {
                id = "&id=" + current.getWorkOrderId();
            }

            return viewString + "?faces-redirect=true" + id;
        } else {
            return viewString;
        }
    }

    public WorkOrderItems getCurrentInvItem() {
        return currentInvItem;
    }

    public void setCurrentInvItem(WorkOrderItems currentInvItem) {
        this.currentInvItem = currentInvItem;
    }

    private Customer addCustomerVehicle(Customer cust) {
        if (!current.getVehicle().isEmpty()) {
            current.getVehicle().setCustomerId(cust);
            if (cust.getCustomerVehicles() == null) {
                cust.setCustomerVehicles(new ArrayList<Vehicle>());
            }

            current.setVehicle(custCtrl.saveCustomerVehicle(current.getVehicle()));
        }

        return cust;
    }

    public void redirectToView(Integer id) {
        try {
            if (id == null || id == 0) {
                id = current.getWorkOrderId();
            }
            JsfUtil.getExternalContext().redirect(JsfUtil.getExternalContext().getRequestContextPath()+"/workorder/view.jsf?id=" + id) ;
        } catch (IOException ex) {
            Logger.getLogger(WorkOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Vehicle> suggestVehicle(String inp) {

        suggestVehicleList.clear();

        if (current == null || current.getCustomerId() == null || Utils.isEmpty(current.getCustomerId().getCustomerVehicles())) {
            JsfUtil.addInfoMessage("No Vehicles found");
            return null;
        }

        if (Utils.isBlank(inp)) {
            suggestVehicleList.addAll(current.getCustomerId().getCustomerVehicles());
        }


        return suggestVehicleList;
    }

    public void identifyVehicle() {

        if (current != null || current.getCustomerId() != null) {
            if (Utils.isEmpty(current.getCustomerId().getCustomerVehicles())) {
                initNewVehicleToWorkOrder();
            } else {
                if (current.getCustomerId().getCustomerVehicles().size() == 1) {
                    current.setVehicle(current.getCustomerId().getCustomerVehicles().get(0));
                }
            }
        }
    }

    public void changeCustomer() {
        current.setCustomerId(null);
        current.setVehicle(null);
        addNewVechicle = false;
    }

    public void initNewVehicleToWorkOrder() {
        current.setVehicle(new Vehicle());
        addNewVechicle = true;
    }

    public boolean isAddNewVechicle() {
        return addNewVechicle;
    }

    public void setAddNewVechicle(Boolean addNewVechicle) {
        this.addNewVechicle = addNewVechicle;
    }

    private boolean validated() {
        boolean validated = true;
        if (Utils.isEmpty(current.getWorkOrderItems())) {
            validated = false;
            JsfUtil.addErrorMessage("Please add items to the invoice.");
        }
        if (current.getCustomerId() == null) {
            validated = false;
            JsfUtil.addErrorMessage("Please select a customer.");
        }
        if (current.getCustomerId() != null && (current.getVehicle() == null || current.getVehicle().isEmpty())) {
            validated = false;
            JsfUtil.addErrorMessage("Please select or add a vehicle.");
        }

        return validated;
    }

    public boolean getChangeVehicleIndicator() {
        return current != null && current.getVehicle() != null
                && current.getCustomerId() != null && Utils.notEmpty(current.getCustomerId().getCustomerVehicles());
    }

    public void changeVehicle() {
        addNewVechicle = false;
        current.setVehicle(null);
    }    
    
    public void assignWorkOrder(){
        if(current.getAssignedUser() != null){
            current = getFacade().assignWorkOrder(current);
        }
    }
    
    public void convertToInvoice(){
        try {
            Invoice invoice = getFacade().convertToInvoice(current);
            
            if(invoice != null){
                JsfUtil.getExternalContext().redirect(JsfUtil.getExternalContext().getRequestContextPath()+"/invoice/view.jsf?id=" + invoice.getInvoiceId());
            }
            else{
                throw new Exception("Unable to convert to Invoice");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    public void addNewItemToWorkOrder(WorkOrderItems ordItm) {
        ordItm.reset();
        
        ordItm.setAddItem(true);
        
        calculateOrderItemPrice(ordItm);

    }
    
    public void undoAddItemToWorkOrder(WorkOrderItems ordItm) {
        ordItm.reset();
                
        calculateOrderItemPrice(ordItm);    
    }

    public Integer getNoOfRowsToAdd() {
        return noOfRowsToAdd;
    }

    public void setNoOfRowsToAdd(Integer noOfRowsToAdd) {
        this.noOfRowsToAdd = noOfRowsToAdd;
    }
       
}
