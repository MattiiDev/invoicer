package com.saake.invoicer.controller.masterdata.util;

import com.saake.invoicer.entity.Invoice;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class JsfUtil {

    public static List<SelectItem> getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return Arrays.asList(items);
    }
    
    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessage(String clientId, String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(clientId, facesMsg);
    }
    
    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }


    public static void addWarnMessage(String clientId, String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        FacesContext.getCurrentInstance().addMessage(clientId, facesMsg);
    }

    public static void addInfoMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addInfoMessage(String clientId, String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage(clientId, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }
    
    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getRequestObject(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(key);
    }
    
    public static void addRequestObject(String key, Object obj) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(key, obj);
    }
    
    public static void addRequestParameter(String key, String val) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().put(key, val);
    }
    
    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = com.saake.invoicer.util.JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }
    
    public static String getViewId() {
        return FacesContext.getCurrentInstance().getViewRoot().getViewId();
    }
    
    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static Application getApplication() {
        return getFacesContext().getApplication();
    }

    public static ExternalContext getExternalContext() {
        return getFacesContext().getExternalContext();
    }

    public static ServletContext getServletContext() {
        return (ServletContext) getExternalContext().getContext();
    }

    public static ELContext getELContext() {
        return getFacesContext().getELContext();
    }

    public static ExpressionFactory getExpressionFactory() {
        return getApplication().getExpressionFactory();
    }

    // HttpServletRequest
    public static HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) getExternalContext().getRequest();
    }

    public static void addAttributeInRequest(String attbributeName, Object attribute) {
        getHttpServletRequest().setAttribute(attbributeName, attribute);
    }

    public static Object getAttributeFromRequest(String attbributeName) {
        return getHttpServletRequest().getAttribute(attbributeName);
    }

    public static void removeAttributeFromRequest(String attributeName) {
        getHttpServletRequest().removeAttribute(attributeName);
    }

    // HttpServletResponse
    public static HttpServletResponse getServletResponse() {
        return (HttpServletResponse) getExternalContext().getResponse();
    }

    // Session
    public static HttpSession getHttpSession() {
        return FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getExternalContext() != null?
                (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(Boolean.FALSE):null;
    }

    public static void addAttributeInSession(String attbributeName, Object attribute) {
        getHttpSession().setAttribute(attbributeName, attribute);
    }

    public static Object getAttributeFromSession(String attbributeName) {
        return getHttpSession().getAttribute(attbributeName);
    }

    public static void removeAttributeFromSession(String attributeName) {
        getHttpSession().removeAttribute(attributeName);
    }

    public static MethodExpression createMethodExpression(String methodExpression) {
        return createMethodExpression(methodExpression, String.class);
    }

    public static MethodExpression createMethodExpression(String methodExpression, Class<?> valueType) {
        return getExpressionFactory().createMethodExpression(getELContext(), methodExpression, valueType, new Class<?>[0]);
    }

    public static MethodExpression createMethodListenerExpression(String methodExpression) {
        return getExpressionFactory().createMethodExpression(getELContext(), methodExpression, null, new Class[]{ActionEvent.class});
    }

    public static ValueExpression createValueExpression(String valueExpression, Class<?> valueType) {
        return getExpressionFactory().createValueExpression(getELContext(), valueExpression, valueType);
    }

    public static String getMessage(String key){
        return ResourceBundle.getBundle(getApplication().getMessageBundle()).getString(key);
    }

    public static String getMessage(String key, String appendString){
        return ResourceBundle.getBundle(getApplication().getMessageBundle()).getString(key) + " " + appendString;
    }

    public static String getComponentClientId(String id) {
        try {
            FacesContext fctx = FacesContext.getCurrentInstance();

            UIComponent found = getComponentWithId(fctx.getViewRoot(), id);

            if (found!=null)
                return found.getClientId(fctx);
            else
                return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static UIComponent getComponentWithId(UIComponent parent, String id) {
        for (Iterator<UIComponent> chs = parent.getFacetsAndChildren(); chs.hasNext();) {
            UIComponent ch = chs.next();
            if (ch != null && id.equalsIgnoreCase(ch.getId()))
                return ch;
            else {
                UIComponent found = getComponentWithId(ch, id);
                if (found!=null)
                    return found;
            }
        }

        return null;
    }

    public static UIComponent getComponentContainingId(UIComponent parent, String id) {
        for (Iterator<UIComponent> chs = parent.getFacetsAndChildren(); chs.hasNext();) {
            UIComponent ch = chs.next();
            if (ch.getId().contains(id))
                return ch;
            else {
                UIComponent found = getComponentWithId(ch, id);
                if (found!=null)
                    return found;
            }
        }   return null;
    }

}