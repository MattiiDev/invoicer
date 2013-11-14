/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.sessionbean;

import com.saake.invoicer.entity.User;
import com.saake.invoicer.entity.UserLogin;
import com.saake.invoicer.entity.UserUuidMap;
import com.saake.invoicer.util.Utils;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jn
 */
@Stateless
public class UserFacade{
    private static Log log = LogFactory.getLog(UserFacade.class);

    @PersistenceContext(unitName = "invoicerPU")
    private EntityManager em;

    public User findUser(String id) {
        try{
            return (User)em.createNamedQuery("User.findByUserId").setParameter("userId", id).getSingleResult();
        }
        catch(Exception e){
            log.error("Error finding user:"+id, e);
        }
        
        return null;
    }    

    public User login(String username, String password) {
        UserLogin userLogin = em.find(UserLogin.class, username);
        
        if(userLogin != null){
            if(userLogin.getPassword().equals(password)){
                return userLogin.getUserId();        
            }                    
        }
        return null;
    }

    public void saveUUID(String uuid, User user) {        
        if(Utils.notBlank(uuid) && user != null){
            em.persist(new UserUuidMap(user, uuid, new Date()));
        }        
    }

    public void deleteUUID(User user) {
        if(user != null){
            em.remove(em.find(UserUuidMap.class, user.getId()));
        }
    }
    
    public User findUserForUUID(String uuid) {
        try{
            if(Utils.notBlank(uuid)){
                return (User)em.createNamedQuery("UserUuidMap.findByUuid").setParameter("uuid",uuid).getSingleResult();
            }
        }
        catch(Exception e){
            log.error("Error finding user for uuid:"+uuid, e);
        }
        
        return null;
    }

    public UserLogin getUserLogin(String userId) {
          try{
            if(Utils.notBlank(userId)){
                return (UserLogin)em.createNamedQuery("UserLogin.findByUserId").setParameter("userId",userId).getSingleResult();
            }
        }
        catch(Exception e){
            log.error("Error finding user login for user:"+userId, e);
        }
        
        return null;
    }

    public void register(UserLogin userLogin) {
        userLogin.setCreateTs(new Date());
        em.persist(userLogin);
    }
}
