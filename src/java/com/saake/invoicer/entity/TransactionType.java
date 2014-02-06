/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "transaction_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransactionType.findAll", query = "SELECT t FROM TransactionType t"),
    @NamedQuery(name = "TransactionType.findByTranTypeId", query = "SELECT t FROM TransactionType t WHERE t.tranTypeId = :tranTypeId"),
    @NamedQuery(name = "TransactionType.findByCode", query = "SELECT t FROM TransactionType t WHERE t.code = :code"),
    @NamedQuery(name = "TransactionType.findByName", query = "SELECT t FROM TransactionType t WHERE t.name = :name")})
public class TransactionType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "TRANS_TYPE_ID")
    private Integer tranTypeId;
    @Size(max = 25)
    @Column(name = "CODE")
    private String code;
    @Size(max = 50)
    @Column(name = "NAME")
    private String name;
   
    public TransactionType() {
    }

    public TransactionType(Integer tranTypeId) {
        this.tranTypeId = tranTypeId;
    }

    public Integer getTranTypeId() {
        return tranTypeId;
    }

    public void setTranTypeId(Integer tranTypeId) {
        this.tranTypeId = tranTypeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tranTypeId != null ? tranTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionType)) {
            return false;
        }
        TransactionType other = (TransactionType) object;
        if ((this.tranTypeId == null && other.tranTypeId != null) || (this.tranTypeId != null && !this.tranTypeId.equals(other.tranTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.TransactionType[ tranTypeId=" + tranTypeId + " ]";
    }
    
}
