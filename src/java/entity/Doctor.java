package entity;

import adt.ArrayList;
import adt.ListInterface;
import java.util.Date;

public class Doctor extends Person implements Comparable<Doctor> {

	private ListInterface<String> edu;
	private ListInterface<Schedule> schedules;

	public Doctor(String name, String ic, Date dateOB, String phoneNumber, String email, String gender, ListInterface<String> edu, ListInterface<Schedule> schedules) {
		super(name, ic, dateOB, phoneNumber, email, gender);
		edu = new ArrayList<>();
		schedules = new ArrayList<>();
	}

	public void addEdu(String edu) {
		this.edu.add(edu);
	}

	public void addSchedules(Schedule schedule) {
		this.schedules.add(schedule);
	}

	public ListInterface<String> getEdu() {
		return edu;
	}

	public ListInterface<Schedule> getSchedules() {
		return schedules;
	}
	
	public String lastNumOfIc(String ic) {
		String[] id = ic.split("-");
		return id[2];
	}

	@Override
	public int compareTo(Doctor o) {
		int firstId = Integer.parseInt(lastNumOfIc(this.getIc()));
		int secondId = Integer.parseInt(lastNumOfIc(o.getIc()));
		
		return Integer.compare(firstId, secondId);
	}
}
