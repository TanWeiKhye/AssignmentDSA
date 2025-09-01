<%-- pharmacy.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="control.PharmacyManager" %>
<%@ page import="entity.Medicine" %>
<%@ page import="java.util.Iterator" %>
<%
    PharmacyManager pharmacyManager = (PharmacyManager) application.getAttribute("pharmacyManager");
    if (pharmacyManager == null) {
        pharmacyManager = new PharmacyManager();
        application.setAttribute("pharmacyManager", pharmacyManager);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Pharmacy Management</title>
    <link rel="stylesheet" type="text/css" href="css/css.css">
</head>
<body>
    <div class="container">
        <h1>Pharmacy Management System</h1>
        
        <% if (request.getAttribute("message") != null) { %>
            <div class="message"><%= request.getAttribute("message") %></div>
        <% } %>
        
        <div class="sections">
            <!-- Add Medicine Form -->
            <div class="section">
                <h2>Add New Medicine</h2>
                <form action="PharmacyServlet" method="post">
                    <input type="hidden" name="action" value="addMedicine">
                    <div class="form-group">
                        <label>Medicine ID:</label>
                        <input type="text" name="medicineId" required>
                    </div>
                    <div class="form-group">
                        <label>Name:</label>
                        <input type="text" name="name" required>
                    </div>
                    <div class="form-group">
                        <label>Description:</label>
                        <input type="text" name="description">
                    </div>
                    <div class="form-group">
                        <label>Price:</label>
                        <input type="number" step="0.01" name="price" required>
                    </div>
                    <button type="submit">Add Medicine</button>
                </form>
            </div>
            
            <!-- Add Stock Form -->
            <div class="section">
                <h2>Add Stock</h2>
                <form action="PharmacyServlet" method="post">
                    <input type="hidden" name="action" value="addStock">
                    <div class="form-group">
                        <label>Batch ID:</label>
                        <input type="text" name="batchId" required>
                    </div>
                    <div class="form-group">
                        <label>Medicine ID:</label>
                        <input type="text" name="medicineId" required>
                    </div>
                    <div class="form-group">
                        <label>Quantity:</label>
                        <input type="number" name="quantity" required>
                    </div>
                    <div class="form-group">
                        <label>Expiry Date:</label>
                        <input type="date" name="expiryDate" required>
                    </div>
                    <button type="submit">Add Stock</button>
                </form>
            </div>
            
            <!-- Process Prescription Form -->
            <div class="section">
                <h2>Process Prescription</h2>
                <form action="PharmacyServlet" method="post">
                    <input type="hidden" name="action" value="processPrescription">
                    <div class="form-group">
                        <label>Prescription ID:</label>
                        <input type="text" name="prescriptionId" required>
                    </div>
                    <div class="form-group">
                        <label>Patient ID:</label>
                        <input type="text" name="patientId" required>
                    </div>
                    <div class="form-group">
                        <label>Doctor ID:</label>
                        <input type="text" name="doctorId" required>
                    </div>
                    <div class="form-group">
                        <label>Medicine ID:</label>
                        <input type="text" name="medicineId" required>
                    </div>
                    <div class="form-group">
                        <label>Quantity:</label>
                        <input type="number" name="quantity" required>
                    </div>
                    <button type="submit">Process Prescription</button>
                </form>
            </div>
            
            <!-- Reports Section -->
            <div class="section">
                <h2>Reports</h2>
                <div class="report-buttons">
                    <form action="PharmacyServlet" method="post">
                        <input type="hidden" name="action" value="generateStockReport">
                        <button type="submit">Generate Stock Report</button>
                    </form>
                    <form action="PharmacyServlet" method="post">
                        <input type="hidden" name="action" value="generateExpiryReport">
                        <button type="submit">Generate Expiry Report</button>
                    </form>
                </div>
                
                <% if (request.getAttribute("report") != null) { %>
                    <div class="report">
                        <h3><%= "stock".equals(request.getAttribute("reportType")) ? 
                                "Stock Report" : "Expiry Report" %></h3>
                        <pre><%= request.getAttribute("report") %></pre>
                    </div>
                <% } %>
            </div>
        </div>
        

<!-- Expiry Management Section -->
<div class="section">
    <h2><i class="fas fa-calendar-times"></i> Expiry Management</h2>
    <div class="form-group">
        <form action="PharmacyServlet" method="post">
            <input type="hidden" name="action" value="removeExpired">
            <label>Remove Expired Batches:</label>
            <select name="medicineId">
                <option value="">All Medicines</option>
                <%
                    Iterator<Medicine> medIterator = pharmacyManager.getAllMedicines();
                    while (medIterator.hasNext()) {
                        Medicine medicine = medIterator.next();
                %>
                <option value="<%= medicine.getMedicineId() %>"><%= medicine.getName() %></option>
                <% } %>
            </select>
            <button type="submit" class="btn-danger"><i class="fas fa-trash"></i> Remove Expired</button>
        </form>
    </div>
    
    <div class="form-group">
        <form action="PharmacyServlet" method="post">
            <input type="hidden" name="action" value="checkExpiring">
            <label>Check Batches Expiring Within:</label>
            <input type="number" name="days" value="30" min="1" max="365">
            <span>days</span>
            <button type="submit" class="btn-secondary"><i class="fas fa-search"></i> Check Expiring</button>
        </form>
    </div>
</div>
            
        <!-- Medicine Catalog -->
        <div class="section">
            <h2>Medicine Catalog</h2>
            <table>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Price</th>
                    <th>Stock</th>
                </tr>
                <%
                    Iterator<Medicine> iterator = pharmacyManager.getAllMedicines();
                    while (iterator.hasNext()) {
                        Medicine medicine = iterator.next();
                        int stock = pharmacyManager.getTotalStock(medicine.getMedicineId());
                %>
                <tr>
                    <td><%= medicine.getMedicineId() %></td>
                    <td><%= medicine.getName() %></td>
                    <td><%= medicine.getDescription() %></td>
                    <td><%= String.format("RM%.2f", medicine.getPrice()) %></td>
                    <td><%= stock %></td>
                </tr>
                <% } %>
            </table>
        </div>
    </div>
</body>
</html>