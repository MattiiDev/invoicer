package com.saake.invoicer.controller.masterdata;

import com.saake.invoicer.entity.Item;
import com.saake.invoicer.controller.masterdata.util.JsfUtil;
import com.saake.invoicer.controller.masterdata.util.PaginationHelper;
import com.saake.invoicer.entity.Customer;
import com.saake.invoicer.sessionbean.ItemFacade;
import com.saake.invoicer.util.Utils;

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

@ManagedBean(name = "itemController")
@ViewScoped
public class ItemController implements Serializable {

    private Item current;
    private List<Item> items = null;
    List<Item> suggestItemList = new ArrayList<>();

 
    @EJB
    private ItemFacade ejbFacade;
    private int selectedItemIndex;

    public ItemController() {
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
        return "List";
    }

    public String prepareView() {
//        current = (Item) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Item();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ItemCreated"));
            return prepareCreate();
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
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ItemUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
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
      
        @FacesConverter(forClass = Item.class)
    public static class ItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ItemController controller = (ItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "itemController");
            return controller.getItem(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            if(Utils.notBlank(value)){
                java.lang.Integer key;
                key = Integer.valueOf(value);
                return key;
            }
            else{
                return null;
            }
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
            if (object instanceof Item) {
                Item o = (Item) object;
                return getStringKey(o.getItemId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Item.class.getName());
            }
        }
    }
}
