package com.tcs.casestudy1.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Vendor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vendorId;
	private String vendorName;
	private Long vendorContactNumber;
	private String vendorEmail;
	private String vendorUserName;
	private String vendorAddress;

	public Vendor() {
		super();
	}

	public Vendor(Long vendorId, String vendorName, Long vendorContactNumber, String vendorEmail, String vendorUserName,
			String vendorAddress) {
		super();
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.vendorContactNumber = vendorContactNumber;
		this.vendorEmail = vendorEmail;
		this.vendorUserName = vendorUserName;
		this.vendorAddress = vendorAddress;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Long getVendorContactNumber() {
		return vendorContactNumber;
	}

	public void setVendorContactNumber(Long vendorContactNumber) {
		this.vendorContactNumber = vendorContactNumber;
	}

	public String getVendorEmail() {
		return vendorEmail;
	}

	public void setVendorEmail(String vendorEmail) {
		this.vendorEmail = vendorEmail;
	}

	public String getVendorUserName() {
		return vendorUserName;
	}

	public void setVendorUserName(String vendorUserName) {
		this.vendorUserName = vendorUserName;
	}

	public String getVendorAddress() {
		return vendorAddress;
	}

	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

}