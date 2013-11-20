/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.controller;

import com.saake.invoicer.model.SearchInvoiceVO;
import com.saake.invoicer.controller.masterdata.CustomerController;
import com.saake.invoicer.controller.masterdata.ItemController;
import com.saake.invoicer.controller.masterdata.util.JsfUtil;
import com.saake.invoicer.entity.Customer;
import com.saake.invoicer.entity.CustomerVehicle;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import com.saake.invoicer.entity.Invoice;
import com.saake.invoicer.entity.InvoiceItems;
import com.saake.invoicer.entity.Item;
import com.saake.invoicer.model.InvoiceDataModel;
import com.saake.invoicer.reports.ReportHelper;
import com.saake.invoicer.sessionbean.InvoiceFacade;
import com.saake.invoicer.util.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
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
//    private Transactions orderTransaction;
//    private Customer orderCustomer;

    private CustomerVehicle custVehicle = new CustomerVehicle();
    
    private List<Invoice> originalInvoiceList = null;
    private List<Invoice> invoiceList = null;
    
    @Inject
    CustomerController custCtrl;
    
    @Inject
    ItemController itemCtrl;
    
    @EJB
    private com.saake.invoicer.sessionbean.InvoiceFacade ejbFacade;

    @EJB
    private com.saake.invoicer.sessionbean.ItemFacade itemFacade;

    private boolean redirect = false;

    public InvoiceController() {
        log.info("Inside InvoiceController!!!");
        Object inv = JsfUtil.getRequestObject("item");
        
        if(inv != null && inv instanceof Invoice){
            current = (Invoice)inv;
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
        if ( JsfUtil.getViewId().contains("create")){
            initNewInvoice();
        }
        else
        if ( JsfUtil.getViewId().contains("edit")){
            editInvoiceInit();
        }
        else
        if ( JsfUtil.getViewId().contains("view")){
            viewInvoiceInit();
        }
        else
        if ( JsfUtil.getViewId().contains("list")){
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

    public String prepareView() {
//        current = (Customer) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        if(current != null && current.getCustomerId() != null && Utils.notEmpty(current.getCustomerId().getCustomerVehicles())){
            custVehicle = current.getCustomerId().getCustomerVehicles().get(0);
        }
        return deriveReturnString("view", true);

    }
    
    private void recreateModel() {
        invoiceList = null;
    }

    public void initNewInvoice(){
        current = new Invoice();
        current.setInvoiceItems(new ArrayList<InvoiceItems>());
        addNewItemToInvoice();
        current.setInvoiceDate(new Date());
        current.setAmount(0.0);
        current.setDiscount(0.0);
        custVehicle = new CustomerVehicle();
    }
    
    public String prepareCreate() {
        initNewInvoice();
        return deriveReturnString("create", false);
    }

    public String save() {
        if(current != null){
            if(current.getInvoiceId() != null){
                return update();
            }
            else{
                return create();
            }
        }
        else{
            return null;
        }
    }
    
    public String create() {
        try {
            addCustumerVehicle(current.getCustomerId());
            
            getFacade().create(current);
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
        
        if(current != null){
            if(Utils.notEmpty(current.getInvoiceItems())){
                for(InvoiceItems invItm:current.getInvoiceItems()){
                    if(invItm.getItem() == null){
                        invItm.setItem(new Item(0.00));                        
                    }
                }
            }
        }
        JsfUtil.addAttributeInRequest("item", current);
        
        redirect = true;

        return deriveReturnString("edit",true);
        
    }

    public String update() {
        try {
            addCustumerVehicle(current.getCustomerId());
            
            current = getFacade().edit(current);
            
            JsfUtil.addRequestObject("item",current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("InvoiceUpdated"));
            return deriveReturnString("view", true);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }    
            
    public String delete(){
        try {
            getFacade().remove(current);
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("InvoiceDeleted"));
            prepareList();
                        
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));            
        }
        
        return deriveReturnString("list", false);
    }
    
    public String addNewItemToInvoice(){
        InvoiceItems it = new InvoiceItems();
        it.setAmount(0.00);
        it.setDiscount(0.00);
        it.setUnitPrice(0.00);
        it.setItem(new Item());
        it.getItem().setUnitCost(0.00);
        it.setInvoice(current);
        it.setQuantity(1);
        current.getInvoiceItems().add(it);                
        return null;
    }
    
    public String removeOrderItem(InvoiceItems ordItm){
        if (ordItm != null){     
            if(current.getInvoiceItems().size() > 1){
                current.getInvoiceItems().remove(ordItm);
            }

            calOrderPrice();
        }
        
        return null;
    }
    public String calOrderPrice(){        
        Double amt = current.getItemTotalAmount();
        
        if(amt != null){
            if(current.getDiscount() != null && current.getDiscount() > 0){
                amt = amt - current.getDiscount();
            }
            current.setAmount(amt);
        }
        else{
            current.setAmount(0.0);
        }

        return null;
    }
    
    public void calculateOrderItemPrice(InvoiceItems ordItm) {
        if ( ordItm != null && ordItm.getItem() != null && ordItm.getItem().getUnitCost() != null &&  ordItm.getQuantity()!= null) {
            ordItm.setAmount((ordItm.getItem().getUnitCost()* ordItm.getQuantity()) - (ordItm.getDiscount() != null? ordItm.getDiscount() * ordItm.getQuantity() : 0));
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
        if(invoiceList == null){
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
        if(current == null){
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
        
        if(Utils.notBlank(id)){
            current = getFacade().find(Integer.parseInt(id));                      
        }   
        
        if (current != null && current.getCustomerId() != null && Utils.notEmpty(current.getCustomerId().getCustomerVehicles())) {
            custVehicle = current.getCustomerId().getCustomerVehicles().get(0);
        }
    }
       
    public void filterList(){   
            if(!filterCriteria.empty()){
                invoiceList.clear();
                
                for(Invoice inv : originalInvoiceList){
                    if(Utils.notBlank(filterCriteria.getInvoicePeriod())){  
                        if("today".equalsIgnoreCase(filterCriteria.getInvoicePeriod())){
                            if(inv.getInvoiceDate() != null && (inv.getInvoiceDate().equals(new Date()))){
                                invoiceList.add(inv);
                            }
                        }
                        else if("yest".equalsIgnoreCase(filterCriteria.getInvoicePeriod())){
//                            if(Utils.notBlank(inv.getStatus()) && inv.getStatus().equalsIgnoreCase(filterCriteria.getStatus())){
//                                invoiceList.add(inv);
//                            }
                        }
                    }
                    else 
                    if(Utils.notBlank(filterCriteria.getStatus())){                                            
                        if(Utils.notBlank(inv.getStatus()) && inv.getStatus().equalsIgnoreCase(filterCriteria.getStatus())){
                            invoiceList.add(inv);
                        }
                    }
                    else if(filterCriteria.getCustomer() != null && filterCriteria.getCustomer().getCustomerId() != null){
                        if(inv.getCustomerId() != null && inv.getCustomerId().getCustomerId() != null &&
                                inv.getCustomerId().getCustomerId().equals(filterCriteria.getCustomer().getCustomerId())){
                            invoiceList.add(inv);                            
                        }
                    }
                    else if(filterCriteria.getFromAmount()!= null && filterCriteria.getToAmount()!= null){
                        if(inv.getAmount()!= null && inv.getAmount() >= filterCriteria.getFromAmount() && 
                                inv.getAmount() <= filterCriteria.getToAmount()){
                            invoiceList.add(inv);                            
                        }
                    }
                    else if(filterCriteria.getFromAmount() != null){
                        if(inv.getAmount()!= null && inv.getAmount() >= filterCriteria.getFromAmount()){
                            invoiceList.add(inv);                            
                        }
                    }
                    else if(filterCriteria.getToAmount() != null){
                        if(inv.getAmount()!= null && inv.getAmount() <= filterCriteria.getToAmount()){
                            invoiceList.add(inv);                            
                        }
                    }
                    else if(filterCriteria.getFromDate() != null && filterCriteria.getToDate() != null){
                        if(inv.getInvoiceDate() != null && 
                                (filterCriteria.getFromDate().after(inv.getInvoiceDate()) || filterCriteria.getFromDate().equals(inv.getInvoiceDate())) && 
                                (filterCriteria.getToDate().before(inv.getInvoiceDate()) || filterCriteria.getToDate().equals(inv.getInvoiceDate()))){
                            invoiceList.add(inv);                            
                        }
                    }
                    else if(filterCriteria.getFromDate() != null ){
                        if(inv.getInvoiceDate() != null && 
                                (filterCriteria.getFromDate().after(inv.getInvoiceDate()) || filterCriteria.getFromDate().equals(inv.getInvoiceDate()))){
                            invoiceList.add(inv);
                        }
                    }
                    else if(filterCriteria.getToDate() != null ){
                        if(inv.getInvoiceDate() != null && 
                                (filterCriteria.getToDate().before(inv.getInvoiceDate()) || filterCriteria.getToDate().equals(inv.getInvoiceDate()))){
                            invoiceList.add(inv);
                        }
                    }                                
                }
            }                                          
    }

    public void resetSearch(){
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
        if(model == null){
            model = new InvoiceDataModel(getInvoiceList());
        }
        return model;
    }

    public void setModel(InvoiceDataModel model) {
        this.model = model;
    }        
    
    public void addNewCustomerToInvoice(){
        log.info("inside addNewCustomerToInvoice");
        
        Customer cust = custCtrl.create(custCtrl.getCurrent());        
        current.setCustomerId(cust);        
    }

    public void createNewItemAndAddToInvoice(){
        log.info("inside createNewItemAndAddToInvoice");
        
//        Item item = itemCtrl.create(itemCtrl.getCurrent());
        currentInvItem.setItem(itemCtrl.create(itemCtrl.getCurrent()));        
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
        JsfUtil.addAttributeInSession("invoice",current);
    }
        
    public void downloadPdf() {
        try {
            ReportHelper.downloadPDF(current);
        } catch (IOException ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, "Error downloading pdf", ex);
        }
    }

    public void viewPdf() {
        try {
            ReportHelper.viewPDF(current);
        } catch (IOException ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, "Error viewing pdf", ex);
        }
    }
    
    public void printInvoice(){
        try{
            ReportHelper.printInvoice(current);
        }
        catch(Exception e){
            JsfUtil.addErrorMessage("Error printing invoice");
            log.error("Error printing invoice",e);
        }
    }

    private String deriveReturnString(String viewString, boolean addId) {
        if(redirect){
            String id = "";
            if(addId && current != null && current.getInvoiceId() != null){
                id="&id="+current.getInvoiceId();
            }
            
            return viewString+"?faces-redirect=true"+ id;
        }
        else{
            return viewString;
        }
    }

    public InvoiceItems getCurrentInvItem() {
        return currentInvItem;
    }

    public void setCurrentInvItem(InvoiceItems currentInvItem) {
        this.currentInvItem = currentInvItem;
    }

    public CustomerVehicle getCustVehicle() {
        return custVehicle;
    }

    public void setCustVehicle(CustomerVehicle custVehicle) {
        this.custVehicle = custVehicle;
    }    

    private Customer addCustumerVehicle(Customer cust) {
        if(!custVehicle.isEmpty()){
            custVehicle.setCustomerId(cust);
            if(cust.getCustomerVehicles() == null){
                cust.setCustomerVehicles(new ArrayList<CustomerVehicle>());
            }
                            
            custVehicle = custCtrl.saveCustomerVehicle(custVehicle);            
        }
        
        return cust;
    }
}
