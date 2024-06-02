package com.expensemanagement.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="client")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Client {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column (name="clientId")
	private int clientId;
	@Column (name="clientName")
	private String clientName;
	@Column(name="clientSurname")
	private String clientSurname;
	@Column(name="clientEmail")
	private String clientEmail;
	@Column(name="clientPassword")
	private String clientPassword;
	
	
	public Client() {
		
	}
	
	public Client(String clientName, String clientSurname, String clientEmail, String clientPassword) {
		super();
		this.clientName=clientName;
		this.clientSurname=clientSurname;
		this.clientEmail=clientEmail;
		this.clientPassword=clientPassword;
	}
	
	public int getClientId() {
		return this.clientId;
	}
	
	public String getClientName() {
		return this.clientName;
	}
	
	public void setClientName(String name) {
		this.clientName=name;
	}
	
	public String getClientSurname() {
		return this.clientSurname;
	}
	
	public void setClientSurname(String surname) {
		this.clientSurname=surname;
	}
	
	public String getClientEmail() {
		return this.clientEmail;
	}
	
	public void setClientEmail(String email) {
		this.clientEmail=email;
	}
	
	public String getClientPassword() {
		return this.clientPassword;
	}
	
	public void setClientPassword(String password) {
		this.clientPassword=password;
	}
	
}
