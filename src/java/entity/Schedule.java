package entity;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Schedule {
    private String doctorIc;
    private Date date;
    private String status;
    private String location;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Schedule(String doctorIc, Date date, String status, String location) {
        this.doctorIc = doctorIc;
        this.date = date;
        this.status = status;
        this.location = location;
    }

    // Getters
    public String getDoctorIc() {
        return doctorIc;
    }

    public Date getDate() {
        return date;
    }

    public String getFormattedDate() {
        return dateFormat.format(date);
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    // Setters
    public void setDoctorIc(String doctorIc) {
        this.doctorIc = doctorIc;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return "Schedule{" +
                "doctorIc='" + doctorIc + '\'' +
                ", date=" + dateFormat.format(date) +
                ", status='" + status + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    // equals method for comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Schedule schedule = (Schedule) o;
        
        if (!doctorIc.equals(schedule.doctorIc)) return false;
        if (!date.equals(schedule.date)) return false;
        if (!status.equals(schedule.status)) return false;
        return location.equals(schedule.location);
    }

    // hashCode method for use in collections
    @Override
    public int hashCode() {
        int result = doctorIc.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + location.hashCode();
        return result;
    }

    // Additional utility methods
    
    public boolean isAvailable() {
        return "Available".equalsIgnoreCase(status);
    }
    
    public boolean isBooked() {
        return "Booked".equalsIgnoreCase(status);
    }
    
    public String toDisplayString() {
        return String.format("Date: %s, Location: %s, Status: %s", 
                           dateFormat.format(date), location, status);
    }
}