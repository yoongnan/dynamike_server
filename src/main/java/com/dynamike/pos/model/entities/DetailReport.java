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
    
    private float cogs;
    
    private float lazadaSales;
    
    private float lazadaFees;
    
    private float lazadacogs;
    
    private float shopeeSales;
    
    private float shopeeFees;
    
	private float shopeecogs;
    
    private float packages;
    
    private float advertisement;
    
    private float refund;
    
    private float stockloss;

    private float monthExpenditure;
    
    private float expenditure;
    
    private float balance;
    
    private Integer lazada;

    private Integer shopee;
  
    private Integer localShop;
    
    private Integer orderCount;
    
    private float stocks;
    
    private float earned;
    
	private float inventoryCash;
    
    private float paymentFees;
    
    private float serviceFees;
    
    private float commissionFees;
    
    private float shippingFees;

	private float otherFees;
    
    private float netsales;


    
    public float getNetsales() {
		return netsales;
	}

	public void setNetsales(float netsales) {
		this.netsales = netsales;
	}

    public float getPaymentFees() {
		return paymentFees;
	}

	public void setPaymentFees(float paymentFees) {
		this.paymentFees = paymentFees;
	}

	public float getServiceFees() {
		return serviceFees;
	}

	public void setServiceFees(float serviceFees) {
		this.serviceFees = serviceFees;
	}

	public float getCommissionFees() {
		return commissionFees;
	}

	public void setCommissionFees(float commissionFees) {
		this.commissionFees = commissionFees;
	}

	public float getShippingFees() {
		return shippingFees;
	}

	public void setShippingFees(float shippingFees) {
		this.shippingFees = shippingFees;
	}

	public float getOtherFees() {
		return otherFees;
	}

	public void setOtherFees(float otherFees) {
		this.otherFees = otherFees;
	}
	
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

	
	public float getMonthExpenditure() {
		return monthExpenditure;
	}

	public void setMonthExpenditure(float monthExpenditure) {
		this.monthExpenditure = monthExpenditure;
	}

	public float getExpenditure() {
		return expenditure;
	}
	
	public float getCogs() {
		return cogs;
	}

	public void setCogs(float cogs) {
		this.cogs = cogs;
	}

	public float getLazadaSales() {
		return lazadaSales;
	}

	public void setLazadaSales(float lazadaSales) {
		this.lazadaSales = lazadaSales;
	}

	public float getLazadaFees() {
		return lazadaFees;
	}

	public void setLazadaFees(float lazadaFees) {
		this.lazadaFees = lazadaFees;
	}

	public float getLazadacogs() {
		return lazadacogs;
	}

	public void setLazadacogs(float lazadacogs) {
		this.lazadacogs = lazadacogs;
	}

	public float getShopeeSales() {
		return shopeeSales;
	}

	public void setShopeeSales(float shopeeSales) {
		this.shopeeSales = shopeeSales;
	}

	public float getShopeeFees() {
		return shopeeFees;
	}

	public void setShopeeFees(float shopeeFees) {
		this.shopeeFees = shopeeFees;
	}

	public float getShopeecogs() {
		return shopeecogs;
	}

	public void setShopeecogs(float shopeecogs) {
		this.shopeecogs = shopeecogs;
	}

	public float getPackages() {
		return packages;
	}

	public void setPackages(float packages) {
		this.packages = packages;
	}

	public float getAdvertisement() {
		return advertisement;
	}

	public void setAdvertisement(float advertisement) {
		this.advertisement = advertisement;
	}

	public float getRefund() {
		return refund;
	}

	public void setRefund(float refund) {
		this.refund = refund;
	}

	public float getStockloss() {
		return stockloss;
	}

	public void setStockloss(float stockloss) {
		this.stockloss = stockloss;
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
