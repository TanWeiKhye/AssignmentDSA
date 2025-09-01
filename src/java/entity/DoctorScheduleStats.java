package entity;

public class DoctorScheduleStats {
    private String doctorName;
    private int totalSlots;
    private int available;
    private int unavailable;
    private double availabilityPercentage;

    public DoctorScheduleStats(String doctorName, int totalSlots, int available, int unavailable, double availabilityPercentage) {
        this.doctorName = doctorName;
        this.totalSlots = totalSlots;
        this.available = available;
        this.unavailable = unavailable;
        this.availabilityPercentage = availabilityPercentage;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public int getAvailable() {
        return available;
    }

    public int getUnavailable() {
        return unavailable;
    }

    public double getAvailabilityPercentage() {
        return availabilityPercentage;
    }
}
