package com.saake.invoicer.controller;

import com.saake.invoicer.controller.InvoiceController;
import com.saake.invoicer.entity.Item;
import com.saake.invoicer.controller.masterdata.util.JsfUtil;
import com.saake.invoicer.controller.masterdata.util.PaginationHelper;
import com.saake.invoicer.entity.Customer;
import com.saake.invoicer.sessionbean.ItemFacade;
import com.saake.invoicer.util.Utils;
import java.io.IOException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

@ManagedBean(name = "itemController")
@ViewScoped
public class ItemController implements Serializable {

    private Item current;
    private List<Item> items = null;
    List<Item> suggestItemList = new ArrayList<>();

 
    @EJB
    private ItemFacade ejbFacade;
    private int selectedItemIndex;

    private boolean redirect = false;

    public ItemController() {
    }

    @PostConstruct
    private void initialize() {
        if ( JsfUtil.getViewId().contains("create")){
            newItemInit();
        }
        else
        if ( JsfUtil.getViewId().contains("edit")){
            editItemInit();
        }
        else
        if ( JsfUtil.getViewId().contains("view")){
            viewItemInit();
        }
        else
        if ( JsfUtil.getViewId().contains("list")){
            prepareList();
        }        
        
        redirect = false;
    }
    
    public Item getSelected() {
        if (current == null) {
            current = new Item();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ItemFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        recreateModel();
        return "list";
    }

    public String prepareView() {
//        current = (Item) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "view";
    }

    public String prepareCreate() {
        current = new Item();
        selectedItemIndex = -1;
        return "create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ItemCreated"));
            return "view.jsf?faces-redirect=true&id="+current.getItemId();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public Item create(Item item) {
        try {
            getFacade().create(item);            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ItemCreated"));
            return item;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
//        current = (Item) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ItemUpdated"));
            return "view.jsf?faces-redirect=true&id="+current.getItemId();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String softDelete() {
        try {
            getFacade().softDelete(current);

            JsfUtil.addSuccessMessage("Item Deleted");

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }

        return "list?faces-redirect=true";
    }

    public List<Item> getItems() {
        if(items == null){
            items = ejbFacade.findAll();
        }
        return items;
    }

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

    public List<SelectItem> getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public List<SelectItem> getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Item getItem(java.lang.Integer id) {
        if(id != null){
            return ejbFacade.find(id);
        }
        else{
            return new Item();
        }
        
    }

        
    public List<Item> suggestItem(String inp){

        suggestItemList.clear();

//        suggestItemList.add(new Item(9999,"Add New Item..."));
        
        if (Utils.notBlank(inp)) {
            for (Item item : getItems()) {
                if (Utils.notBlank(item.getDescription())
                        && item.getDescription().toLowerCase().contains(inp.toString().trim().toLowerCase())) {

                    suggestItemList.add(item);
                } else if (Utils.notBlank(item.getName())
                        && item.getName().toLowerCase().contains(inp.toString().trim().toLowerCase())) {

                    suggestItemList.add(item);
                } 
            }
        }
        else{
            suggestItemList.addAll(getItems());
        }
       
        if (Utils.isEmpty(suggestItemList)) {
            JsfUtil.addInfoMessage("ordItemTable", "No Items found");
        }
        
        return suggestItemList;
    }    
    
    public void itemSelectedListener(Item item){
        System.out.println("inside itemSelectListener");
        
        current = item;
        if(item != null && item.getItemId() == 9999){
            System.out.println("inside new item");
        }
    }

    public Item getCurrent() {
        return current;
    }

    public void setCurrent(Item current) {
        this.current = current;
    }

    private void newItemInit() {
        current = new Item();
        current.setCreateTs(new Date());
    }

    private void viewItemInit() {
        String id = JsfUtil.getRequestParameter("id");
        
        if(Utils.notBlank(id)){
            current = getFacade().find(Integer.parseInt(id));                      
        }   
    }

    private void editItemInit() {
        viewItemInit();
    }
      
    public void redirectToView(Integer id) {
        try {
            if(id == null || id == 0){
                id = current.getItemId();
            }
            JsfUtil.getExternalContext().redirect("view.jsf?id="+id);
        } catch (IOException ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }            
}
