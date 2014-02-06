/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.controller;

import com.saake.invoicer.entity.User;
import com.saake.invoicer.entity.UserLogin;
import com.saake.invoicer.sessionbean.UserFacade;
import static com.saake.invoicer.util.Constants.COOKIE_AGE;
import static com.saake.invoicer.util.Constants.COOKIE_NAME;
import com.saake.invoicer.util.JsfUtil;
import com.saake.invoicer.util.Utils;
import static com.saake.invoicer.util.Utils.addCookie;
import static com.saake.invoicer.util.Utils.removeCookie;
import com.sun.istack.logging.Logger;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jn
 */
@ManagedBean(name = "menuCtrl")
@ViewScoped
public class MenuController implements Serializable {

    private static final Log log = LogFactory.getLog(MenuController.class);
    
    public String menuItemFocus ;    

    public MenuController() {
        log.info("Inside MenuController!!!");
    }
        
    @PostConstruct
    private void init() {
        
        if (Utils.notBlank(JsfUtil.getViewId())) {
            String[] viewIdArr = JsfUtil.getViewId().split("/");
            
            if(viewIdArr != null && viewIdArr.length > 0){
                String prependStr = "";
                if(viewIdArr[1].contains(".xhtml")){
                    prependStr = viewIdArr[1].split(".xhtml")[0];
                }
                else{
                    prependStr = viewIdArr[1];
                }
                menuItemFocus = prependStr + "Menu";
            }
        }        
    }
    
    public String getMenuItemFocus() {
        return menuItemFocus;
    }

    public void setMenuItemFocus(String menuItemFocus) {
        this.menuItemFocus = menuItemFocus;
    }
    
    
}
