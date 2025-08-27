package entity;

import java.util.Date;

public class Schedule {
    private Date date;
    private String day;
    private String shift;
    private String location;
    private String status;

    public Schedule(Date date, String day, String shift, String location, String status) {
        this.date = date;
        this.day = day;
        this.shift = shift;
        this.location = location;
        this.status = status;
    }

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public String getDay() {
		return day;
	}

	public String getShift() {
		return shift;
	}

	public String getLocation() {
		return location;
	}

	public String getStatus() {
		return status;
	}

}
