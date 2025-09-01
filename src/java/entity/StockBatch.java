// StockBatch.java - Added validation method
package entity;

import java.time.LocalDate;

public class StockBatch {
    private String batchId;
    private String medicineId;
    private int quantity;
    private LocalDate expiryDate;
    private LocalDate receivedDate;
    
    public StockBatch(String batchId, String medicineId, int quantity, 
                     LocalDate expiryDate, LocalDate receivedDate) {
        this.batchId = batchId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.receivedDate = receivedDate;
    }
    
    // Check if batch is expired
    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }
    
    // Check if batch will expire within days
    public boolean expiresWithin(int days) {
        return expiryDate.isBefore(LocalDate.now().plusDays(days));
    }
    
    // Getters and setters
    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
    
    public String getMedicineId() { return medicineId; }
    public void setMedicineId(String medicineId) { this.medicineId = medicineId; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    
    public LocalDate getReceivedDate() { return receivedDate; }
    public void setReceivedDate(LocalDate receivedDate) { this.receivedDate = receivedDate; }
    
    @Override
    public String toString() {
        return "StockBatch{" +
                "batchId='" + batchId + '\'' +
                ", medicineId='" + medicineId + '\'' +
                ", quantity=" + quantity +
                ", expiryDate=" + expiryDate +
                ", receivedDate=" + receivedDate +
                ", expired=" + isExpired() +
                '}';
    }
}