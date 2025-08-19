package entity;

import java.util.Date;

public class Person {
	private String name;
	private String ic;
	private Date dateOB;
	private String phoneNumber;
	private String email;
	
	public Person() {
		
	}

	public Person(String name, String ic, Date dateOB, String phoneNumber, String email) {
		this.name = name;
		this.ic = ic;
		this.dateOB = dateOB;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIc(String ic) {
		this.ic = ic;
	}

	public void setDateOB(Date dateOB) {
		this.dateOB = dateOB;
	}

	public String getName() {
		return name;
	}

	public String getIc() {
		return ic;
	}

	public Date getDateOB() {
		return dateOB;
	}

	@Override
	public String toString() {
		return "Person{" + "name=" + name + ", ic=" + ic + ", dateOB=" + dateOB + '}';
	}
}
