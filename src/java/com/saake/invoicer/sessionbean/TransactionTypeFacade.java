/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.sessionbean;

import com.saake.invoicer.entity.TransactionType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jn
 */
@Stateless
public class TransactionTypeFacade extends AbstractFacade<TransactionType> {
    @PersistenceContext(unitName = "invoicerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TransactionTypeFacade() {
        super(TransactionType.class);
    }
    
}
