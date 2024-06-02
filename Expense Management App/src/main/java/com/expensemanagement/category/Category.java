package com.expensemanagement.category;

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
@Table(name = "category")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "categoryId")
	private int categoryId;

	@Column(name = "categoryName", unique = true)
	private String categoryName;

	@Column(name = "categoryType")
	private String categoryType;

	public Category() {

	}

	public Category(String categoryName, String categoryType) {
		super();
		this.categoryName = categoryName;
		this.categoryType = categoryType;
	}

	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(int id) {
		this.categoryId = id;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String name) {
		this.categoryName = name;
	}

	public String getCategoryType() {
		return this.categoryType;
	}

	public void setCategoryType(String type) {
		this.categoryType = type;
	}

	@Override
	public String toString() {
		return String.format(this.categoryName);
	}

}
