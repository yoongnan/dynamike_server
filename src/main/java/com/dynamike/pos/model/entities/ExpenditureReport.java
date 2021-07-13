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
public class ExpenditureReport implements Serializable {


//    @Column(name = "month")
    private Integer month;
    
//    @Column(name = "year")
    private Integer year;    

//    @Column(name = "product_type")
    private float inventory;

//  @Column(name = "weight")
    private float packages;
    
//    @Column(name = "unit_cost")
    private float advertise;
  
//    @Column(name = "weight")
    private float equipment;
    
//    @Column(name = "description")
    private float lost;

//  @Column(name = "description")
    private float refund;	
    
//  @Column(name = "dimension")
    private float subsidy;

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
	
	public float getInventory() {
		return inventory;
	}
	
	public void setInventory(float inventory) {
		this.inventory = inventory;
	}
	
	public float getPackages() {
		return packages;
	}
	
	public void setPackages(float packages) {
		this.packages = packages;
	}
	
	public float getAdvertise() {
		return advertise;
	}
	
	public void setAdvertise(float advertise) {
		this.advertise = advertise;
	}
	
	public float getEquipment() {
		return equipment;
	}
	
	public void setEquipment(float equipment) {
		this.equipment = equipment;
	}
	
	public float getLost() {
		return lost;
	}
	
	public void setLost(float lost) {
		this.lost = lost;
	}
	
	public float getRefund() {
		return refund;
	}
	
	public void setRefund(float refund) {
		this.refund = refund;
	}
	
	public float getSubsidy() {
		return subsidy;
	}
	
	public void setSubsidy(float subsidy) {
		this.subsidy = subsidy;
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
