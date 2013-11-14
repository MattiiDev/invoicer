/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.servlet;

import com.saake.invoicer.controller.masterdata.util.JsfUtil;
import com.saake.invoicer.entity.User;
import com.saake.invoicer.entity.UserLogin;
import com.saake.invoicer.sessionbean.UserFacade;
import com.sun.istack.logging.Logger;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.saake.invoicer.util.Constants.*;        
import static com.saake.invoicer.util.Utils.*;        
/**
 *
 * @author jn
 */
//@WebServlet(name="LoginServlet", urlPatterns={"/"})
public class LoginServlet extends HttpServlet {


    @EJB
    UserFacade userFacade;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserLogin userLogin = (UserLogin)JsfUtil.getAttributeFromRequest("userLogin");
        
        String username = userLogin.getUserId().getUserId();
        String password = userLogin.getPassword();
        
        boolean remember = "true".equals(request.getParameter("remember"));
        
        try {
            if (request.getUserPrincipal() == null) {
                request.login(username, password); // Password should already be the hashed variant.
            }

            User user = userFacade.findUser(username);

            if (user != null) {
                request.getSession().setAttribute("user", user);

                if (remember) {
                    String uuid = UUID.randomUUID().toString();
                    userFacade.saveUUID(uuid, user);
                    addCookie(response, COOKIE_NAME, uuid, COOKIE_AGE);
                } else {
                    userFacade.deleteUUID(user);
                    removeCookie(response, COOKIE_NAME);
                }
            }
        } catch (Exception se) {
            Logger.getLogger(this.getClass()).log(Level.SEVERE, "Error logging in", se);
            JsfUtil.addErrorMessage("Error logging in." + se.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
}
