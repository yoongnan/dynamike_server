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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Lob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 *
 * @author bysadmin
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "stock_check")
public class StockCheck {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="date")
    private Date date;

    @Column(name = "product_id")
    private String productId;
    
    @Column(name = "quantity")
    private String quantity;
    
    @Column(name = "unit_cost")
    private float unitCost;
    
    @Column(name = "total")
    private float total;
    
    
    @Column(name = "month")
    private Integer month;
    
    @Column(name = "year")
    private Integer year;
    
    @Column(name = "status")
    private String status;
 
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getDate() {
    	java.text.DateFormat dateFormat;
    	dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
    	
          
        String strDate = dateFormat.format(date);  
        return strDate;
    }

    public void setDate(Date date) {
        this.date = date;
        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("MM");  
        String strMonth = dateFormat.format(date);  
        dateFormat = new java.text.SimpleDateFormat("yyyy");  
        String strYear = dateFormat.format(date);  
        setMonth(Integer.valueOf(strMonth));
        setYear(Integer.valueOf(strYear));
    }
    
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMonth() {
        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("MM");  
        String strMonth = dateFormat.format(date);  
        return strMonth;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
    
    public String getYear() {
        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy");  
        String strYear = dateFormat.format(date);  
        return strYear;
    }

    public void setYear(Integer year) {
        this.year = year;
    }   
        
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StockCheck other = (StockCheck) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public float getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(float unitCost) {
		this.unitCost = unitCost;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}
}
