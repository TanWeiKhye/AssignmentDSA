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
    private static int consultationCounter = 1011;
    
    public MaintainConsultation() {
        consultationTree = new AVLTree<>();
        consultationList = new ArrayList<>();
    }
    
    public String generateConsultationId() {
        consultationCounter++;
        return String.format("CONST%04d", consultationCounter);
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
        for (int i = 0; i <= consultationList.size(); i++) {
            Consultation consultation = consultationList.get(i);
            
            // Add null checks for patient and doctor
            String patientIc = (consultation.getPatient() != null) ? consultation.getPatient().getIc() : "UNKNOWN_PATIENT";
            String doctorIc = (consultation.getDoctor() != null) ? consultation.getDoctor().getIc() : "UNKNOWN_DOCTOR";
            
            writer.write(String.join(",",
                consultation.getConsultationId(),
                patientIc,
                doctorIc,
                consultation.getConsultationDateTime() != null ? 
                    consultation.getConsultationDateTime().toString() : "",
                consultation.getNextAppointment() != null ? 
                    consultation.getNextAppointment().toString() : "",
                consultation.getStatus()
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
    
    public boolean startConsultation(String consultationId) {
        Consultation searchKey = Consultation.createSearchKey(consultationId);
        Consultation consultation = consultationTree.search(searchKey);
        
        if (consultation != null && "Scheduled".equals(consultation.getStatus())) {
            consultation.setStatus("InProgress");
            return true;
        }
        return false;
    }
    
    public boolean completeConsultation(String consultationId) {
        Consultation searchKey = Consultation.createSearchKey(consultationId);
        Consultation consultation = consultationTree.search(searchKey);
        
        if (consultation != null && "InProgress".equals(consultation.getStatus())) {
            consultation.setStatus("Completed");
            return true;
        }
        return false;
    }
    
    public boolean cancelConsultation(String consultationId) {
        Consultation searchKey = Consultation.createSearchKey(consultationId);
        Consultation consultation = consultationTree.search(searchKey);
        
        if (consultation != null && !"Completed".equals(consultation.getStatus())) {
            consultation.setStatus("Cancelled");
            return true;
        }
        return false;
    }
    
    public boolean scheduleFollowUp(String consultationId, LocalDateTime nextAppointment) {
        Consultation searchKey = Consultation.createSearchKey(consultationId);
        Consultation consultation = consultationTree.search(searchKey);
        
        if (consultation != null && "Completed".equals(consultation.getStatus())) {
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
    
    public Consultation findConsultationById(String consultationId) {
        Consultation searchKey = Consultation.createSearchKey(consultationId);
        return consultationTree.search(searchKey);
    }
    
    public ListInterface<Consultation> getConsultationsByPatient(String patientIc) {
        ListInterface<Consultation> patientConsultations = new ArrayList<>();
        
        // Iterate through the list instead of using tree iterator
        for (int i = 0; i <= consultationList.size(); i++) {
            Consultation cons = consultationList.get(i);
            if (cons.getPatient() != null && cons.getPatient().getIc().equals(patientIc)) {
                patientConsultations.add(cons);
            }
        }
        return patientConsultations;
    }
    
    public ListInterface<Consultation> getConsultationsByDoctor(String doctorIc) {
        ListInterface<Consultation> doctorConsultations = new ArrayList<>();
        
        // Iterate through the list instead of using tree iterator
        for (int i = 1; i <= consultationList.size(); i++) {
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
        for (int i = 0; i <= consultationList.size(); i++) {
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
        for (int i = 1; i <= consultationList.size(); i++) {
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
        for (int i = 1; i <= consultationList.size(); i++) {
            Consultation cons = consultationList.get(i);
            if ("Completed".equals(cons.getStatus())) {
                completed.add(cons);
            }
        }
        return completed;
    }
    
    public Consultation[] getAllConsultations() {
        Consultation[] array = new Consultation[consultationList.size()];
        for (int i = 1; i <= consultationList.size(); i++) {
            array[i-1] = consultationList.get(i);
        }
        return array;
    }
    
    public boolean isDoctorAvailable(Doctor doctor, LocalDateTime dateTime) {
        // Iterate through the list instead of using tree iterator
        for (int i = 1; i <= consultationList.size(); i++) {
            Consultation cons = consultationList.get(i);
            if (cons.getDoctor() != null && 
                cons.getDoctor().getIc().equals(doctor.getIc()) && 
                cons.getConsultationDateTime().equals(dateTime) &&
                !"Cancelled".equals(cons.getStatus())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean rescheduleConsultation(String consultationId, LocalDateTime newDateTime) {
        Consultation searchKey = Consultation.createSearchKey(consultationId);
        Consultation consultation = consultationTree.search(searchKey);
        
        if (consultation != null && !"Completed".equals(consultation.getStatus())) {
            if (isDoctorAvailable(consultation.getDoctor(), newDateTime)) {
                consultation.setConsultationDateTime(newDateTime);
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteConsultation(String consultationId) {
        Consultation searchKey = Consultation.createSearchKey(consultationId);
        Consultation consultation = consultationTree.search(searchKey);
        
        if (consultation != null) {
            // Remove from tree
            consultationTree.delete(consultation);
            
            // Remove from list
            for (int i = 1; i <= consultationList.size(); i++) {
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
        for (int i = 1; i <= consultationList.size(); i++) {
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