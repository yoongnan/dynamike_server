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
public class CapitalReport implements Serializable {


//    @Column(name = "month")
    private Integer month;
    
//    @Column(name = "year")
    private Integer year;    

//    @Column(name = "product_type")
    private float cash;

//  @Column(name = "weight")
    private float inventory;
    
//    @Column(name = "unit_cost")
    private float inventorycash;
  
//    @Column(name = "weight")
    private float dividend;
    
//    @Column(name = "description")
    private float investment;

//  @Column(name = "description")
    private float openAccount;
    
    private float expentidure;
    
    private float sales;
    
    private float earned;
    
    private float bringforward;
    
    private float totalCapital;

    public float getBringForward() {
		return bringforward;
	}

	public void setBringForward(float bringforward) {
		this.bringforward = bringforward;
//		this.inventorycash = float.valueOf(Double.parseDouble(inventory) + Double.parseDouble(cash));
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

	public float getCash() {
		return cash;
	}

	public void setCash(float cash) {
		this.cash = cash;
//		this.inventorycash = float.valueOf(Double.parseDouble(inventory) + Double.parseDouble(cash));
	}
	
	public float getEarned() {
		return earned;
	}

	public void setEarned(float earned) {
		this.earned = earned;
//		this.inventorycash = float.valueOf(Double.parseDouble(inventory) + Double.parseDouble(cash));
	}

	public float getInventory() {
		return inventory;
	}

	public void setInventory(float inventory) {
		this.inventory = inventory;
//		this.inventorycash = float.valueOf(Double.parseDouble(inventory) + Double.parseDouble(cash));
	}

	public float getInventorycash() {
		return inventorycash;
	}

	public void setInventorycash(float inventorycash) {
		this.inventorycash = inventorycash;
	}

	public float getDividend() {
		return dividend;
	}

	public void setDividend(float dividend) {
		this.dividend = dividend;
	}

	public float getInvestment() {
		return investment;
	}

	public void setInvestment(float investment) {
		this.investment = investment;
	}

	public float getOpenAccount() {
		return openAccount;
	}

	public void setOpenAccount(float openAccount) {
		this.openAccount = openAccount;
	}
	
	public void setExpenditure(float expentidure) {
		this.expentidure = expentidure;
	}	
	
	public float getExpenditure() {
		return expentidure;
	}
	
	public void setSales(float sales) {
		this.sales = sales;
	}	
	
	public float getSales() {
		return sales;
	}
	
	public void setTotalCapital(float totalCapital) {
		this.totalCapital = totalCapital;
	}	
	
	public float getTotalCapital() {
		return totalCapital;
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
