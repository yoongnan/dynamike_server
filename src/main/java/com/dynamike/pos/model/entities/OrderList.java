/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamike.pos.model.entities;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Lob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 *
 * @author bysadmin
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "order_items")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderList {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

//    @Column(name = "order_id")
//    private String orderId;
    
    @Column(name = "item_id")
    private String itemId;

    @Transient
    private String index;
	@Transient
    private String name;
    
	@Column(name = "quantity")
    private Integer quantity;
    
//    @Transient
    @Column(name = "selling_price")
    private String sellingPrice;
    
    @Column(name = "unit_price")
    private String unitPrice;

	@Column(name = "total_price")
    private String totalPrice;
    
    @Column(name = "invoice_id")
    private String invoiceId;
    
    public String getId() {
    	return id;
    }

    public void setId(String id) {
    	this.id = id;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

    
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
        
    public String getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(String sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	
    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }
    
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
     
    @Override
    public int hashCode() {
        int hash = 3;       
        hash = 53 * hash
                + ((getItemId() == null) ? 0 : getItemId().hashCode());
        hash = 53 * hash + ((getId() == null) ? 0 : getId().hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OrderList other = (OrderList) obj;
        if (getItemId() == null) {
            if (other.getItemId() != null)
                return false;
        } else if (!getItemId().equals(other.getItemId()))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
}
