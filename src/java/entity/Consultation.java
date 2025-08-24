/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDateTime;
/**
 *
 * @author TanWeiKhye
 */
public class Consultation {
	private String consultationId;
        private Patient patient;
        private Doctor doctor;
        private LocalDateTime consultationDateTime;
        private String diagnosis;
        private String notes;
        private double fee;
        
        //Default constructor
        public Consultation(){
            this.consultationDateTime = LocalDateTime.now();
        }
        
        // Parameterized constructor
        public Consultation(String consultationId, Patient patient, Doctor doctor,
                        LocalDateTime consultationDateTime, String diagnosis,
                        String notes, double fee, LocalDateTime nextAppointment) {
            this.consultationId = consultationId;
            this.patient = patient;
            this.doctor = doctor;
            this.consultationDateTime = consultationDateTime != null ? consultationDateTime : LocalDateTime.now();
            this.diagnosis = diagnosis;
            this.notes = notes;
            this.fee = fee;
        }
        
        // Getters and Setters
        public String getConsultationId() { return consultationId; }
        public void setConsultationId(String consultationId) { this.consultationId = consultationId; }

        public Patient getPatient() { return patient; }
        public void setPatient(Patient patient) { this.patient = patient; }

        public Doctor getDoctor() { return doctor; }
        public void setDoctor(Doctor doctor) { this.doctor = doctor; }

        public LocalDateTime getConsultationDateTime() { return consultationDateTime; }
        public void setConsultationDateTime(LocalDateTime consultationDateTime) { 
            this.consultationDateTime = consultationDateTime; 
        }

        public String getDiagnosis() { return diagnosis; }
        public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }

        public double getFee() { return fee; }
        public void setFee(double fee) { this.fee = fee; }
        
        @Override
        public String toString() {
            return "Consultation{" +
                    "ID='" + consultationId + '\'' +
                    ", Patient=" + (patient != null ? patient.getName() : "N/A") +
                    ", Doctor=" + (doctor != null ? doctor.getName() : "N/A") +
                    ", DateTime=" + consultationDateTime +
                    ", Diagnosis='" + diagnosis + '\'' +
                    ", Notes='" + notes + '\'' +
                    ", Fee=" + fee +
                    '}';
        }
}
