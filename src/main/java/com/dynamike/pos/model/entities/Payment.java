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
/**
 *
 * @author bysadmin
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "payments")
public class Payment {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    @Column(name="date")
    private Date date;

    @Column(name = "order_id")
    private String orderId;
    
    @Column(name = "payment_credit")
    private String paymentCredit;

    @Column(name = "balance")
    private String balance;
    
    @Column(name = "payment_due")
    private String paymentDue;
    
//    @Column(name = "payment_type")
//    private String paymentType;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "payment_type")
    private PaymentType paymentType;
    
    @Column(name = "payment_fees")
    private String paymentFees;
    
    @Column(name = "shipping_fees")
    private String shippingFees;
    
//    @Transient
    @Column(name = "commission_fees")
    private String commissionFees;
    
    @Column(name = "other_fees")
    private String otherFees;    
    

	@Column(name = "discount")
    private String discount;
    
    @Column(name = "free_shipping")
    private Boolean freeShipping;
    
//    @Column(name = "provider")
//    private String provider;
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "provider")
    private ProviderSource provider;
    
    @Column(name = "remarks")
    private String remarks;
    
    @Column(name = "month")
    private Integer month;
    
    @Column(name = "year")
    private Integer year;
    
    @Column(name = "status")
    private String status;
    
//    @Column(name = "client")
//    private String client;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "client")
    private Client client;
    
    @Transient
    private List<OrderList> orderList;
    
    public List<OrderList> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderList> orderList) {
        this.orderList = orderList;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getDate() {
    	java.text.DateFormat dateFormat;
    	if(provider.getId().equals(3)) {
    		dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");	
    	}else {
    		dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
    	}
          
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
    
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public String getPaymentCredit() {
        return paymentCredit;
    }

    public void setPaymentCredit(String paymentCredit) {
        this.paymentCredit = paymentCredit;
    }
    
    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
    
    public String getPaymentDue() {
        return paymentDue;
    }

    public void setPaymentDue(String paymentDue) {
        this.paymentDue = paymentDue;
    }
    
    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
    
    public String getPaymentFees() {
        return paymentFees;
    }

    public void setPaymentFees(String paymentFees) {
        this.paymentFees = paymentFees;
    }
    
    public String getCommissionFees() {
		return commissionFees;
	}

	public void setCommissionFees(String commissionFees) {
		this.commissionFees = commissionFees;
	}
    
    public String getOthersFees() {
        return otherFees;
    }

    public void setOthersFees(String otherFees) {
        this.otherFees = otherFees;
    }
    
    public String getShippingFees() {
        return shippingFees;
    }

    public void setShippingFees(String shippingFees) {
        this.shippingFees = shippingFees;
    }
    
    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
    
    public Boolean isFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(Boolean freeShipping) {
        this.freeShipping = freeShipping;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public ProviderSource getProvider() {
        return provider;
    }

    public void setProvider(ProviderSource provider) {
        this.provider = provider;
    }
    
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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
        Payment other = (Payment) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }
}
