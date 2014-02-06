/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.controller;

import com.saake.invoicer.model.SearchInvoiceVO;
import com.saake.invoicer.entity.Customer;
import com.saake.invoicer.entity.Vehicle;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import com.saake.invoicer.entity.Invoice;
import com.saake.invoicer.entity.InvoiceItems;
import com.saake.invoicer.entity.Item;
import com.saake.invoicer.entity.Transaction;
import com.saake.invoicer.model.InvoiceDataModel;
import com.saake.invoicer.reports.ReportHelper;
import com.saake.invoicer.sessionbean.InvoiceFacade;
import com.saake.invoicer.util.JsfUtil;
import com.saake.invoicer.util.TransTypeEnum;
import com.saake.invoicer.util.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
@ManagedBean(name = "invoiceCtrl")
@ViewScoped
public class InvoiceController implements Serializable {

    private static final Log log = LogFactory.getLog(InvoiceController.class);
    private InvoiceDataModel model;
    private Invoice current;
    private InvoiceItems currentInvItem;
    private SearchInvoiceVO filterCriteria = new SearchInvoiceVO();
//    private Orders invoiceOrder;
    private Transaction currentTransaction = new Transaction();
//    private Customer orderCustomer;
//    private CustomerVehicle custVehicle = new CustomerVehicle();
    private List<Invoice> originalInvoiceList = null;
    private List<Invoice> invoiceList = null;
    private List<Vehicle> suggestVehicleList = new ArrayList<>();
    @Inject
    CustomerController custCtrl;
    @Inject
    ItemController itemCtrl;
    @EJB
    private com.saake.invoicer.sessionbean.InvoiceFacade ejbFacade;
    @EJB
    private com.saake.invoicer.sessionbean.ItemFacade itemFacade;
    private boolean redirect = false;
    private Boolean addNewVechicle = false;
    
    public InvoiceController() {
        log.info("Inside InvoiceController!!!");
        Object inv = JsfUtil.getRequestObject("item");

        if (inv != null && inv instanceof Invoice) {
            current = (Invoice) inv;
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
            initNewInvoice();
        } else if (JsfUtil.getViewId().contains("edit")) {
            editInvoiceInit();
        } else if (JsfUtil.getViewId().contains("view")) {
            viewInvoiceInit();
        } else if (JsfUtil.getViewId().contains("list")) {
            prepareList();
        }

        redirect = false;
    }

    public Invoice getSelected() {
        if (current == null) {
            initNewInvoice();
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
        invoiceList = null;
    }

    public void initNewInvoice() {
        current = new Invoice();
        current.setInvoiceItems(new ArrayList<InvoiceItems>());
        addNewItemToInvoice();
        current.setInvoiceDate(new Date());
        current.setAmount(0.0);
        current.setAdvanceAmount(0.0);
        current.setDiscount(0.0);
    }

    public String prepareCreate() {
        initNewInvoice();
        return deriveReturnString("create", false);
    }

    public String save() {
        if (current != null) {
            if (validated()) {
                if (Utils.notEmpty(current.getInvoiceItems())) {
                    List<InvoiceItems> emptyList = new ArrayList<>();
                    for (InvoiceItems items : current.getInvoiceItems()) {
                        if (items.isEmptyForUse()) {
                            emptyList.add(items);
                        }
                    }
                    current.getInvoiceItems().removeAll(emptyList);
                }

                if (current.getInvoiceId() != null) {
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
            getFacade().createInvoice(current);
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("InvoiceCreated"));
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

//        if (current != null) {
//            if (Utils.notEmpty(current.getInvoiceItems())) {
//                for (InvoiceItems invItm : current.getInvoiceItems()) {
//                    if (invItm.getItem() == null) {
//                        invItm.setItem(new Item(0.00));
//                    }
//                }
//            }
//        }
        JsfUtil.addAttributeInRequest("item", current);

        redirect = true;

        return deriveReturnString("edit", true);

    }

    public String update() {
        try {
            getFacade().updateInvoice(current);

            JsfUtil.addRequestObject("item", current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("InvoiceUpdated"));

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

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("InvoiceDeleted"));
            prepareList();

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }

        return deriveReturnString("list", false);
    }

    public String softDelete() {
        try {
            getFacade().softDelete(current);

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("InvoiceDeleted"));
            prepareList();

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }

        return deriveReturnString("list", false);
    }

    public void addNewRowsToInvoice(Integer noOfRowsToAdd ) {
        for(int i = 0; i< noOfRowsToAdd; i++){
            addNewItemToInvoice();
        }
    }
        
    public String addNewItemToInvoice() {
        InvoiceItems it = new InvoiceItems();
        it.setAmount(0.00);
        it.setDiscount(0.00);
        it.setUnitPrice(0.00);
        it.setItem(new Item());
        it.getItem().setUnitPrice(0.00);
        it.setInvoice(current);
        it.setQuantity(1);
        current.getInvoiceItems().add(it);
        return null;
    }

    public String removeOrderItem(InvoiceItems ordItm) {
        if (ordItm != null) {
            if (current.getInvoiceItems().size() > 1) {
                current.getInvoiceItems().remove(ordItm);
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

    public void addNewItemToInvoice(InvoiceItems ordItm) {
        ordItm.reset();
        
        ordItm.setAddItem(true);
        
        calculateOrderItemPrice(ordItm);

    }
    
    public void undoAddItemToInvoice(InvoiceItems ordItm) {
        ordItm.reset();
                
        calculateOrderItemPrice(ordItm);    
    }
    
    public void populateItemOnInvoice(InvoiceItems ordItm) {
        if(ordItm != null){
            if(ordItm.getItem() != null){
                ordItm.setUnitPrice(ordItm.getItem().getUnitPrice());
                ordItm.setDescription(ordItm.getItem().getDescription());
            }
        }
        
        calculateOrderItemPrice(ordItm);
    }
    
    public void calculateOrderItemPrice(InvoiceItems ordItm) {
        if (ordItm != null && ordItm.getQuantity() != null) {
            ordItm.setAmount((ordItm.getUnitPrice() * ordItm.getQuantity()) - (ordItm.getDiscount() != null ? ordItm.getDiscount() * ordItm.getQuantity() : 0));
        }

        calOrderPrice();
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
    private InvoiceFacade getFacade() {
        return ejbFacade;
    }

    public List<Invoice> getInvoiceList() {
        if (invoiceList == null) {
            invoiceList = ejbFacade.findAll();
            originalInvoiceList = new ArrayList(invoiceList);
        }
        return invoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }

    public InvoiceFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(InvoiceFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public Invoice getCurrent() {
        if (current == null) {
            initNewInvoice();
        }
        return current;
    }

    public void setCurrent(Invoice current) {
        this.current = current;
    }

//    public Orders getInvoiceOrder() {
//        return invoiceOrder;
//    }
//
//    public void setInvoiceOrder(Orders invoiceOrder) {
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
    private void editInvoiceInit() {
        viewInvoiceInit();
    }

    private void viewInvoiceInit() {
        String id = JsfUtil.getRequestParameter("id");

        if (Utils.notBlank(id)) {
            current = getFacade().getInvoice(Integer.parseInt(id));
        }

        if (current != null) {
            if (current.getVehicle() == null) {
                initNewVehicleToInvoice();
            }
        }
    }

    public void filterList() {
        if (!filterCriteria.empty()) {
            invoiceList.clear();

            for (Invoice inv : originalInvoiceList) {
                if (Utils.notBlank(filterCriteria.getSearchText())) {
                    if (Utils.notBlank(inv.getStatus()) && inv.getStatus().equalsIgnoreCase(filterCriteria.getSearchText())) {
                            invoiceList.add(inv);
                        }
                    
                    if (inv.getCustomerId() != null && inv.getCustomerId().getInvoiceListDisplayName()!= null
                                && inv.getCustomerId().getInvoiceListDisplayName().contains(filterCriteria.getSearchText())) {
                            invoiceList.add(inv);
                        }
                    
                    if (inv.getAmount() != null && inv.getAmount().toString().equals(filterCriteria.getSearchText())) {
                            invoiceList.add(inv);
                        }
                    
                    if (inv.getAmount() != null && inv.getVehicle() != null && 
                            (inv.getVehicle().getVin().contains(filterCriteria.getSearchText()) 
                            || inv.getVehicle().getModel().contains(filterCriteria.getSearchText())
                            || inv.getVehicle().getYear().contains(filterCriteria.getSearchText())
                            || inv.getVehicle().getMileage().contains(filterCriteria.getSearchText())
                            || inv.getVehicle().getMake().contains(filterCriteria.getSearchText()))) {
                            invoiceList.add(inv);
                        }
                }
                    
                if (Utils.notBlank(filterCriteria.getInvoicePeriod())) {
                        if ("today".equalsIgnoreCase(filterCriteria.getInvoicePeriod())) {
                            if (inv.getInvoiceDate() != null && (inv.getInvoiceDate().equals(new Date()))) {
                                invoiceList.add(inv);
                            }
                        } else if ("yest".equalsIgnoreCase(filterCriteria.getInvoicePeriod())) {
    //                            if(Utils.notBlank(inv.getStatus()) && inv.getStatus().equalsIgnoreCase(filterCriteria.getStatus())){
    //                                invoiceList.add(inv);
    //                            }
                        }
                    } else if (Utils.notBlank(filterCriteria.getStatus())) {
                        if (Utils.notBlank(inv.getStatus()) && inv.getStatus().equalsIgnoreCase(filterCriteria.getStatus())) {
                            invoiceList.add(inv);
                        }
                    } else if (filterCriteria.getCustomer() != null && filterCriteria.getCustomer().getCustomerId() != null) {
                        if (inv.getCustomerId() != null && inv.getCustomerId().getCustomerId() != null
                                && inv.getCustomerId().getCustomerId().equals(filterCriteria.getCustomer().getCustomerId())) {
                            invoiceList.add(inv);
                        }
                    } else if (filterCriteria.getFromAmount() != null && filterCriteria.getToAmount() != null) {
                        if (inv.getAmount() != null && inv.getAmount() >= filterCriteria.getFromAmount()
                                && inv.getAmount() <= filterCriteria.getToAmount()) {
                            invoiceList.add(inv);
                        }
                    } else if (filterCriteria.getFromAmount() != null) {
                        if (inv.getAmount() != null && inv.getAmount() >= filterCriteria.getFromAmount()) {
                            invoiceList.add(inv);
                        }
                    } else if (filterCriteria.getToAmount() != null) {
                        if (inv.getAmount() != null && inv.getAmount() <= filterCriteria.getToAmount()) {
                            invoiceList.add(inv);
                        }
                    } else if (filterCriteria.getFromDate() != null && filterCriteria.getToDate() != null) {
                        if (inv.getInvoiceDate() != null
                                && (filterCriteria.getFromDate().after(inv.getInvoiceDate()) || filterCriteria.getFromDate().equals(inv.getInvoiceDate()))
                                && (filterCriteria.getToDate().before(inv.getInvoiceDate()) || filterCriteria.getToDate().equals(inv.getInvoiceDate()))) {
                            invoiceList.add(inv);
                        }
                    } else if (filterCriteria.getFromDate() != null) {
                        if (inv.getInvoiceDate() != null
                                && (filterCriteria.getFromDate().after(inv.getInvoiceDate()) || filterCriteria.getFromDate().equals(inv.getInvoiceDate()))) {
                            invoiceList.add(inv);
                        }
                    } else if (filterCriteria.getToDate() != null) {
                        if (inv.getInvoiceDate() != null
                                && (filterCriteria.getToDate().before(inv.getInvoiceDate()) || filterCriteria.getToDate().equals(inv.getInvoiceDate()))) {
                            invoiceList.add(inv);
                        }
                    }
                }
            
        }
    }

    public void resetSearch() {
        invoiceList = new ArrayList(originalInvoiceList);
        filterCriteria = new SearchInvoiceVO();
    }

    public SearchInvoiceVO getFilterCriteria() {
        return filterCriteria;
    }

    public void setFilterCriteria(SearchInvoiceVO filterCriteria) {
        this.filterCriteria = filterCriteria;
    }

    public InvoiceDataModel getModel() {
        if (model == null) {
            model = new InvoiceDataModel(getInvoiceList());
        }
        return model;
    }

    public void setModel(InvoiceDataModel model) {
        this.model = model;
    }

    public void addNewCustomerToInvoice() {
        log.info("inside addNewCustomerToInvoice");

        Customer cust = custCtrl.create(custCtrl.getCurrent());
        current.setCustomerId(cust);

        if (Utils.notEmpty(cust.getCustomerVehicles())) {
            current.setVehicle(cust.getCustomerVehicles().get(cust.getCustomerVehicles().size() - 1));
        } else {
            current.setVehicle(new Vehicle());
        }
    }

    public void createNewItemAndAddToInvoice() {
        log.info("inside createNewItemAndAddToInvoice");

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

    public void generateInvoicePdfAction() {
        JsfUtil.addAttributeInSession("invoice", current);
    }

    public void downloadPdf() {
        try {
            ReportHelper.downloadInvoicePDF(current);
        } catch (IOException ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, "Error downloading pdf", ex);
        }
    }

    public void viewPdf() {
        try {
            ReportHelper.viewInvoicePDF(current);
        } catch (IOException ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, "Error viewing pdf", ex);
        }
    }

    public void printInvoice() {
        try {
            ReportHelper.printInvoice(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error printing invoice");
            log.error("Error printing invoice", e);
        }
    }

    private String deriveReturnString(String viewString, boolean addId) {
        if (redirect) {
            String id = "";
            if (addId && current != null && current.getInvoiceId() != null) {
                id = "&id=" + current.getInvoiceId();
            }

            return viewString + "?faces-redirect=true" + id;
        } else {
            return viewString;
        }
    }

    public InvoiceItems getCurrentInvItem() {
        return currentInvItem;
    }

    public void setCurrentInvItem(InvoiceItems currentInvItem) {
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
                id = current.getInvoiceId();
            }
            JsfUtil.getExternalContext().redirect(JsfUtil.getExternalContext().getRequestContextPath()+"/invoice/view.jsf?id=" + id);
        } catch (IOException ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
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
                initNewVehicleToInvoice();
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

    public void initNewVehicleToInvoice() {
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
        if (Utils.isEmpty(current.getInvoiceItems())) {
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

    public void postTransaction() {
        try {
            if(validateTransaction()){

                if(Utils.isEmpty(current.getTransactions())){
                    current.setTransactions(new HashSet<Transaction>()); 
                }
                current.getTransactions().add(currentTransaction);
                
                current = getFacade().postTransaction(currentTransaction);
               
                current = getFacade().find(current.getInvoiceId());              
                
                JsfUtil.addInfoMessage("Payment posted successfully");

            }            
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public void postTransactionAndAddAgain() {
        postTransaction();
        if(!JsfUtil.isErrorRaised()){
            currentTransaction = new Transaction();
        }
    }

    public void postTransactionAndClose() {
        postTransaction();
        
        if(!JsfUtil.isErrorRaised()){
            redirectToView(current.getInvoiceId());
        }
    }

    public void initCurrentTransaction() {
        currentTransaction = new Transaction();
        currentTransaction.setAmount(current.getAmount() - current.getTransactionAmount());
        
        if(currentTransaction.getAmount() < 0.0){
            currentTransaction.setTransType(TransTypeEnum.REFUND.getValue());
        }
        else{
            currentTransaction.setTransType(TransTypeEnum.PAYMENT.getValue());
        }
        
        currentTransaction.setTransDate(new Date());
        currentTransaction.setInvoiceId(current);
        
    }

    public List<SelectItem> getTransTypeList() {
        List<SelectItem> list = new ArrayList();

        for (TransTypeEnum tr : TransTypeEnum.values()) {
            list.add(new SelectItem(tr.getValue(), tr.getValue()));
        }
        return list;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    private boolean validateTransaction() {
        boolean validated = true;
        if (currentTransaction != null) {
            if (currentTransaction.getAmount() == null) {
                validated = false;
                JsfUtil.addErrorMessage("Please add a payment amount.");
            }
            else{
                if (currentTransaction.getAmount() <= 0.0) {
                    validated = false;
                    JsfUtil.addErrorMessage("Payment amount invalid.");
                }
                else if (current.getTransactionAmount() + currentTransaction.getAmount() > current.getAmount()) {
                    validated = false;
                    JsfUtil.addErrorMessage("Payment amount exceeds invoice amount.");
                }
            }
            if (currentTransaction.getTransDate() == null) {
                validated = false;
                JsfUtil.addErrorMessage("Please add a payment date.");
            }
            if (currentTransaction.getTransType() == null) {
                validated = false;
                JsfUtil.addErrorMessage("Please add a payment type.");
            }
        } else {
            validated = false;
        }
        return validated;

    }
    
}
