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
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "items")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

//    @Column(name = "product_id")
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName="code")
    private Inventory product;

    @Column(name = "price_id")
    private String priceType;

    @Column(name = "selling_price")
    private String sellingPrice;
    
    @Column(name = "research_price")
    private String researchPrice;
    
    @Column(name = "price_50")
    private String price_50;

    @Column(name = "price_45")
    private String price_45;

    @Column(name = "price_40")
    private String price_40;

    @Column(name = "price_35")
    private String price_35;    

    @Column(name = "price_30")
    private String price_30;     

    @Column(name = "price_25")
    private String price_25; 
    
    @Column(name = "price_20")
    private String price_20; 
        
    @Column(name = "price_15")
    private String price_15; 
    
    @Column(name = "price_10")
    private String price_10; 
    
    @Transient
    private String price;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Inventory getProduct() {
        return product;
    }
    
    public String getProductName() {
        return product.getName();
    }

    public void setProduct(Inventory product) {
        this.product = product;
//        Inventory product
    }
    
    public String getPriceType() {
        return priceType;
    }

    public void setProductType(String priceType) {
        this.priceType = priceType;
    }
    
    
    private String getPrice(){
        switch(this.priceType){
            case"1":{
                price = getSellingPrice();
                break;
            }
            case"2":{
                price = getResearchPrice();
                break;
            }
            case"3":{
                price = getPrice_50();
                break;
            }
            case"4":{
                price = getPrice_45();
                break;
            }
            case"5":{
                price = getPrice_40();
                break;
            }
            case"6":{
                price = getPrice_35();
                break;
            }
            case"7":{
                price = getPrice_30();
                break;
            }
            case"8":{
                price = getPrice_25();
                break;
            }
            case"9":{
                price = getPrice_20();
                break;
            }
            case"10":{
                price = getPrice_15();
                break;
            }
            case"11":{
                price = getPrice_10();
                break;
            }
        
        }
        return price;
    }
    
    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    
    public String getResearchPrice() {
        return researchPrice;
    }

    public void setResearchPrice(String researchPrice) {
        this.researchPrice = researchPrice;
    }
    
    public String getPrice_50() {
        return price_50;
    }

    public void setPrice_50(String price_50) {
        this.price_50 = price_50;
    }

    public String getPrice_45() {
        return price_45;
    }

    public void setPrice_45(String price_45) {
        this.price_45 = price_45;
    }
    
    public String getPrice_40() {
        return price_40;
    }

    public void setPrice_40(String price_40) {
        this.price_40 = price_40;
    }
    
    public String getPrice_35() {
        return price_35;
    }

    public void setPrice_35(String price_35) {
        this.price_35 = price_35;
    }
    
    public String getPrice_30() {
        return price_30;
    }

    public void setPrice_30(String price_30) {
        this.price_30 = price_30;
    }
    
    public String getPrice_25() {
        return price_25;
    }

    public void setPrice_25(String price_25) {
        this.price_25 = price_25;
    }
    
    public String getPrice_20() {
        return price_20;
    }

    public void setPrice_20(String price_20) {
        this.price_20 = price_20;
    }
    
    public String getPrice_15() {
        return price_15;
    }

    public void setPrice_15(String price_15) {
        this.price_15 = price_15;
    }

    public String getPrice_10() {
        return price_10;
    }

    public void setPrice_10(String price_10) {
        this.price_10 = price_10;
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
        Item other = (Item) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }
}
