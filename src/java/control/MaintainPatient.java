/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

/**
 *
 * @author hongj
 */

import adt.Queue;
import adt.ArrayList;
import adt.QueueInterface;
import adt.ListInterface;
import entity.Patient;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class MaintainPatient {
    private QueueInterface<Patient> walkInQueue;
    private ListInterface<Patient> allPatients;

    public MaintainPatient() {
        walkInQueue = new Queue<>();
        allPatients = new ArrayList<>();

    }
    
    public Patient[] getQueueAsArray() {
        Patient[] arr = new Patient[walkInQueue.size()];
        for (int i = 0; i < arr.length; i++) {
            Patient p = walkInQueue.dequeue();
            arr[i] = p;
            walkInQueue.enqueue(p); // maintain order
        }
        return arr;
    }

    public void addPatient(Patient patient) {
        allPatients.add(patient);
    }
    
    public void addToQueue(Patient p) {
        if (!isInQueue(p.getIcNum())) {
            walkInQueue.enqueue(p);
        }
    }

    public boolean isInQueue(String icNum) {
        int size = walkInQueue.size();
        for (int i = 0; i < size; i++) {
            Patient p = walkInQueue.dequeue();
            walkInQueue.enqueue(p); // maintain order
            if (p.getIcNum().equalsIgnoreCase(icNum)) {
                return true;
            }
        }
        return false;
    }

    public Patient serveNextPatient() {
        return walkInQueue.isEmpty() ? null : walkInQueue.dequeue();
    }

    public Patient searchPatientByIC(String icNum) {
        for (Patient p : allPatients.toArray(new Patient[0])) {
            if (p.getIcNum().equalsIgnoreCase(icNum)) {
                return p;
            }
        }
        return null;
    }

    public boolean updatePatient(String icNum, String name, String gender, LocalDate dateOfBirth,
                                 String phoneNum, String email, String address) {
        Patient[] patients = getAllPatientsArray();
        boolean updated = false;
        walkInQueue.clear();
        for (int i = 0; i < patients.length; i++) {
            Patient p = patients[i];
            if (p.getIcNum().equalsIgnoreCase(icNum)) {
                if (name != null && !name.isEmpty()) p.setName(name);
                if (gender != null && !gender.isEmpty()) p.setGender(gender);
                if (dateOfBirth != null) p.setDateOfBirth(dateOfBirth);
                if (phoneNum != null) p.setPhoneNum(phoneNum);
                if (email != null) p.setEmail(email);
                if (address != null) p.setAddress(address);
                updated = true;
            }
            walkInQueue.enqueue(p);
        }
        return updated;
    }

    public boolean removePatientByIC(String icNum) {
        boolean removed = false;

        // Remove from allPatients
        for (int i = 0; i < allPatients.size(); i++) {
            if (allPatients.get(i).getIcNum().equalsIgnoreCase(icNum)) {
                allPatients.remove(i);
                removed = true;
                break;
            }
        }

        // Remove from walkInQueue
        int queueSize = walkInQueue.size();
        for (int i = 0; i < queueSize; i++) {
            Patient p = walkInQueue.dequeue();
            if (!p.getIcNum().equalsIgnoreCase(icNum)) {
                walkInQueue.enqueue(p);
            }
        }

        return removed;
    }
    
    public Patient removeFromQueueByIndex(int index) {
        if (index >= 0 && index < walkInQueue.size()) {
            return walkInQueue.remove(index); 
        }
        return null;
    }
    
    public boolean removeFromQueueByIC(String ic) {
        for (int i = 0; i < walkInQueue.size(); i++) {
            if (allPatients.get(i).getIcNum().equalsIgnoreCase(ic)) {
                walkInQueue.remove(i);
                return true;
            }
        }
        return false;
    }

    
    public Patient[] getAllRegisteredPatients() {
        return allPatients.toArray(new Patient[0]);
    }

    public Patient[] getAllPatientsArray() {
        int size = walkInQueue.size();
        Patient[] arr = new Patient[size];
        for (int i = 0; i < size; i++) {
            Patient p = walkInQueue.dequeue();
            arr[i] = p;
            walkInQueue.enqueue(p);
        }
        return arr;
    }
    
    public Patient serveSpecificPatient(int index) {
        if (index < 0 || index >= walkInQueue.size()) {
            return null; 
        }
        return walkInQueue.remove(index); 
    }

    public int getQueueSize() {
        return walkInQueue.size();
    }
    
    public void clearPatientsOnly() {
        walkInQueue.clear();
    }

    public void savePatientsToFile(String filePath) throws IOException {
        Patient[] patients = getAllPatientsArray();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < patients.length; i++) {
                Patient p = patients[i];
                writer.write(String.join(",",
                        p.getIcNum(),
                        p.getName(),
                        p.getGender(),
                        p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : "",
                        p.getPhoneNum(),
                        p.getEmail(),
                        p.getAddress(),
                        p.getDateRegistered() != null ? p.getDateRegistered().toString() : ""
                ));
                writer.newLine();
            }
        }
    }

    public void loadPatientsFromFile(String filePath) throws IOException {
        walkInQueue.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 8) {
                    Patient p = new Patient(
                            data[0],
                            data[1], 
                            data[2], 
                            data[3].isEmpty() ? null : LocalDate.parse(data[3]), 
                            data[4], 
                            data[5], 
                            data[6], 
                            data[7].isEmpty() ? null : LocalDate.parse(data[7])
                    );
                    addPatient(p);
                }
            }
        }
    }

    public void clearQueue() {
        walkInQueue.clear();
    }
}