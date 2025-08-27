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
import adt.AVLTree;
import adt.QueueInterface;
import adt.ListInterface;
import adt.ArrayList;
import adt.TreeInterface;
import entity.Patient;

import java.io.*;
import java.time.LocalDate;

public class MaintainPatient {
    private QueueInterface<Patient> walkInQueue;
    private TreeInterface<Patient> patientTree;
    private ListInterface<Patient> allPatients;

    public MaintainPatient() {
        walkInQueue = new Queue<>();
        patientTree = new AVLTree<>();
        allPatients = new ArrayList<>();
    }

    public Patient[] getQueueAsArray() {
        Patient[] arr = new Patient[walkInQueue.size()];
        for (int i = 0; i < arr.length; i++) {
            Patient p = walkInQueue.dequeue();
            arr[i] = p;
            walkInQueue.enqueue(p); 
        }
        return arr;
    }

    public void addPatient(Patient patient) {
        patientTree.insert(patient);
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
            walkInQueue.enqueue(p);
            if (p.getIcNum().equalsIgnoreCase(icNum)) {
                return true;
            }
        }
        return false;
    }

    public Patient serveNextPatient() {
        return walkInQueue.isEmpty() ? null : walkInQueue.dequeue();
    }

    public Patient serveSpecificPatient(int index) {
        if (index < 0 || index >= walkInQueue.size()) {
            return null;
        }
        return walkInQueue.remove(index);
    }

    public Patient searchPatientByIC(String icNum) {
        Patient temp = new Patient();
        temp.setIcNum(icNum);
        return patientTree.search(temp);
    }

    public boolean updatePatient(String icNum, String name, String gender, LocalDate dateOfBirth,
                                 String phoneNum, String email, String address) {
        Patient patient = searchPatientByIC(icNum);
        if (patient == null) return false;

        if (name != null && !name.isEmpty()) patient.setName(name);
        if (gender != null && !gender.isEmpty()) patient.setGender(gender);
        if (dateOfBirth != null) patient.setDateOfBirth(dateOfBirth);
        if (phoneNum != null) patient.setPhoneNum(phoneNum);
        if (email != null) patient.setEmail(email);
        if (address != null) patient.setAddress(address);

        return true;
    }

    public boolean removePatientByIC(String icNum) {
        Patient target = searchPatientByIC(icNum);
        boolean removed = false;

        if (target != null) {
            patientTree.delete(target);
            removed = true;
        }

        for (int i = 0; i < allPatients.size(); i++) {
            if (allPatients.get(i).getIcNum().equalsIgnoreCase(icNum)) {
                allPatients.remove(i);
                break;
            }
        }

        return removeFromQueueByIC(icNum) || removed;
    }

    public boolean removeFromQueueByIC(String icNum) {
        int size = walkInQueue.size();
        boolean removed = false;
        for (int i = 0; i < size; i++) {
            Patient p = walkInQueue.dequeue();
            if (!p.getIcNum().equalsIgnoreCase(icNum)) {
                walkInQueue.enqueue(p);
            } else {
                removed = true;
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

    public Patient[] getAllPatientsArray() {
        return allPatients.toArray(new Patient[0]);
    }

    public int getQueueSize() {
        return walkInQueue.size();
    }

    public void clearQueue() {
        walkInQueue.clear();
    }

    public void clearPatientsOnly() {
        walkInQueue.clear();
    }

    public void savePatientsToFile(String filePath) throws IOException {
        Patient[] patients = getAllPatientsArray();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Patient p : patients) {
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
        allPatients.clear();
        patientTree.clear();

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
}