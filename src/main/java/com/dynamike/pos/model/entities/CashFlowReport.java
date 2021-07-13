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
public class CashFlowReport implements Serializable {

//	17	銀行
//	18	实店底钱
//	19	Shopee Wallet
//	20	Lazada Balance
//	21	荷包仔
    private float cash;

    private float bankAccount;
    
    private float shopeeWallet;
  
    private float lazadaWallet;
    
    private float insurance;

    private float wallet;
    
    private float total;

    public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	
	public float getCash() {
		return cash;
	}

	public void setCash(float cash) {
		this.cash = cash;
	}

	public float getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(float bankAccount) {
		this.bankAccount = bankAccount;
	}

	public float getShopeeWallet() {
		return shopeeWallet;
	}

	public void setShopeeWallet(float shopeeWallet) {
		this.shopeeWallet = shopeeWallet;
	}

	public float getLazadaWallet() {
		return lazadaWallet;
	}

	public void setLazadaWallet(float lazadaWallet) {
		this.lazadaWallet = lazadaWallet;
	}

	public float getInsurance() {
		return insurance;
	}

	public void setInsurance(float insurance) {
		this.insurance = insurance;
	}

	public float getWallet() {
		return wallet;
	}

	public void setWallet(float wallet) {
		this.wallet = wallet;
	}
    
}
