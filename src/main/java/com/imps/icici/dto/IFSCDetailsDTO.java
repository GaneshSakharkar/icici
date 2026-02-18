package com.imps.icici.dto;


public class IFSCDetailsDTO {

	private String ifsc;
	private String branchName;
	private String pincode;
	public IFSCDetailsDTO(String ifsc, String branchName, String pincode) {
		super();
		this.ifsc = ifsc;
		this.branchName = branchName;
		this.pincode = pincode;
	}
	
}
