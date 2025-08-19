package entity;

import java.util.Date;

public class Doctor extends Person {

	public Doctor() {

	}

	public Doctor(String name, String ic, Date dateOB, String phoneNumber, String email) {
		super(name, ic, dateOB, phoneNumber, email);
	}
}
