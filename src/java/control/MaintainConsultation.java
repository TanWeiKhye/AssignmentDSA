package control;

import adt.ArrayList;
import adt.AVLTree;
import adt.ListInterface;
import adt.TreeInterface;

import entity.Consultation;
import entity.Doctor;
import entity.Patient;

import java.io.*;
import java.time.LocalDateTime;

public class MaintainConsultation {
    private TreeInterface<Consultation> consultationTree;
    private ListInterface<Consultation> consultationList;
    private static int defaultCounter = 1000;
    
    public MaintainConsultation() {
        consultationTree = new AVLTree<>();
        consultationList = new ArrayList<>();
    }
   
    public String generateConsultationId() {
    // Use the current consultation list size as the base
    int nextId = defaultCounter + (consultationList != null ? consultationList.size() : 0) + 1;
    
    // Make sure the new ID does not collide with existing IDs
    String newId;
    do {
        newId = String.format("CONST%04d", nextId);
        nextId++;
    } while (consultationTree != null && consultationTree.search(Consultation.createSearchKey(newId)) != null);
    
    return newId;
}

    
    public void loadConsultationsFromFile(String filePath, MaintainPatient patientManager) throws IOException {  
    clearAllConsultations();
    
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length >= 6) {
                // Parse consultation data
                String consultationId = data[0].trim();
                String patientIc = data[1].trim();
                String doctorIc = data[2].trim();
                LocalDateTime consultationDateTime = parseDateTime(data[3].trim());
                LocalDateTime nextAppointment = data[4].isEmpty() ? null : parseDateTime(data[4].trim());
                String status = data[5].trim();
                
                // Handle unknown patients/doctors gracefully
                Patient patient = null;
                if (!"UNKNOWN_PATIENT".equals(patientIc)) {
                    patient = patientManager.searchPatientByIC(patientIc);
                    if (patient == null) {
                        System.err.println("Warning: Patient not found with IC: " + patientIc);
                    }
                }
                
                Doctor doctor = new Doctor();
                if (!"UNKNOWN_DOCTOR".equals(doctorIc)) {
                    doctor.setIc(doctorIc);
                } else {
                    doctor.setIc("UNKNOWN_DOCTOR");
                }
                
                // Create consultation
                Consultation consultation = new Consultation(consultationId, patient, doctor,
                        consultationDateTime, nextAppointment);
                consultation.setStatus(status);
                
                consultationTree.insert(consultation);
                consultationList.add(consultation);
            }
        }
    }
}

private LocalDateTime parseDateTime(String dateTimeStr) {
    if (dateTimeStr == null || dateTimeStr.isEmpty() || dateTimeStr.equals("null")) {
        return null;
    }
    try {
        return LocalDateTime.parse(dateTimeStr);
    } catch (Exception e) {
        System.err.println("Error parsing datetime: " + dateTimeStr);
        return null;
    }
}

// Helper method to find doctor by IC (you'll need access to doctor data)
private Doctor findDoctorByIc(String doctorIc) {
    // This depends on how you store doctors
    // You might need to get this from your groupmate's doctor module
    // For now, create a temporary doctor object
    Doctor doctor = new Doctor();
    doctor.setIc(doctorIc);
    return doctor;
}

public void saveConsultationsToFile(String filePath) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation c = consultationList.get(i);
            String patientIc = (c.getPatient() != null && c.getPatient().getIc() != null)
                    ? c.getPatient().getIc() : "UNKNOWN_PATIENT";
            String doctorIc = (c.getDoctor() != null && c.getDoctor().getIc() != null)
                    ? c.getDoctor().getIc() : "UNKNOWN_DOCTOR";

            writer.write(String.join(",",
                c.getConsultationId(),
                patientIc,
                doctorIc,
                c.getConsultationDateTime() != null ? c.getConsultationDateTime().toString() : "",
                c.getNextAppointment() != null ? c.getNextAppointment().toString() : "",
                c.getStatus() != null ? c.getStatus() : "Scheduled"
            ));
            writer.newLine();
        }
    }
}



    
    public boolean createConsultation(Patient patient, Doctor doctor, 
                                    LocalDateTime consultationDateTime, 
                                    LocalDateTime nextAppointment) {
        String consultationId = generateConsultationId();        
        Consultation consultation = new Consultation(consultationId, patient, doctor, 
                consultationDateTime, nextAppointment);
        consultation.setStatus("Scheduled");
        
        consultationTree.insert(consultation);
        consultationList.add(consultation);
        return true;
    }
    
    public boolean startConsultation(String consultationId, String filePath) {
    Consultation searchKey = Consultation.createSearchKey(consultationId);
    Consultation consultation = consultationTree.search(searchKey);

    if (consultation != null && "Scheduled".equals(consultation.getStatus())) {
        consultation.setStatus("InProgress");
        try {
            saveConsultationsToFile(filePath);
        } catch (IOException e) {
            System.err.println("Failed to save after starting consultation: " + e.getMessage());
        }
        return true;
    }
    return false;
}
    
    public boolean completeConsultation(String consultationId, String filePath) {
    Consultation searchKey = Consultation.createSearchKey(consultationId);
    Consultation consultation = consultationTree.search(searchKey);

    if (consultation != null && "InProgress".equals(consultation.getStatus())) {
        consultation.setStatus("Completed");
        try {
            saveConsultationsToFile(filePath);
        } catch (IOException e) {
            System.err.println("Failed to save after completing consultation: " + e.getMessage());
        }
        return true;
    }
    return false;
}
    
    public boolean cancelConsultation(String consultationId, String filePath) {
    Consultation searchKey = Consultation.createSearchKey(consultationId);
    Consultation consultation = consultationTree.search(searchKey);

    if (consultation != null && !"Completed".equals(consultation.getStatus())) {
        consultation.setStatus("Cancelled");
        try {
            saveConsultationsToFile(filePath);
        } catch (IOException e) {
            System.err.println("Failed to save after cancelling consultation: " + e.getMessage());
        }
        return true;
    }
    return false;
}
   
    public boolean scheduleFollowUp(String consultationId, LocalDateTime nextAppointment) {
    Consultation searchKey = Consultation.createSearchKey(consultationId);
    Consultation consultation = consultationTree.search(searchKey);
    
    if (consultation != null && "Completed".equals(consultation.getStatus())) {
        // Update the original consultation with the next appointment
        consultation.setNextAppointment(nextAppointment);

        // Create a new consultation for the follow-up
        Consultation followUp = new Consultation(
            generateConsultationId(),
            consultation.getPatient(),
            consultation.getDoctor(),
            nextAppointment,
            null
        );
        followUp.setStatus("Scheduled");

        consultationTree.insert(followUp);
        consultationList.add(followUp);

        return true;
    }
    return false;
}



    
        // helper to avoid blocking rescheduling of same consultation
public boolean isDoctorAvailable(Doctor doctor, LocalDateTime dateTime, String skipConsultationId) {
    for (int i = 0; i < consultationList.size(); i++) {
        Consultation cons = consultationList.get(i);
        if (cons == null) continue;
        if (skipConsultationId != null && skipConsultationId.equals(cons.getConsultationId())) continue;

        if (cons.getDoctor() != null && doctor != null &&
            cons.getDoctor().getIc() != null && doctor.getIc() != null &&
            cons.getDoctor().getIc().equals(doctor.getIc()) &&
            cons.getConsultationDateTime() != null &&
            cons.getConsultationDateTime().equals(dateTime) &&
            !"Cancelled".equals(cons.getStatus())) {
            return false;
        }
    }
    return true;
}

public boolean rescheduleConsultation(String consultationId, LocalDateTime newDateTime) {
    Consultation searchKey = Consultation.createSearchKey(consultationId);
    Consultation c = consultationTree.search(searchKey);

    if (c != null && !"Completed".equals(c.getStatus())) {
        // Check doctor availability
        if (isDoctorAvailable(c.getDoctor(), newDateTime, consultationId)) {
            // Update main consultation date/time
            c.setConsultationDateTime(newDateTime);

            // If this consultation has a next appointment, update it too
            if (c.getNextAppointment() != null) {
                c.setNextAppointment(newDateTime.plusDays(1)); // example logic, or keep original interval
            }

            // Check if this is a follow-up of another consultation
            for (int i = 0; i < consultationList.size(); i++) {
                Consultation parent = consultationList.get(i);
                if (parent != null && newDateTime.equals(parent.getNextAppointment())) {
                    // Update parent's nextAppointment to match rescheduled follow-up
                    parent.setNextAppointment(newDateTime);
                }
            }

            return true;
        }
    }
    return false;
}

 
    public Consultation findConsultationById(String consultationId) {
        Consultation searchKey = Consultation.createSearchKey(consultationId);
        return consultationTree.search(searchKey);
    }
    
    public ListInterface<Consultation> getConsultationsByPatientIc(String patientIc) {
        ListInterface<Consultation> patientConsultations = new ArrayList<>();
        
        // Iterate through the list instead of using tree iterator
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation cons = consultationList.get(i);
            if (cons.getPatient() != null && cons.getPatient().getIc().equals(patientIc)) {
                patientConsultations.add(cons);
            }
        }
        return patientConsultations;
    }
    
    public ListInterface<Consultation> getConsultationsByDoctorIc(String doctorIc) {
        ListInterface<Consultation> doctorConsultations = new ArrayList<>();
        
        // Iterate through the list instead of using tree iterator
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation cons = consultationList.get(i);
            if (cons.getDoctor() != null && cons.getDoctor().getIc().equals(doctorIc)) {
                doctorConsultations.add(cons);
            }
        }
        return doctorConsultations;
    }
    
    public ListInterface<Consultation> getScheduledConsultations() {
        ListInterface<Consultation> scheduled = new ArrayList<>();
        
        if (consultationList == null) {
        System.err.println("ERROR: consultationList is null!");
        return scheduled;
    }
        
        // Iterate through the list instead of using tree iterator
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation cons = consultationList.get(i);
            if ("Scheduled".equals(cons.getStatus())) {
                scheduled.add(cons);
            }
        }
        return scheduled;
    }
    
    public ListInterface<Consultation> getInProgressConsultations() {
        ListInterface<Consultation> inProgress = new ArrayList<>();
        
        // Iterate through the list instead of using tree iterator
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation cons = consultationList.get(i);
            if ("InProgress".equals(cons.getStatus())) {
                inProgress.add(cons);
            }
        }
        return inProgress;
    }
    
    public ListInterface<Consultation> getCompletedConsultations() {
        ListInterface<Consultation> completed = new ArrayList<>();
        
        // Iterate through the list instead of using tree iterator
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation cons = consultationList.get(i);
            if ("Completed".equals(cons.getStatus())) {
                completed.add(cons);
            }
        }
        return completed;
    }
    
    public ListInterface<Consultation> getAllConsultations() {
        return consultationList;
    }
            
    public boolean deleteConsultation(String consultationId) {
        Consultation searchKey = Consultation.createSearchKey(consultationId);
        Consultation consultation = consultationTree.search(searchKey);
        
        if (consultation != null) {
            // Remove from tree
            consultationTree.delete(consultation);
            
            // Remove from list
            for (int i = 0; i < consultationList.size(); i++) {
                Consultation current = consultationList.get(i);
                if (current.getConsultationId().equals(consultationId)) {
                    consultationList.remove(i);
                    break;
                }
            }
            return true;
        }
        return false;
    }
    
    public int getConsultationCount() {
        return consultationList.size();
    }
    
    public void clearAllConsultations() {
        consultationTree.clear();
        consultationList.clear();
    }
    
    // Helper method to get consultations by status
    public ListInterface<Consultation> getConsultationsByStatus(String status) {
        ListInterface<Consultation> result = new ArrayList<>();
        
        // Iterate through the list instead of using tree iterator
        for (int i = 0; i < consultationList.size(); i++) {
            Consultation cons = consultationList.get(i);
            if (status.equals(cons.getStatus())) {
                result.add(cons);
            }
        }
        return result;
    }
    
    // Get minimum consultation ID (for tree operations if needed)
    public Consultation getMinConsultation() {
        return consultationTree.getMin();
    }
    
    // Get maximum consultation ID (for tree operations if needed)
    public Consultation getMaxConsultation() {
        return consultationTree.getMax();
    }
    
    // Check if consultation tree is empty
    public boolean isConsultationTreeEmpty() {
        return consultationTree.isEmpty();
    }
    
    // Get consultation tree size
    public int getConsultationTreeSize() {
        return consultationTree.size();
    }
}