/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "user_uuid_map")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserUuidMap.findAll", query = "SELECT u FROM UserUuidMap u"),
    @NamedQuery(name = "UserUuidMap.findByUserId", query = "SELECT u FROM UserUuidMap u WHERE u.userId = :userId"),
    @NamedQuery(name = "UserUuidMap.findByUuid", query = "SELECT u FROM UserUuidMap u WHERE u.uuid = :uuid"),
    @NamedQuery(name = "UserUuidMap.findByCreateTs", query = "SELECT u FROM UserUuidMap u WHERE u.createTs = :createTs")})
public class UserUuidMap implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JoinColumn(name = "user_id")
    @OneToOne
    private User userId;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "create_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTs;

    public UserUuidMap() {
    }

    public UserUuidMap(User userId, String uuid, Date createTs) {
        this.userId = userId;
        this.uuid = uuid;
        this.createTs = createTs;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }   

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserUuidMap)) {
            return false;
        }
        UserUuidMap other = (UserUuidMap) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.UserUuidMap[ userId=" + userId + " ]";
    }
    
}
