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

public class Patient {
    private String ICNum;
    private String name;
    private String gender;
    private LocalDate dateOfBirth;
    private String phoneNum;
    private String email;
    private String address;
    private LocalDate dateRegistered;

    public Patient() {
        this.dateRegistered = LocalDate.now();
    }

    public Patient(String ICNum, String name, String gender, LocalDate dateOfBirth,
                   String phoneNum, String email, String address, LocalDate dateRegistered) {
        this.ICNum = ICNum;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNum = phoneNum;
        this.email = email;
        this.address = address;
        this.dateRegistered = dateRegistered != null ? dateRegistered : LocalDate.now();
    }

    public String getIcNum() { return ICNum; }
    public void setIcNum(String ICNum) { this.ICNum = ICNum; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPhoneNum() { return phoneNum; }
    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getDateRegistered() { return dateRegistered; }
    public void setDateRegistered(LocalDate dateRegistered) { this.dateRegistered = dateRegistered; }

    @Override
    public String toString() {
        return "Patient{" +
                "IC='" + ICNum + '\'' +
                ", Name='" + name + '\'' +
                ", Gender='" + gender + '\'' +
                ", DOB=" + dateOfBirth +
                ", Phone='" + phoneNum + '\'' +
                ", Email='" + email + '\'' +
                ", Address='" + address + '\'' +
                ", Registered=" + dateRegistered +
                '}';
    }
}

