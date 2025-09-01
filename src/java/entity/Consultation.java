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
public class Consultation implements Comparable<Consultation>{
	private String consultationId;
        private Patient patient;
        private Doctor doctor;
        private LocalDateTime consultationDateTime;
        private LocalDateTime nextAppointment;
        private String status; //Status: Scheduled, InProgress, Complete

        
        //Default constructor
        public Consultation(){}
        
        // Parameterized constructor
        public Consultation(String consultationId, Patient patient, Doctor doctor,
                        LocalDateTime consultationDateTime, LocalDateTime nextAppointment) {
            this.consultationId = consultationId;
            this.patient = patient;
            this.doctor = doctor;
            this.consultationDateTime = consultationDateTime != null ? consultationDateTime : LocalDateTime.now();
            this.nextAppointment = nextAppointment;
            this.status = "Scheduled";
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

        public LocalDateTime getNextAppointment() { return nextAppointment; }
        public void setNextAppointment(LocalDateTime nextAppointment) { this.nextAppointment = nextAppointment; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        @Override
        public int compareTo(Consultation other){
            return this.consultationId.compareTo(other.consultationId);
        }
        
        @Override
        public String toString() {
            return "Consultation{" +
                    "ID='" + consultationId + '\'' +
                    ", Patient=" + (patient != null ? patient.getName() : "N/A") +
                    ", Doctor=" + (doctor != null ? doctor.getName() : "N/A") +
                    ", DateTime=" + consultationDateTime +
                    ", nextAppointment=" + nextAppointment +
                    ", status='" + status + '\'' +
                    '}';
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Consultation other = (Consultation) obj;
            return consultationId.equals(other.consultationId);
        }

        @Override
        public int hashCode() {
            return consultationId.hashCode();
        }

        // Helper method for searching by consultation ID
        public static Consultation createSearchKey(String consultationId) {
            Consultation searchKey = new Consultation();
            searchKey.setConsultationId(consultationId);
            return searchKey;
        }
}
