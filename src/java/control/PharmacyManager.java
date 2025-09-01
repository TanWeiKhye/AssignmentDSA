package control;

import adt.AVLTree;
import adt.LinkedQueue;
import adt.ArrayList;
import adt.LinkedMap;
import adt.MapInterface;
import entity.Medicine;
import entity.Prescription;
import entity.StockBatch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * PharmacyManager - Control class for pharmacy operations
 * @author Your Name
 * 
 * This class manages all pharmacy operations including medicine catalog,
 * stock management, prescription processing, and reporting.
 * It uses custom ADTs exclusively (no Java Collections Framework).
 */
public class PharmacyManager {
    // AVL Tree to store medicines for efficient searching and sorting
    private AVLTree<Medicine> medicineCatalog;
    
    // Custom Map to store stock batches for each medicine (medicineId -> Queue of StockBatch)
    private MapInterface<String, LinkedQueue<StockBatch>> medicineStock;
    
    // Custom Map to store prescriptions (prescriptionId -> Prescription)
    private MapInterface<String, Prescription> prescriptions;
    
    /**
     * Constructs a new PharmacyManager and initializes with sample data
     */
    public PharmacyManager() {
        medicineCatalog = new AVLTree<>();
        medicineStock = new LinkedMap<>();
        prescriptions = new LinkedMap<>();
        loadInitialData();
    }
    
    /**
     * Loads initial sample data for testing
     */
    private void loadInitialData() {
        // Load medicines
        Medicine med1 = new Medicine("M001", "Paracetamol", "Pain reliever and fever reducer", 5.50);
        Medicine med2 = new Medicine("M002", "Amoxicillin", "Antibiotic for bacterial infections", 12.75);
        Medicine med3 = new Medicine("M003", "Ibuprofen", "Anti-inflammatory pain reliever", 7.25);
        Medicine med4 = new Medicine("M004", "Omeprazole", "Reduces stomach acid", 9.80);
        Medicine med5 = new Medicine("M005", "Atorvastatin", "Lowers cholesterol", 15.20);
        
        medicineCatalog.insert(med1);
        medicineCatalog.insert(med2);
        medicineCatalog.insert(med3);
        medicineCatalog.insert(med4);
        medicineCatalog.insert(med5);
        
        // Add stock batches with different expiry dates
        addStockBatch("M001", new StockBatch("B001", "M001", 1000, 
                LocalDate.of(2025, 7, 30), LocalDate.of(2023, 1, 15)));
        addStockBatch("M001", new StockBatch("B002", "M001", 150, 
                LocalDate.of(2025, 12, 31), LocalDate.of(2023, 6, 20)));
        
        addStockBatch("M002", new StockBatch("B003", "M002", 50, 
                LocalDate.of(2025, 8, 15), LocalDate.of(2023, 2, 10)));
        addStockBatch("M002", new StockBatch("B004", "M002", 75, 
                LocalDate.of(2025, 10, 20), LocalDate.of(2023, 4, 5)));
        
        addStockBatch("M003", new StockBatch("B005", "M003", 75, 
                LocalDate.of(2025, 11, 20), LocalDate.of(2023, 3, 5)));
        addStockBatch("M003", new StockBatch("B006", "M003", 60, 
                LocalDate.of(2025, 10, 10), LocalDate.of(2023, 7, 12)));
        
        // Add expired batches for testing
        addStockBatch("M004", new StockBatch("B007", "M004", 90, 
                LocalDate.of(2025, 9, 15), LocalDate.of(2022, 5, 18))); // Expired
        addStockBatch("M004", new StockBatch("B008", "M004", 110, 
                LocalDate.of(2025, 11, 30), LocalDate.of(2023, 8, 22)));
        
        addStockBatch("M005", new StockBatch("B009", "M005", 40, 
                LocalDate.of(2025, 7, 25), LocalDate.of(2022, 6, 30))); // Expired
        addStockBatch("M005", new StockBatch("B010", "M005", 85, 
                LocalDate.of(2025, 9, 15), LocalDate.of(2023, 9, 10)));
    }
    
    // ==================== MEDICINE MANAGEMENT METHODS ====================
    
    /**
     * Adds a new medicine to the catalog
     * @param medicine the medicine to add
     */
    public void addMedicine(Medicine medicine) {
        medicineCatalog.insert(medicine);
        if (!medicineStock.containsKey(medicine.getMedicineId())) {
            medicineStock.put(medicine.getMedicineId(), new LinkedQueue<>());
        }
    }
    
    /**
     * Finds a medicine by ID
     * @param medicineId the ID of the medicine to find
     * @return the medicine if found, null otherwise
     */
    public Medicine findMedicine(String medicineId) {
        Medicine searchKey = new Medicine(medicineId, "", "", 0.0);
        return medicineCatalog.search(searchKey);
    }
    
    /**
     * Gets all medicines as an iterator
     * @return iterator over all medicines
     */
    public Iterator<Medicine> getAllMedicines() {
        return medicineCatalog.iterator();
    }
    
    /**
     * Gets all medicine IDs
     * @return ArrayList containing all medicine IDs
     */
    public ArrayList<String> getAllMedicineIds() {
        ArrayList<String> medicineIds = new ArrayList<>();
        Iterator<Medicine> iterator = medicineCatalog.iterator();
        
        while (iterator.hasNext()) {
            Medicine medicine = iterator.next();
            medicineIds.add(medicine.getMedicineId());
        }
        
        return medicineIds;
    }
    
    /**
     * Gets all medicines as a list
     * @return ArrayList containing all medicines
     */
    public ArrayList<Medicine> getAllMedicinesList() {
        ArrayList<Medicine> medicines = new ArrayList<>();
        Iterator<Medicine> iterator = medicineCatalog.iterator();
        
        while (iterator.hasNext()) {
            medicines.add(iterator.next());
        }
        
        return medicines;
    }
    
    /**
     * Checks if a medicine exists
     * @param medicineId the ID of the medicine to check
     * @return true if the medicine exists, false otherwise
     */
    public boolean medicineExists(String medicineId) {
        return findMedicine(medicineId) != null;
    }
    
    // ==================== STOCK MANAGEMENT METHODS ====================
    
    /**
     * Adds a stock batch for a medicine
     * @param medicineId the ID of the medicine
     * @param batch the stock batch to add
     */
    public void addStockBatch(String medicineId, StockBatch batch) {
        if (!medicineStock.containsKey(medicineId)) {
            medicineStock.put(medicineId, new LinkedQueue<>());
        }
        medicineStock.get(medicineId).enqueue(batch);
    }
    
    /**
     * Gets available (non-expired) stock for a medicine
     * @param medicineId the ID of the medicine
     * @return the quantity of available stock
     */
    public int getAvailableStock(String medicineId) {
        if (!medicineStock.containsKey(medicineId)) {
            return 0;
        }
        
        int total = 0;
        LinkedQueue<StockBatch> stockQueue = medicineStock.get(medicineId);
        LinkedQueue<StockBatch> tempQueue = new LinkedQueue<>();
        
        // Calculate total by going through all batches
        while (!stockQueue.isEmpty()) {
            StockBatch batch = stockQueue.dequeue();
            if (!batch.isExpired()) {
                total += batch.getQuantity();
            }
            tempQueue.enqueue(batch);
        }
        
        // Restore the queue
        while (!tempQueue.isEmpty()) {
            stockQueue.enqueue(tempQueue.dequeue());
        }
        
        return total;
    }
    
    /**
     * Gets total stock (including expired) for a medicine
     * @param medicineId the ID of the medicine
     * @return the quantity of total stock
     */
    public int getTotalStock(String medicineId) {
        if (!medicineStock.containsKey(medicineId)) {
            return 0;
        }
        
        int total = 0;
        LinkedQueue<StockBatch> stockQueue = medicineStock.get(medicineId);
        LinkedQueue<StockBatch> tempQueue = new LinkedQueue<>();
        
        while (!stockQueue.isEmpty()) {
            StockBatch batch = stockQueue.dequeue();
            total += batch.getQuantity();
            tempQueue.enqueue(batch);
        }
        
        // Restore the queue
        while (!tempQueue.isEmpty()) {
            stockQueue.enqueue(tempQueue.dequeue());
        }
        
        return total;
    }
    
    /**
     * Dispenses medicine using FIFO principle (oldest first, non-expired only)
     * @param medicineId the ID of the medicine to dispense
     * @param quantity the quantity to dispense
     * @return true if successful, false if insufficient stock
     */
    public boolean dispenseMedicine(String medicineId, int quantity) {
        if (!medicineStock.containsKey(medicineId)) {
            return false;
        }
        
        LinkedQueue<StockBatch> stockQueue = medicineStock.get(medicineId);
        int remainingQuantity = quantity;
        
        // Temporary queues to hold batches while processing
        LinkedQueue<StockBatch> tempQueue = new LinkedQueue<>();
        LinkedQueue<StockBatch> expiredQueue = new LinkedQueue<>();
        
        try {
            while (remainingQuantity > 0 && !stockQueue.isEmpty()) {
                StockBatch batch = stockQueue.dequeue();
                
                if (batch.isExpired()) {
                    // Skip expired batches - move to separate queue
                    expiredQueue.enqueue(batch);
                    continue;
                }
                
                if (batch.getQuantity() > remainingQuantity) {
                    // Only use part of this batch
                    batch.setQuantity(batch.getQuantity() - remainingQuantity);
                    tempQueue.enqueue(batch);
                    remainingQuantity = 0;
                } else {
                    // Use the entire batch
                    remainingQuantity -= batch.getQuantity();
                    // Don't add this batch back as it's fully used
                }
            }
            
            // Put the remaining non-expired batches back in the queue
            while (!tempQueue.isEmpty()) {
                stockQueue.enqueue(tempQueue.dequeue());
            }
            
            // Put expired batches back at the end (they shouldn't be used)
            while (!expiredQueue.isEmpty()) {
                stockQueue.enqueue(expiredQueue.dequeue());
            }
            
            // If we still have remaining quantity, restoration failed
            if (remainingQuantity > 0) {
                // Restore everything and return failure
                while (!stockQueue.isEmpty()) {
                    tempQueue.enqueue(stockQueue.dequeue());
                }
                while (!tempQueue.isEmpty()) {
                    stockQueue.enqueue(tempQueue.dequeue());
                }
                return false;
            }
            
            return true;
        } catch (Exception e) {
            // In case of error, restore the stock queue
            while (!tempQueue.isEmpty()) {
                stockQueue.enqueue(tempQueue.dequeue());
            }
            while (!expiredQueue.isEmpty()) {
                stockQueue.enqueue(expiredQueue.dequeue());
            }
            return false;
        }
    }
    
    /**
     * Removes expired batches for a specific medicine
     * @param medicineId the ID of the medicine
     * @return the number of batches removed
     */
    public int removeExpiredBatches(String medicineId) {
        if (!medicineStock.containsKey(medicineId)) {
            return 0;
        }
        
        int removedCount = 0;
        LinkedQueue<StockBatch> stockQueue = medicineStock.get(medicineId);
        LinkedQueue<StockBatch> tempQueue = new LinkedQueue<>();
        
        while (!stockQueue.isEmpty()) {
            StockBatch batch = stockQueue.dequeue();
            if (batch.isExpired()) {
                removedCount++;
                // Don't add expired batches back (they're removed)
            } else {
                tempQueue.enqueue(batch);
            }
        }
        
        // Restore only non-expired batches
        while (!tempQueue.isEmpty()) {
            stockQueue.enqueue(tempQueue.dequeue());
        }
        
        // If no batches left for this medicine, remove it from the map
        if (stockQueue.isEmpty()) {
            medicineStock.remove(medicineId);
        }
        
        return removedCount;
    }
    
    /**
     * Removes expired batches from all medicines
     * @return the total number of batches removed
     */
    public int removeAllExpiredBatches() {
        int totalRemoved = 0;
        ArrayList<String> medicineIds = getAllMedicineIds();
        
        for (int i = 0; i < medicineIds.size(); i++) {
            totalRemoved += removeExpiredBatches(medicineIds.get(i));
        }
        
        return totalRemoved;
    }
    
    /**
     * Gets batches that will expire within a specified number of days
     * @param daysThreshold the number of days to check
     * @return queue of batches expiring soon
     */
    public LinkedQueue<StockBatch> getBatchesExpiringSoon(int daysThreshold) {
        LinkedQueue<StockBatch> expiringBatches = new LinkedQueue<>();
        LocalDate thresholdDate = LocalDate.now().plusDays(daysThreshold);
        
        ArrayList<String> medicineIds = getAllMedicineIds();
        
        for (int i = 0; i < medicineIds.size(); i++) {
            String medicineId = medicineIds.get(i);
            
            if (!medicineStock.containsKey(medicineId)) {
                continue;
            }
            
            LinkedQueue<StockBatch> stockQueue = medicineStock.get(medicineId);
            LinkedQueue<StockBatch> tempQueue = new LinkedQueue<>();
            
            while (!stockQueue.isEmpty()) {
                StockBatch batch = stockQueue.dequeue();
                tempQueue.enqueue(batch);
                
                if (!batch.isExpired() && batch.getExpiryDate().isBefore(thresholdDate)) {
                    expiringBatches.enqueue(batch);
                }
            }
            
            // Restore the queue
            while (!tempQueue.isEmpty()) {
                stockQueue.enqueue(tempQueue.dequeue());
            }
        }
        
        return expiringBatches;
    }
    
    // ==================== PRESCRIPTION MANAGEMENT METHODS ====================
    
    /**
     * Adds a prescription
     * @param prescription the prescription to add
     */
    public void addPrescription(Prescription prescription) {
        prescriptions.put(prescription.getPrescriptionId(), prescription);
    }
    
    /**
     * Gets a prescription by ID
     * @param prescriptionId the ID of the prescription
     * @return the prescription if found, null otherwise
     */
    public Prescription getPrescription(String prescriptionId) {
        return prescriptions.get(prescriptionId);
    }
    
    /**
     * Checks if a prescription exists
     * @param prescriptionId the ID of the prescription
     * @return true if the prescription exists, false otherwise
     */
    public boolean prescriptionExists(String prescriptionId) {
        return prescriptions.get(prescriptionId) != null;
    }
    
    /**
     * Processes a prescription by dispensing all required medicines
     * @param prescriptionId the ID of the prescription to process
     * @return true if successful, false if insufficient stock or other error
     */
    public boolean processPrescription(String prescriptionId) {
        Prescription prescription = prescriptions.get(prescriptionId);
        if (prescription == null) {
            return false;
        }
        
        // Get all medicine IDs from the prescription
        ArrayList<String> medicineIds = prescription.getMedicineIds();
        
        // First pass: Check if all medicines are available (non-expired)
        for (int i = 0; i < medicineIds.size(); i++) {
            String medicineId = medicineIds.get(i);
            int quantity = prescription.getMedicineQuantity(medicineId);
            
            if (getAvailableStock(medicineId) < quantity) {
                return false; // Not enough non-expired stock
            }
        }
        
        // Second pass: Dispense all medicines (only non-expired)
        for (int i = 0; i < medicineIds.size(); i++) {
            String medicineId = medicineIds.get(i);
            int quantity = prescription.getMedicineQuantity(medicineId);
            
            if (!dispenseMedicine(medicineId, quantity)) {
                return false; // Dispensing failed
            }
        }
        
        return true;
    }
    
    /**
     * Gets all prescriptions
     * @return ArrayList containing all prescriptions
     */
    public ArrayList<Prescription> getAllPrescriptions() {
        ArrayList<Prescription> allPrescriptions = new ArrayList<>();
        ArrayList<String> prescriptionIds = prescriptions.keySet();
        
        for (int i = 0; i < prescriptionIds.size(); i++) {
            Prescription prescription = prescriptions.get(prescriptionIds.get(i));
            if (prescription != null) {
                allPrescriptions.add(prescription);
            }
        }
        
        return allPrescriptions;
    }
    
    // ==================== REPORTING METHODS ====================
    
    /**
     * Generates a stock report showing available and expired stock
     * @return the stock report as a formatted string
     */
    public String generateStockReport() {
        StringBuilder report = new StringBuilder();
        report.append("MEDICINE STOCK REPORT\n");
        report.append("=====================\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        
        ArrayList<Medicine> medicines = getAllMedicinesList();
        for (int i = 0; i < medicines.size(); i++) {
            Medicine medicine = medicines.get(i);
            int totalStock = getTotalStock(medicine.getMedicineId());
            int availableStock = getAvailableStock(medicine.getMedicineId());
            int expiredStock = totalStock - availableStock;
            
            String status = availableStock < 50 ? "LOW STOCK" : "OK";
            report.append(String.format("%s (%s): %d units available, %d units expired [%s]\n", 
                    medicine.getName(), medicine.getMedicineId(), availableStock, expiredStock, status));
        }
        
        return report.toString();
    }
    
    /**
     * Generates an expiry report showing expired and soon-to-expire batches
     * @return the expiry report as a formatted string
     */
    public String generateExpiryReport() {
        StringBuilder report = new StringBuilder();
        report.append("MEDICINE EXPIRY REPORT\n");
        report.append("======================\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        
        LocalDate now = LocalDate.now();
        LocalDate warningDate = now.plusMonths(3); // Warn about batches expiring in 3 months
        
        ArrayList<String> medicineIds = getAllMedicineIds();
        
        for (int i = 0; i < medicineIds.size(); i++) {
            String medicineId = medicineIds.get(i);
            Medicine medicine = findMedicine(medicineId);
            if (medicine == null) continue;
            
            report.append(String.format("%s (%s):\n", medicine.getName(), medicineId));
            
            if (!medicineStock.containsKey(medicineId)) {
                report.append("  No stock available\n");
                continue;
            }
            
            LinkedQueue<StockBatch> stockQueue = medicineStock.get(medicineId);
            LinkedQueue<StockBatch> tempQueue = new LinkedQueue<>();
            
            boolean hasExpiring = false;
            
            while (!stockQueue.isEmpty()) {
                StockBatch batch = stockQueue.dequeue();
                tempQueue.enqueue(batch);
                
                if (batch.isExpired() || batch.getExpiryDate().isBefore(warningDate)) {
                    hasExpiring = true;
                    String status = batch.isExpired() ? "EXPIRED" : "Expiring soon";
                    report.append(String.format("  - Batch %s: %d units, Expiry: %s (%s)\n",
                            batch.getBatchId(), batch.getQuantity(), 
                            batch.getExpiryDate(), status));
                }
            }
            
            // Restore the queue
            while (!tempQueue.isEmpty()) {
                stockQueue.enqueue(tempQueue.dequeue());
            }
            
            if (!hasExpiring) {
                report.append("  No expiring batches\n");
            }
            
            report.append("\n");
        }
        
        return report.toString();
    }
    
    /**
     * Generates a prescription report
     * @return the prescription report as a formatted string
     */
    public String generatePrescriptionReport() {
        StringBuilder report = new StringBuilder();
        report.append("PRESCRIPTION REPORT\n");
        report.append("===================\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        
        ArrayList<Prescription> allPrescriptions = getAllPrescriptions();
        
        if (allPrescriptions.isEmpty()) {
            report.append("No prescriptions found.\n");
        } else {
            for (int i = 0; i < allPrescriptions.size(); i++) {
                Prescription prescription = allPrescriptions.get(i);
                report.append(String.format("Prescription ID: %s\n", prescription.getPrescriptionId()));
                report.append(String.format("Patient ID: %s, Doctor ID: %s\n", 
                        prescription.getPatientId(), prescription.getDoctorId()));
                report.append(String.format("Date: %s\n", prescription.getDateTime()));
                
                ArrayList<String> medicineIds = prescription.getMedicineIds();
                if (medicineIds.isEmpty()) {
                    report.append("  No medicines prescribed\n");
                } else {
                    report.append("  Medicines:\n");
                    for (int j = 0; j < medicineIds.size(); j++) {
                        String medicineId = medicineIds.get(j);
                        int quantity = prescription.getMedicineQuantity(medicineId);
                        Medicine medicine = findMedicine(medicineId);
                        String medicineName = medicine != null ? medicine.getName() : medicineId;
                        
                        report.append(String.format("  - %s: %d units\n", medicineName, quantity));
                    }
                }
                report.append("\n");
            }
        }
        
        return report.toString();
    }
    
    
    /**
     * Gets the total number of prescriptions
     * @return the number of prescriptions
     */
    public int getPrescriptionCount() {
        return prescriptions.keySet().size();
    }
    
    /**
     * Clears all data (for testing/reset purposes)
     */
    public void clearAllData() {
        medicineCatalog.clear();
        medicineStock.clear();
        prescriptions.clear();
    }
}