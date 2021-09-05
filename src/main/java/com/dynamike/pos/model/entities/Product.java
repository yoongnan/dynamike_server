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

@Entity
@Table(name = "inventory")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "code")
    private String code;
    
    @Column(name = "name")
    private String name;

    @Column(name = "stock")
    private String stock;

    @Column(name = "unit_cost")
    private String unit_cost;
    
    @Column(name = "total_stock")
    private String total_stock;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "code", insertable=false, updatable=false,referencedColumnName="id")
    private SimpleItem item;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }


    public String getUnit_cost() {
		return unit_cost;
	}

	public void setUnit_cost(String unit_cost) {
		this.unit_cost = unit_cost;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public SimpleItem getItem() {
        return item;
    }
    
    public void setItem(SimpleItem item) {
        this.item = item;
    }
    public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getTotal_stock() {
		return total_stock;
	}

	public void setTotal_stock(String total_stock) {
		this.total_stock = total_stock;
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
        Product other = (Product) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }
}
