package com.exam.forms;

import org.hibernate.validator.constraints.NotEmpty;


import com.esotericsoftware.kryo.Registration;
import com.exam.forms.checks.LoginFormCheck;
import com.exam.forms.checks.RegisterFormCheck;

public class HibernateValidation {
	@NotEmpty(message = "Email is empty", groups = { LoginFormCheck.class,RegisterFormCheck.class })
	@javax.validation.constraints.Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid Email type", groups = {
			LoginFormCheck.class,RegisterFormCheck.class })
	private String userLoginId;
	
	@NotEmpty(message = "password is empty", groups = { LoginFormCheck.class,RegisterFormCheck.class })
	@javax.validation.constraints.Pattern(regexp = "^.{8,}", message = "Invalid Password type", groups = { RegisterFormCheck.class })
	private String password;
	
	@NotEmpty(message = "firstname is empty", groups = { RegisterFormCheck.class })
	@javax.validation.constraints.Pattern(regexp = "^[a-zA-Z][a-zA-Z-'\\\\.\\\\s]*$", message = "Invalid firstname type", groups = {
			RegisterFormCheck.class })
	private String firstName;
	
	@NotEmpty(message = "lastname is empty", groups = { RegisterFormCheck.class })
	@javax.validation.constraints.Pattern(regexp = "^[a-zA-Z][a-zA-Z-'\\\\.\\\\s]*$", message = "Invalid lastname type", groups = {
			RegisterFormCheck.class })
	private String lastName;
	
	public String getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	

}
