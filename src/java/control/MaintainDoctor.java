/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

/**
 *
 * @author dinjun
 */
import adt.AVLTree;
import adt.ArrayList;
import adt.ListInterface;
import entity.Doctor;
import entity.Schedule;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MaintainDoctor {
    private AVLTree<Doctor> doctors;
    private String filePath;

    public MaintainDoctor(String filePath) {
        this.doctors = new AVLTree<>();
        this.filePath = filePath;
        loadDoctorData();
    }

    public void loadDoctorData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.equals("---")) {
                    continue;
                }

                String[] data = line.split("\\|");
                if (data.length >= 6) {
                    String name = data[0];
                    String ic = data[1];
                    Date dateOB = new SimpleDateFormat("yyyy-MM-dd").parse(data[2]);
                    String phoneNumber = data[3];
                    String email = data[4];
                    String gender = data[5];

                    ListInterface<String> edu = new ArrayList<>();
                    ListInterface<Schedule> schedules = new ArrayList<>();

                    // Read education data
                    while ((line = reader.readLine()) != null && !line.equals("---")) {
                        if (line.trim().isEmpty()) {
                            continue;
                        }
                        String[] eduData = line.split("\\|");
                        for (String eduItem : eduData) {
                            edu.add(eduItem.trim());
                        }
                    }

                    // Read schedules data
                    while ((line = reader.readLine()) != null && !line.equals("---")) {
                        String[] scheduleData = line.split("\\|");
                        if (scheduleData.length == 4) {
                            Date scheduleDate = new SimpleDateFormat("yyyy-MM-dd").parse(scheduleData[0]);
                            String status = scheduleData[1];
                            String location = scheduleData[2];
                            String scheduleDoctorIc = scheduleData[3];
                            schedules.add(new Schedule(scheduleDoctorIc, scheduleDate, status, location));
                        }
                    }

                    Doctor doctor = new Doctor(name, ic, dateOB, phoneNumber, email, gender, edu, schedules);
                    doctors.insert(doctor);
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error loading doctor data: " + e.getMessage());
        }
    }

    public void saveDoctorData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Doctor doctor : doctors) {
                // Write basic doctor info
                writer.write(doctor.getName() + "|" + 
                            doctor.getIc() + "|" + 
                            new SimpleDateFormat("yyyy-MM-dd").format(doctor.getDateOB()) + "|" + 
                            doctor.getPhoneNumber() + "|" + 
                            doctor.getEmail() + "|" + 
                            doctor.getGender());
                writer.newLine();
                
                // Write education
                for (int i = 1; i <= doctor.getEdu().size(); i++) {
                    String education = doctor.getEdu().get(i);
                    writer.write(education + "|");
                }
                writer.newLine();
                
                // Write schedules
                for (int i = 1; i <= doctor.getSchedules().size(); i++) {
                    Schedule schedule = doctor.getSchedules().get(i);
                    writer.write(new SimpleDateFormat("yyyy-MM-dd").format(schedule.getDate()) + "|" +
                                 schedule.getStatus() + "|" +
                                 schedule.getLocation() + "|" +
                                 schedule.getDoctorIc());
                    writer.newLine();
                }
                
                // Separator between doctors
                writer.write("---");
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving doctor data: " + e.getMessage());
        }
    }

    public boolean addDoctor(Doctor doctor) {
        if (doctors.search(doctor) != null) {
            return false; // Doctor already exists
        }
        doctors.insert(doctor);
        saveDoctorData();
        return true;
    }

    public Doctor getDoctor(String ic) {
        return doctors.search(new Doctor(ic));
    }

    public boolean updateDoctor(String ic, Doctor updatedDoctor) {
        Doctor existingDoctor = doctors.search(new Doctor(ic));
        if (existingDoctor == null) {
            return false; // Doctor not found
        }
        
        // Remove the old doctor and add the updated one
        doctors.delete(existingDoctor);
        doctors.insert(updatedDoctor);
        saveDoctorData();
        return true;
    }

    public boolean deleteDoctor(String ic) {
        Doctor doctor = doctors.search(new Doctor(ic));
        if (doctor == null) {
            return false; // Doctor not found
        }
        
        doctors.delete(doctor);
        saveDoctorData();
        return true;
    }

    public AVLTree<Doctor> getAllDoctors() {
        return doctors;
    }

    public boolean addEducation(String ic, String education) {
        Doctor doctor = doctors.search(new Doctor(ic));
        if (doctor == null) {
            return false; // Doctor not found
        }
        
        doctor.addEdu(education);
        saveDoctorData();
        return true;
    }

    public boolean addSchedule(String ic, Schedule schedule) {
        Doctor doctor = doctors.search(new Doctor(ic));
        if (doctor == null) {
            return false; // Doctor not found
        }
        
        doctor.addSchedules(schedule);
        saveDoctorData();
        return true;
    }

    public ListInterface<Schedule> getSchedules(String ic) {
        Doctor doctor = doctors.search(new Doctor(ic));
        if (doctor == null) {
            return new ArrayList<>(); // Return empty list if doctor not found
        }
        
        return doctor.getSchedules();
    }

    public ListInterface<Schedule> getUpcomingSchedules(String ic, int days) {
        Doctor doctor = doctors.search(new Doctor(ic));
        if (doctor == null) {
            return new ArrayList<>(); // Return empty list if doctor not found
        }
        
        ListInterface<Schedule> upcomingSchedules = new ArrayList<>();
        Date currentDate = new Date();
        
        for (int i = 1; i <= doctor.getSchedules().size(); i++) {
            Schedule schedule = doctor.getSchedules().get(i);
            long diff = schedule.getDate().getTime() - currentDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            
            if (diffDays >= 0 && diffDays <= days) {
                upcomingSchedules.add(schedule);
            }
        }
        
        return upcomingSchedules;
    }
}
