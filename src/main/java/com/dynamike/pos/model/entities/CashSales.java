package com.dynamike.pos.model.entities;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.TreeMap;

public class CashSales {


    @Valid
    private List<OrderList> orderItemList;
    
//    @Pattern(regexp = "^(?![+\\-@=]).*$", message = "Error data.")
    private String clientName;
    
//    @Pattern(regexp = "^(?![+\\-@=]).*$", message = "Error data.")
    private String cashSalesNo;
    
//    @Pattern(regexp = "^(?![+\\-@=]).*$", message = "Error data.")
    private String address;
    
//    @Pattern(regexp = "^(?![+\\-@=]).*$", message = "Error data.")
    private String contactNo;
    
    @Pattern(regexp = "^(?![+\\-@=]).*$", message = "Error data.")
    private String email;
    
//    @Pattern(regexp = "^(?![+\\-@=]).*$", message = "Error data.")
    private String date;
        
//    @Pattern(regexp = "^(?![+\\-@=]).*$", message = "Error data.")
    private String ringgit;
    
//    @Pattern(regexp = "^(?![+\\-@=]).*$", message = "Error data.")
    private String cents;
    
//    @Pattern(regexp = "^(?![+\\-@=]).*$", message = "Error data.")
    private String subTotal;
    
//    @Pattern(regexp = "^(?![+\\-@=]).*$", message = "Error data.")
    private String finalTotal;

	public List<OrderList> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderList> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCashSalesNo() {
		return cashSalesNo;
	}

	public void setCashSalesNo(String cashSalesNo) {
		this.cashSalesNo = cashSalesNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRinggit() {
		return ringgit;
	}

	public void setRinggit(String ringgit) {
		this.ringgit = ringgit;
	}

	public String getCents() {
		return cents;
	}

	public void setCents(String cents) {
		this.cents = cents;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(String finalTotal) {
		this.finalTotal = finalTotal;
	}
    
    
}
