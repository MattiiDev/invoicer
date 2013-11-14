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
@ManagedBean(name = "loginCtrl")
@ViewScoped
public class LoginController implements Serializable {

    private static final Log log = LogFactory.getLog(LoginController.class);
    private UserLogin userLogin;
    private String password2;
    private Boolean remember = true;
    @EJB
    UserFacade userFacade;

    public LoginController() {
        log.info("Inside LoginController!!!");
    }
    
    public String login() {
        HttpServletRequest req = JsfUtil.getHttpServletRequest();
        String refUrl = req.getServletPath();

        try {
            if (userLogin != null) {
                if (userLogin.getUserId() != null && Utils.notBlank(userLogin.getUserId().getUserId())){
                    
                  req.login(userLogin.getUserId().getUserId(), userLogin.getPassword());
                    
                    User user = userFacade.findUser(userLogin.getUserId().getUserId());

                    if (user != null) {
                        req.getSession().setAttribute("user", user);

                        if (remember) {
                            String uuid = UUID.randomUUID().toString();
                            userFacade.saveUUID(uuid, user);
                            addCookie(JsfUtil.getServletResponse(), COOKIE_NAME, uuid, COOKIE_AGE);
                        } else {
                            userFacade.deleteUUID(user);
                            removeCookie(JsfUtil.getServletResponse(), COOKIE_NAME);
                        }
                    }
                }
                else{
                    JsfUtil.addErrorMessage("Please enter user name!");
                }
            }
        } catch (Exception se) {
            Logger.getLogger(this.getClass()).log(Level.SEVERE, "Error logging in", se);
            JsfUtil.addErrorMessage("Error logging in." + se.getMessage());
        }
        
        return Utils.notBlank(refUrl) ? refUrl : "/";
    }

    public String logout() {
        log.info("Inside logout");
        HttpServletRequest req = JsfUtil.getHttpServletRequest();

        try {
            req.logout();
            req.getSession().invalidate();
            JsfUtil.getExternalContext().invalidateSession();
            req.getSession(false).removeAttribute("user");
            req.getSession(false).removeAttribute("initialLogin");
//            JsfUtil.getServletResponse().sendRedirect("/invoicer/login.jsf");                          
        } catch (Exception e) {
            log.error("Error logging out", e);
            JsfUtil.addErrorMessage("Logout failed");
        }
        
        return "/login.jsf?faces-redirect=true";
    }

    public void registerListener() {
        userLogin = new UserLogin();
        userLogin.setUserId(new User());
    }

    public String register() {
        HttpServletRequest req = JsfUtil.getHttpServletRequest();

        if (userLogin.getUserId() != null) {
            if (Utils.isBlank(userLogin.getUserId().getUserId())) {
                JsfUtil.addErrorMessage("Please choose a user name");
            } else if (Utils.isBlank(userLogin.getPassword()) || Utils.isBlank(password2)) {
                JsfUtil.addErrorMessage("Please type in a password");
            } else if (!userLogin.getPassword().equals(password2)) {
                JsfUtil.addErrorMessage("Passwords should match");
            } else {
                userFacade.register(userLogin);
            }
        }

        JsfUtil.addAttributeInRequest("remember", remember);
        JsfUtil.addAttributeInRequest("username", remember);
        JsfUtil.addAttributeInRequest("password", remember);

        return "login.jsf?faces-redirect=true";

    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public Boolean getRemember() {
        return remember;
    }

    public void setRemember(Boolean remember) {
        this.remember = remember;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }
}
