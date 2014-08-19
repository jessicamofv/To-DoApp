/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_web_app.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jessica
 */
@Entity
@Table(name = "LISTITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ListItem.findAll", query = "SELECT l FROM ListItem l"),
    @NamedQuery(name = "ListItem.findById", query = "SELECT l FROM ListItem l WHERE l.id = :id"),
    @NamedQuery(name = "ListItem.findByItem", query = "SELECT l FROM ListItem l WHERE l.appUserId.id = :appUserId AND l.item = :item"),
    @NamedQuery(name = "ListItem.findByListDate", query = "SELECT l FROM ListItem l WHERE l.appUserId.id = :appUserId AND l.listDate = :listDate"),
    @NamedQuery(name = "ListItem.findByItemNum", query = "SELECT l FROM ListItem l WHERE l.appUserId.id = :appUserId AND l.itemNum = :itemNum"),
    @NamedQuery(name = "ListItem.findByDifficulty", query = "SELECT l FROM ListItem l WHERE l.appUserId.id = :appUserId AND l.difficulty = :difficulty"),
    @NamedQuery(name = "ListItem.findByAddTime", query = "SELECT l from ListItem l WHERE l.appUserId.id = :appUserId AND l.addTime = :addTime"),
    @NamedQuery(name = "ListItem.findByStatus", query = "SELECT l FROM ListItem l WHERE l.appUserId.id = :appUserId AND l.status = :status"),
    @NamedQuery(name = "ListItem.findByAppUserId", query = "SELECT l FROM ListItem l WHERE l.appUserId.id = :appUserId")})
public class ListItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ITEM")
    private String item;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "LISTDATE")
    private String listDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ITEMNUM")
    private int itemNum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "DIFFICULTY")
    private String difficulty;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ADDTIME")
    private long addTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "STATUS")
    private String status;
    @JoinColumn(name = "APPUSERID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AppUser appUserId;

    public ListItem() {
    }

    public ListItem(Integer id) {
        this.id = id;
    }

    public ListItem(Integer id, String item, String listDate, int itemNum, String difficulty, long addTime, String status) {
        this.id = id;
        this.item = item;
        this.listDate = listDate;
        this.itemNum = itemNum;
        this.difficulty = difficulty;
        this.addTime = addTime;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getListDate() {
        return listDate;
    }

    public void setListDate(String listDate) {
        this.listDate = listDate;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public long getAddTime() {
        return addTime;
    }
    
    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AppUser getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(AppUser appUserId) {
        this.appUserId = appUserId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ListItem)) {
            return false;
        }
        ListItem other = (ListItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "my_web_app.entities.ListItem[ id=" + id + " ]";
    }
    
}
