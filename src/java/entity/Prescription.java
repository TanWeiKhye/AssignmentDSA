package entity;

import java.time.LocalDateTime;
import adt.ArrayList;
import adt.MapInterface;
import adt.LinkedMap;

/**
 * Prescription - Entity class representing a medical prescription
 * @author Your Name
 * 
 * This class represents a prescription with medicine quantities.
 */
public class Prescription {
    private String prescriptionId;
    private String patientId;
    private String doctorId;
    private LocalDateTime dateTime;
    private MapInterface<String, Integer> medicines; // medicineId -> quantity
    
    /**
     * Constructs a new Prescription with the specified details
     * 
     * @param prescriptionId the unique ID of the prescription
     * @param patientId the ID of the patient
     * @param doctorId the ID of the doctor
     */
    public Prescription(String prescriptionId, String patientId, String doctorId) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = LocalDateTime.now();
        this.medicines = new LinkedMap<>(); // Use custom Map implementation
    }
    
    // ==================== GETTERS AND SETTERS ====================
    
    public String getPrescriptionId() { 
        return prescriptionId; 
    }
    
    public void setPrescriptionId(String prescriptionId) { 
        this.prescriptionId = prescriptionId; 
    }
    
    public String getPatientId() { 
        return patientId; 
    }
    
    public void setPatientId(String patientId) { 
        this.patientId = patientId; 
    }
    
    public String getDoctorId() { 
        return doctorId; 
    }
    
    public void setDoctorId(String doctorId) { 
        this.doctorId = doctorId; 
    }
    
    public LocalDateTime getDateTime() { 
        return dateTime; 
    }
    
    public void setDateTime(LocalDateTime dateTime) { 
        this.dateTime = dateTime; 
    }
    
    public MapInterface<String, Integer> getMedicines() { 
        return medicines; 
    }
    
    public void setMedicines(MapInterface<String, Integer> medicines) { 
        this.medicines = medicines; 
    }
    
    // ==================== MEDICINE MANAGEMENT METHODS ====================
    
    /**
     * Adds a medicine to the prescription with the specified quantity
     * 
     * @param medicineId the ID of the medicine to add
     * @param quantity the quantity to prescribe
     */
    public void addMedicine(String medicineId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        Integer currentQty = medicines.get(medicineId);
        if (currentQty == null) {
            medicines.put(medicineId, quantity);
        } else {
            medicines.put(medicineId, currentQty + quantity);
        }
    }
    
    /**
     * Updates the quantity of a medicine in the prescription
     * 
     * @param medicineId the ID of the medicine to update
     * @param quantity the new quantity
     */
    public void updateMedicine(String medicineId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        if (medicines.containsKey(medicineId)) {
            medicines.put(medicineId, quantity);
        } else {
            throw new IllegalArgumentException("Medicine not found in prescription: " + medicineId);
        }
    }
    
    /**
     * Removes a medicine from the prescription
     * 
     * @param medicineId the ID of the medicine to remove
     */
    public void removeMedicine(String medicineId) {
        if (!medicines.containsKey(medicineId)) {
            throw new IllegalArgumentException("Medicine not found in prescription: " + medicineId);
        }
        medicines.remove(medicineId);
    }
    
    /**
     * Gets the quantity of a specific medicine in the prescription
     * 
     * @param medicineId the ID of the medicine
     * @return the quantity of the medicine, or 0 if not found
     */
    public int getMedicineQuantity(String medicineId) {
        Integer quantity = medicines.get(medicineId);
        return quantity != null ? quantity : 0;
    }
    
    /**
     * Checks if the prescription contains a specific medicine
     * 
     * @param medicineId the ID of the medicine to check
     * @return true if the medicine is in the prescription, false otherwise
     */
    public boolean containsMedicine(String medicineId) {
        return medicines.containsKey(medicineId);
    }
    
    /**
     * Gets all medicine IDs in the prescription
     * 
     * @return ArrayList containing all medicine IDs
     */
    public ArrayList<String> getMedicineIds() {
        return medicines.keySet();
    }
    
    /**
     * Gets the total number of different medicines in the prescription
     * 
     * @return the number of medicines
     */
    public int getMedicineCount() {
        return medicines.size();
    }
    
    /**
     * Checks if the prescription is empty (contains no medicines)
     * 
     * @return true if the prescription is empty, false otherwise
     */
    public boolean isEmpty() {
        return medicines.isEmpty();
    }
    
    /**
     * Clears all medicines from the prescription
     */
    public void clearMedicines() {
        medicines.clear();
    }
    
    /**
     * Gets the total quantity of all medicines in the prescription
     * 
     * @return the total quantity
     */
    public int getTotalQuantity() {
        int total = 0;
        ArrayList<String> medicineIds = medicines.keySet();
        
        for (int i = 0; i < medicineIds.size(); i++) {
            String medicineId = medicineIds.get(i);
            total += medicines.get(medicineId);
        }
        
        return total;
    }
    
    /**
     * Validates the prescription (check for empty medicines, negative quantities, etc.)
     * 
     * @return true if the prescription is valid, false otherwise
     */
    public boolean validate() {
        if (prescriptionId == null || prescriptionId.trim().isEmpty()) {
            return false;
        }
        
        if (patientId == null || patientId.trim().isEmpty()) {
            return false;
        }
        
        if (doctorId == null || doctorId.trim().isEmpty()) {
            return false;
        }
        
        if (medicines.isEmpty()) {
            return false;
        }
        
        // Check that all quantities are positive
        ArrayList<String> medicineIds = medicines.keySet();
        for (int i = 0; i < medicineIds.size(); i++) {
            String medicineId = medicineIds.get(i);
            int quantity = medicines.get(medicineId);
            
            if (quantity <= 0) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Creates a copy of this prescription
     * 
     * @return a new Prescription with the same data
     */
    public Prescription copy() {
        Prescription copy = new Prescription(prescriptionId, patientId, doctorId);
        copy.setDateTime(dateTime);
        
        ArrayList<String> medicineIds = medicines.keySet();
        for (int i = 0; i < medicineIds.size(); i++) {
            String medicineId = medicineIds.get(i);
            copy.addMedicine(medicineId, medicines.get(medicineId));
        }
        
        return copy;
    }
    
    /**
     * Returns a string representation of the prescription
     * 
     * @return string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Prescription{")
          .append("prescriptionId='").append(prescriptionId).append('\'')
          .append(", patientId='").append(patientId).append('\'')
          .append(", doctorId='").append(doctorId).append('\'')
          .append(", dateTime=").append(dateTime)
          .append(", medicines=").append(medicines.toString())
          .append('}');
        
        return sb.toString();
    }
    
    /**
     * Compares this prescription to another object for equality
     * 
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Prescription that = (Prescription) obj;
        
        if (!prescriptionId.equals(that.prescriptionId)) return false;
        if (!patientId.equals(that.patientId)) return false;
        if (!doctorId.equals(that.doctorId)) return false;
        if (!dateTime.equals(that.dateTime)) return false;
        
        return medicines.toString().equals(that.medicines.toString());
    }
    
}