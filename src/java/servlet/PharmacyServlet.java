// PharmacyServlet.java - Added expiry management features
package servlet;

import adt.LinkedQueue;
import control.PharmacyManager;
import entity.Medicine;
import entity.Prescription;
import entity.StockBatch;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/PharmacyServlet")
public class PharmacyServlet extends HttpServlet {
    private PharmacyManager pharmacyManager;
    
    @Override
    public void init() throws ServletException {
        super.init();
        pharmacyManager = new PharmacyManager();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        switch (action) {
            case "addMedicine":
                addMedicine(request, response);
                break;
            case "addStock":
                addStock(request, response);
                break;
            case "processPrescription":
                processPrescription(request, response);
                break;
            case "generateStockReport":
                generateStockReport(request, response);
                break;
            case "generateExpiryReport":
                generateExpiryReport(request, response);
                break;
            case "removeExpired":
                removeExpiredBatches(request, response);
                break;
            case "checkExpiring":
                checkExpiringBatches(request, response);
                break;
            default:
                request.setAttribute("message", "Invalid action");
                request.getRequestDispatcher("pharmacy.jsp").forward(request, response);
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("pharmacy.jsp").forward(request, response);
    }
    
    private void addMedicine(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String medicineId = request.getParameter("medicineId");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            
            Medicine medicine = new Medicine(medicineId, name, description, price);
            pharmacyManager.addMedicine(medicine);
            
            request.setAttribute("message", "Medicine '" + name + "' added successfully");
        } catch (Exception e) {
            request.setAttribute("message", "Error adding medicine: " + e.getMessage());
        }
        
        request.getRequestDispatcher("pharmacy.jsp").forward(request, response);
    }
    
    private void addStock(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String batchId = request.getParameter("batchId");
            String medicineId = request.getParameter("medicineId");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            LocalDate expiryDate = LocalDate.parse(request.getParameter("expiryDate"));
            LocalDate receivedDate = LocalDate.now();
            
            // Validate expiry date
            if (expiryDate.isBefore(LocalDate.now())) {
                request.setAttribute("message", "Error: Expiry date cannot be in the past");
                request.getRequestDispatcher("pharmacy.jsp").forward(request, response);
                return;
            }
            
            StockBatch batch = new StockBatch(batchId, medicineId, quantity, expiryDate, receivedDate);
            pharmacyManager.addStockBatch(medicineId, batch);
            
            request.setAttribute("message", "Stock added successfully");
        } catch (Exception e) {
            request.setAttribute("message", "Error adding stock: " + e.getMessage());
        }
        
        request.getRequestDispatcher("pharmacy.jsp").forward(request, response);
    }
    
    private void processPrescription(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String prescriptionId = request.getParameter("prescriptionId");
            String patientId = request.getParameter("patientId");
            String doctorId = request.getParameter("doctorId");
            
            Prescription prescription = new Prescription(prescriptionId, patientId, doctorId);
            
            String medicineId = request.getParameter("medicineId");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            prescription.addMedicine(medicineId, quantity);
            
            pharmacyManager.addPrescription(prescription);
            boolean success = pharmacyManager.processPrescription(prescriptionId);
            
            if (success) {
                request.setAttribute("message", "Prescription processed successfully for patient " + patientId);
            } else {
                // Check if failure was due to expired medicine
                int availableStock = pharmacyManager.getAvailableStock(medicineId);
                int totalStock = pharmacyManager.getTotalStock(medicineId);
                
                if (availableStock < totalStock) {
                    request.setAttribute("message", "Failed to process prescription - some stock is expired. Available: " 
                            + availableStock + ", Required: " + quantity);
                } else {
                    request.setAttribute("message", "Failed to process prescription - insufficient stock. Available: " 
                            + availableStock + ", Required: " + quantity);
                }
            }
        } catch (Exception e) {
            request.setAttribute("message", "Error processing prescription: " + e.getMessage());
        }
        
        request.getRequestDispatcher("pharmacy.jsp").forward(request, response);
    }
    
    private void generateStockReport(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String report = pharmacyManager.generateStockReport();
        request.setAttribute("report", report);
        request.setAttribute("reportType", "stock");
        request.getRequestDispatcher("pharmacy.jsp").forward(request, response);
    }
    
    private void generateExpiryReport(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String report = pharmacyManager.generateExpiryReport();
        request.setAttribute("report", report);
        request.setAttribute("reportType", "expiry");
        request.getRequestDispatcher("pharmacy.jsp").forward(request, response);
    }
    
// Update the removeExpiredBatches method in PharmacyServlet
private void removeExpiredBatches(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    try {
        String medicineId = request.getParameter("medicineId");
        int removedCount;
        
        if (medicineId == null || medicineId.trim().isEmpty()) {
            // Remove expired from all medicines using our new method
            removedCount = pharmacyManager.removeAllExpiredBatches();
            request.setAttribute("message", "Removed " + removedCount + " expired batches from all medicines");
        } else {
            // Remove expired from specific medicine
            removedCount = pharmacyManager.removeExpiredBatches(medicineId);
            Medicine medicine = pharmacyManager.findMedicine(medicineId);
            String medName = medicine != null ? medicine.getName() : medicineId;
            request.setAttribute("message", "Removed " + removedCount + " expired batches from " + medName);
        }
    } catch (Exception e) {
        request.setAttribute("message", "Error removing expired batches: " + e.getMessage());
    }
    
    request.getRequestDispatcher("pharmacy.jsp").forward(request, response);
}
    
    private void checkExpiringBatches(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int days = Integer.parseInt(request.getParameter("days"));
            LinkedQueue<StockBatch> expiringBatches = pharmacyManager.getBatchesExpiringSoon(days);
            
            StringBuilder report = new StringBuilder();
            report.append("BATCHES EXPIRING WITHIN ").append(days).append(" DAYS\n");
            report.append("===================================\n\n");
            
            if (expiringBatches.isEmpty()) {
                report.append("No batches expiring within ").append(days).append(" days.");
            } else {
                while (!expiringBatches.isEmpty()) {
                    StockBatch batch = expiringBatches.dequeue();
                    Medicine medicine = pharmacyManager.findMedicine(batch.getMedicineId());
                    String medName = medicine != null ? medicine.getName() : batch.getMedicineId();
                    
                    report.append(String.format("• %s (Batch %s): %d units, Expires: %s\n", 
                            medName, batch.getBatchId(), batch.getQuantity(), batch.getExpiryDate()));
                }
            }
            
            request.setAttribute("report", report.toString());
            request.setAttribute("reportType", "expiring");
            request.getRequestDispatcher("pharmacy.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("message", "Error checking expiring batches: " + e.getMessage());
            request.getRequestDispatcher("pharmacy.jsp").forward(request, response);
        }
    }
}