package com.smartcity.hyd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class UserModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int uid;
	@Column
	private String uname;
	@Column
	private String upassword;
	@Column
	private String umail;
	@Column
	private long mobile;
	public UserModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserModel(int uid, String uname, String upassword, String umail, long mobile) {
		super();
		this.uid = uid;
		this.uname = uname;
		this.upassword = upassword;
		this.umail = umail;
		this.mobile = mobile;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUpassword() {
		return upassword;
	}
	public void setUpassword(String upassword) {
		this.upassword = upassword;
	}
	public String getUmail() {
		return umail;
	}
	public void setUmail(String umail) {
		this.umail = umail;
	}
	public long getMobile() {
		return mobile;
	}
	public void setMobile(long mobile) {
		this.mobile = mobile;
	}
	@Override
	public String toString() {
		return "UserModel [uid=" + uid + ", uname=" + uname + ", upassword=" + upassword + ", umail=" + umail
				+ ", mobile=" + mobile + "]";
	}
	
}
