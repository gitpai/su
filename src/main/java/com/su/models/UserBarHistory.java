package com.su.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class UserBarHistory {

	@Column(name="user_name",unique=true)
	private String userName;
	@Column(name="borrow_time")
	private Date borrowTime; 
	@Column(name="reback_time")
	private Date rebackTime; 
}
