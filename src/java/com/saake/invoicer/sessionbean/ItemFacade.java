/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.sessionbean;

import com.saake.invoicer.entity.InvoiceItems;
import com.saake.invoicer.entity.Item;
import com.saake.invoicer.util.Utils;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

/**
 *
 * @author jn
 */
@Stateless
public class ItemFacade extends AbstractFacade<Item> {
    @PersistenceContext(unitName = "invoicerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ItemFacade() {
        super(Item.class);
    }

    @Override
    public List<Item> findAll() {
//
//        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
//
//        Root<Item> root = cq.from(Item.class);
//        cq.select(root);
//
//        ParameterExpression<String> status = cb.parameter(String.class);
////        cq.where(cb.notEqual(invRoot.get("status"), status));
//        cq.orderBy(cb.desc(root.get("itemId")));
//        Query query = getEntityManager().createQuery(cq);
////        query.setParameter(status, InvoiceStatusEnum.DELETE.getValue());

        return (List<Item>) em.createQuery("select t from Item t where t.status is null or t.status = 'INACTIVE' order by t.itemId desc", Item.class).getResultList();
    }

    public void softDelete(Item item) {
        item.setStatus("DELETED");

        em.merge(item);
    }

    public void deactivate(Item item) {
        item.setStatus("INACTIVE");

        em.merge(item);
    }

    public List<Item> createItems(List<Item> newItemsList) {
        for (Item item : newItemsList) {
            if(!item.isEmptyForUse()){
                if(Utils.isBlank(item.getItemCategory())){
                    item.setItemCategory("PRODUCT");
                }
                item = save(item);
            }
        }
        
        return newItemsList;
    }    

    public Item saveItem(Item item) {
//        if(!item.isEmptyForUse()){

            item = save(item);
//        }
        
        return item;
    }    
    
    private Item save(Item item) {
        if(item.getItemId()== null){
            item.setCreateTs(new Date());
            em.persist(item);
        }
        else{
            item.setUpdateTs(new Date());
            item = em.merge(item);
        }
        
        return item;
    }
}
