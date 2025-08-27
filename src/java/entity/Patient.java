/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author hongj
 */
import java.time.LocalDate;
import java.util.Date;

public class Patient extends Person implements Comparable<Patient>{
    private String gender;
    private String address;
    private LocalDate dateRegistered;

    public Patient() {
        super(); // call Person's constructor
        this.dateRegistered = LocalDate.now();
    }

    public Patient(String ICNum, String name, String gender, LocalDate dateOfBirth,
                   String phoneNum, String email, String address, LocalDate dateRegistered) {
        super(name, ICNum, toUtilDate(dateOfBirth), phoneNum, email);
        this.gender = gender;
        this.address = address;
        this.dateRegistered = dateRegistered != null ? dateRegistered : LocalDate.now();
    }

    private static Date toUtilDate(LocalDate localDate) {
        return localDate != null ? java.sql.Date.valueOf(localDate) : null;
    }
    
    public Patient(String icNum) {
        super(); // still calls the default Person constructor
        this.setIcNum(icNum); // set IC using the setter
    }

    private static LocalDate toLocalDate(Date date) {
        return date != null ? new java.sql.Date(date.getTime()).toLocalDate() : null;
    }

    public String getIcNum() {
        return super.getIc();
    }

    public void setIcNum(String ic) {
        super.setIc(ic);
    }

    public LocalDate getDateOfBirth() {
        return toLocalDate(super.getDateOB());
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        super.setDateOB(toUtilDate(dateOfBirth));
    }

    public String getPhoneNum() {
        return super.getPhoneNumber();
    }

    public void setPhoneNum(String phoneNum) {
        super.setPhoneNumber(phoneNum);
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDate dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
    
    public int compareTo(Patient other) {
        return this.getIcNum().compareToIgnoreCase(other.getIcNum());
    }

    @Override
    public String toString() {
        return "Patient{" +
                "IC='" + getIcNum() + '\'' +
                ", Name='" + getName() + '\'' +
                ", Gender='" + gender + '\'' +
                ", DOB=" + getDateOfBirth() +
                ", Phone='" + getPhoneNum() + '\'' +
                ", Email='" + getEmail() + '\'' +
                ", Address='" + address + '\'' +
                ", Registered=" + dateRegistered +
                '}';
    }
}