package com.expensemanagement.transaction;

import com.expensemanagement.category.Category;
import com.expensemanagement.client.Client;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "transaction")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "transactionId")
	private int transactionId;

	@Column(name = "transactionDate")
	private String transactionDate;

	@Column(name = "transactionAmount")
	private double transactionAmount;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "categoryId", referencedColumnName = "categoryId", nullable = false)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "clientId", referencedColumnName = "clientId", nullable = false)
	private Client client;

	public Transaction() {

	}

	public Transaction(String transactionDate, double transactionAmount, Category category, Client client) {
		super();
		this.transactionDate = transactionDate;
		this.transactionAmount = transactionAmount;
		this.category = category;
		this.client = client;

	}

	public void setTransactionClient(Client client) {
		this.client = client;
	}

	public Client getTransactionClient() {
		return this.client;
	}

	public int getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionCategoryId(Category kategorija, int id) {
		this.category.setCategoryId(id);
	}

	public String getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(String date) {
		this.transactionDate = date;
	}

	public double getTransactionAmount() {
		return this.transactionAmount;
	}

	public void setTransactionAmount(double amount) {
		this.transactionAmount = amount;
	}

	public Category getTransactionCategory() {
		return this.category;
	}

	public void setTransactionCategory(Category category) {
		this.category = category;
	}

}
