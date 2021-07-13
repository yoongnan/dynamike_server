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
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "purchases")
public class Account {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name="date")
    private Date date;

    
    @Column(name = "particular")
    private String particular;

    @Column(name = "invoice_no")
    private String invoiceNo;
    
    

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "type")
    private InvoiceType type;
    
//    @Column(name = "type")
//    private String type;
    
    @Column(name = "total_amount")
    private String totalAmount;    
    
    @Column(name = "month")
    private Integer month;
    
    @Column(name = "year")
    private Integer year;
    
    @Column(name = "paid")
    private Boolean paid;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getDate() {
//        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
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
    
    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }
    
    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
    
    public InvoiceType getType() {
        return type;
    }

    public void setType(InvoiceType type) {
        this.type = type;
    }
    
    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
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
    
    public Boolean isPaid() {
        return this.paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
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
        Account other = (Account) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }
}
