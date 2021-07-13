package com.dynamike.pos.model.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.WhereJoinTable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailReport implements Serializable {


    private Integer month;
    
    private Integer year;        
    
    private float sale;

    private float expenditure;
    
    private float balance;
    
    private Integer lazada;

    private Integer shopee;
  
    private Integer localShop;
    
    private Integer orderCount;
    
    private float stocks;
    
    private float earned;
    
    private float inventoryCash;

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public float getSale() {
		return sale;
	}

	public void setSale(float sale) {
		this.sale = sale;
	}

	public float getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(float expenditure) {
		this.expenditure = expenditure;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public Integer getLazada() {
		return lazada;
	}

	public void setLazada(Integer lazada) {
		this.lazada = lazada;
	}

	public Integer getShopee() {
		return shopee;
	}

	public void setShopee(Integer shopee) {
		this.shopee = shopee;
	}

	public Integer getLocalShop() {
		return localShop;
	}

	public void setLocalShop(Integer localShop) {
		this.localShop = localShop;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	
	public float getStocks() {
		return stocks;
	}

	public void setStocks(float stocks) {
		this.stocks = stocks;
	}
	
	public float getInventoryCash() {
		return inventoryCash;
	}

	public void setInventoryCash(float inventoryCash) {
		this.inventoryCash = inventoryCash;
	}
	
	

	public float getEarned() {
		return earned;
	}

	public void setEarned(float earned) {
		this.earned = earned;
	}
    
    
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        if (getClass() != obj.getClass())
//            return false;
//        DetailReport other = (DetailReport) obj;
//        if (getId() == null) {
//            if (other.getId() != null)
//                return false;
//        } else if (!getId().equals(other.getId()))
//            return false;
//        return true;
//    }
}
