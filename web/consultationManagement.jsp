<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="entity.Consultation" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Clinic — Consultation Management</title>
<style>
    body { font-family: Arial, sans-serif; background: #f6f8fb; margin: 0; padding: 20px; }
    .container { max-width: 1100px; margin: 0 auto; }
    h1 { text-align: center; color: #1f6feb; }
    .message { margin: 10px 0; padding: 10px; background: #fffbea; border-left: 4px solid #ffd24d; border-radius: 4px; }
    button { padding: 8px 14px; border: none; background: #1f6feb; color: #fff; border-radius: 6px; cursor: pointer; }
    button:hover { opacity: 0.95; }
    .queue { margin-top: 20px; background: #fff; padding: 14px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
    table { width: 100%; border-collapse: collapse; margin-top: 8px; }
    th, td { padding: 8px; border-bottom: 1px solid #eee; text-align: left; }
    .form-popup {
        display: none;
        position: fixed;
        top: 50%; left: 50%;
        transform: translate(-50%, -50%);
        background: #fff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.3);
        z-index: 999;
        max-width: 400px;
        width: 100%;
    }
    .form-popup label { display: block; margin-top: 8px; }
    .form-popup input, .form-popup select { width: 100%; padding: 8px; margin-top: 4px; }
    .form-popup .close-btn {
        background: #e74c3c;
        margin-top: 12px;
    }
    .overlay {
        display: none;
        position: fixed;
        top: 0; left: 0;
        width: 100%; height: 100%;
        background: rgba(0,0,0,0.4);
        z-index: 998;
    }
    .back-btn {
        position: fixed;
        top: 15px;
        left: 15px;
        background: #f28b82;
        color: white;
        padding: 8px 14px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-size: 14px;
        box-shadow: 0 2px 6px rgba(0,0,0,0.15);
    }
    .status-scheduled { color: #2196F3; }
    .status-inprogress { color: #FF9800; }
    .status-completed { color: #4CAF50; }
    .status-cancelled { color: #F44336; }
    .section { margin-bottom: 20px; padding: 15px; background: #fff; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
</style>
</head>
<body>
<button class="back-btn" onclick="window.location.href='home.jsp'">← Back to Home</button>
<div class="container">
    <h1>Clinic — Consultation Management</h1>
    <%-- Add this debug section at the top of your JSP --%>
<div style="background: #f5f5f5; padding: 10px; margin: 10px 0; border: 1px solid #ddd;">
    <strong>Debug Information:</strong><br>
    <%
    java.util.Enumeration<String> params = request.getParameterNames();
    while (params.hasMoreElements()) {
        String paramName = params.nextElement();
        String paramValue = request.getParameter(paramName);
        out.println(paramName + " = " + paramValue + "<br>");
    }
    %>
</div>

    <% String message = (String) request.getAttribute("message"); %>
    <% if (message != null) { %>
        <div class="message"><%= message %></div>
    <% } %>

    <!-- Action buttons -->
    <div style="text-align:center; margin-bottom:20px;">
        <button onclick="openForm('addForm')">Schedule Consultation</button>
        <button onclick="openForm('actionForm')">Consultation Actions</button>
        <button onclick="openForm('followupForm')">Schedule Follow-up</button>
        <button onclick="openForm('rescheduleForm')">Reschedule</button>
        <button onclick="openForm('reportForm')">Reports</button>
    </div>

    <!-- Scheduled Consultations -->
    <div class="section">
        <h2>Scheduled Consultations</h2>
        <%
            Consultation[] scheduledConsultations = (Consultation[]) request.getAttribute("scheduledConsultations");
            if (scheduledConsultations != null && scheduledConsultations.length > 0) {
        %>
        <table>
            <tr>
                <th>ID</th><th>Patient</th><th>Doctor</th><th>Date & Time</th><th>Status</th>
            </tr>
            <% for (Consultation con : scheduledConsultations) { %>
            <tr>
                <td><%= con.getConsultationId() %></td>
                <td><%= con.getPatient() != null ? con.getPatient().getName() : "N/A" %></td>
                <td><%= con.getDoctor() != null ? con.getDoctor().getName() : "N/A" %></td>
                <td><%= con.getConsultationDateTime() != null ? con.getConsultationDateTime().toString() : "N/A" %></td>
                <td class="status-scheduled"><%= con.getStatus() != null ? con.getStatus() : "N/A" %></td>
            </tr>
            <% } %>
        </table>
        <% } else { %>
            <p>No scheduled consultations.</p>
        <% } %>
    </div>

    <!-- In Progress Consultations -->
    <div class="section">
        <h2>In Progress Consultations</h2>
        <%
            Consultation[] inProgressConsultations = (Consultation[]) request.getAttribute("inProgressConsultations");
            if (inProgressConsultations != null && inProgressConsultations.length > 0) {
        %>
        <table>
            <tr>
                <th>ID</th><th>Patient</th><th>Doctor</th><th>Date & Time</th><th>Status</th>
            </tr>
            <% for (Consultation con : inProgressConsultations) { %>
            <tr>
                <td><%= con.getConsultationId() %></td>
                <td><%= con.getPatient() != null ? con.getPatient().getName() : "N/A" %></td>
                <td><%= con.getDoctor() != null ? con.getDoctor().getName() : "N/A" %></td>
                <td><%= con.getConsultationDateTime() != null ? con.getConsultationDateTime().toString() : "N/A" %></td>
                <td class="status-inprogress"><%= con.getStatus() != null ? con.getStatus() : "N/A" %></td>
            </tr>
            <% } %>
        </table>
        <% } else { %>
            <p>No consultations in progress.</p>
        <% } %>
    </div>

    <!-- Completed Consultations -->
    <div class="section">
        <h2>Completed Consultations</h2>
        <%
            Consultation[] completedConsultations = (Consultation[]) request.getAttribute("completedConsultations");
            if (completedConsultations != null && completedConsultations.length > 0) {
        %>
        <table>
            <tr>
                <th>ID</th><th>Patient</th><th>Doctor</th><th>Date & Time</th><th>Next Appointment</th><th>Status</th>
            </tr>
            <% for (Consultation con : completedConsultations) { %>
            <tr>
                <td><%= con.getConsultationId() %></td>
                <td><%= con.getPatient() != null ? con.getPatient().getName() : "N/A" %></td>
                <td><%= con.getDoctor() != null ? con.getDoctor().getName() : "N/A" %></td>
                <td><%= con.getConsultationDateTime() != null ? con.getConsultationDateTime().toString() : "N/A" %></td>
                <td><%= con.getNextAppointment() != null ? con.getNextAppointment().toString() : "N/A" %></td>
                <td class="status-completed"><%= con.getStatus() != null ? con.getStatus() : "N/A" %></td>
            </tr>
            <% } %>
        </table>
        <% } else { %>
            <p>No completed consultations.</p>
        <% } %>
    </div>

    <!-- All Consultations -->
    <div class="section">
        <h2>All Consultations</h2>
        <%
            Consultation[] allConsultations = (Consultation[]) request.getAttribute("consultations");
            if (allConsultations != null && allConsultations.length > 0) {
        %>
        <table>
            <tr>
                <th>ID</th><th>Patient</th><th>Doctor</th><th>Date & Time</th><th>Status</th>
            </tr>
            <% for (Consultation con : allConsultations) { 
                String statusClass = "";
                if ("Scheduled".equals(con.getStatus())) statusClass = "status-scheduled";
                else if ("InProgress".equals(con.getStatus())) statusClass = "status-inprogress";
                else if ("Completed".equals(con.getStatus())) statusClass = "status-completed";
                else if ("Cancelled".equals(con.getStatus())) statusClass = "status-cancelled";
            %>
            <tr>
                <td><%= con.getConsultationId() %></td>
                <td><%= con.getPatient() != null ? con.getPatient().getName() : "N/A" %></td>
                <td><%= con.getDoctor() != null ? con.getDoctor().getName() : "N/A" %></td>
                <td><%= con.getConsultationDateTime() != null ? con.getConsultationDateTime().toString() : "N/A" %></td>
                <td class="<%= statusClass %>"><%= con.getStatus() != null ? con.getStatus() : "N/A" %></td>
            </tr>
            <% } %>
        </table>
        <% } else { %>
            <p>No consultations available.</p>
        <% } %>
    </div>
</div>

<!-- Popup Forms -->
<div class="overlay" id="overlay"></div>

<!-- Schedule New Consultation Form -->
<div class="form-popup" id="addForm">
    <h2>Schedule New Consultation</h2>
    <form method="post" action="MaintainConsultationServlet">
        <input type="hidden" name="action" value="createConsultation">
        
        <label>Patient IC:</label>
        <input type="text" name="patientIc" required>
        
        <label>Doctor IC:</label>
        <input type="text" name="doctorIc" required>
        
        <label>Date & Time:</label>
        <input type="datetime-local" name="consultationDateTime" required>
        
        <button type="submit">Schedule Consultation</button>
        <button type="button" class="close-btn" onclick="closeForm()">Cancel</button>
    </form>
</div>

<!-- Consultation Actions Form -->
<div class="form-popup" id="actionForm">
    <h2>Consultation Actions</h2>
    
    <!-- Start Consultation -->
    <form method="post" action="MaintainConsultationServlet" style="margin-bottom: 15px;">
        <input type="hidden" name="action" value="startConsultation">
        <label>Consultation ID to Start:</label>
        <input type="text" name="consultationId" required>
        <button type="submit">Start Consultation</button>
    </form>
    
    <!-- Complete Consultation -->
    <form method="post" action="MaintainConsultationServlet" style="margin-bottom: 15px;">
        <input type="hidden" name="action" value="completeConsultation">
        <label>Consultation ID to Complete:</label>
        <input type="text" name="consultationId" required>
        <button type="submit">Complete Consultation</button>
    </form>
    
    <!-- Cancel Consultation -->
    <form method="post" action="MaintainConsultationServlet">
        <input type="hidden" name="action" value="cancelConsultation">
        <label>Consultation ID to Cancel:</label>
        <input type="text" name="consultationId" required>
        <button type="submit" style="background-color: #f44336;">Cancel Consultation</button>
    </form>
    
    <button type="button" class="close-btn" onclick="closeForm()" style="margin-top: 15px;">Cancel</button>
</div>

<!-- Schedule Follow-up Form -->
<div class="form-popup" id="followupForm">
    <h2>Schedule Follow-up Appointment</h2>
    <form method="post" action="MaintainConsultationServlet">
        <input type="hidden" name="action" value="scheduleFollowUp">
        
        <label>Consultation ID for Follow-up:</label>
        <input type="text" name="consultationId" required>
        
        <label>Follow-up Date & Time:</label>
        <input type="datetime-local" name="nextAppointment" required>
        
        <button type="submit">Schedule Follow-up</button>
        <button type="button" class="close-btn" onclick="closeForm()">Cancel</button>
    </form>
</div>

<!-- Reschedule Consultation Form -->
<div class="form-popup" id="rescheduleForm">
    <h2>Reschedule Consultation</h2>
    <form method="post" action="MaintainConsultationServlet">
        <input type="hidden" name="action" value="rescheduleConsultation">
        
        <label>Consultation ID to Reschedule:</label>
        <input type="text" name="consultationId" required>
        
        <label>New Date & Time:</label>
        <input type="datetime-local" name="newDateTime" required>
        
        <button type="submit">Reschedule Consultation</button>
        <button type="button" class="close-btn" onclick="closeForm()">Cancel</button>
    </form>
</div>

<!-- Delete Consultation Form -->
<div class="form-popup" id="deleteForm">
    <h2>Delete Consultation</h2>
    <form method="post" action="MaintainConsultationServlet" 
          onsubmit="return confirm('Are you sure you want to delete this consultation?');">
        <input type="hidden" name="action" value="deleteConsultation">
        
        <label>Consultation ID:</label>
        <input type="text" name="consultationId" required>
        
        <button type="submit" style="background-color: #f44336;">Delete Consultation</button>
        <button type="button" class="close-btn" onclick="closeForm()">Cancel</button>
    </form>
</div>

<!-- Reports Form -->
<div class="form-popup" id="reportForm">
    <h2>Generate Reports</h2>
    
    <form method="get" action="MaintainConsultationServlet" style="margin-bottom: 10px;">
        <input type="hidden" name="action" value="viewPatientHistory">
        <label>Patient IC for History:</label>
        <input type="text" name="patientIc" required>
        <button type="submit">Patient Consultation History</button>
    </form>
    
    <form method="get" action="MaintainConsultationServlet">
        <input type="hidden" name="action" value="viewDoctorSchedule">
        <label>Doctor IC for Schedule:</label>
        <input type="text" name="doctorIc" required>
        <button type="submit">Doctor Consultation Schedule</button>
    </form>
    
    <button type="button" class="close-btn" onclick="closeForm()" style="margin-top: 15px;">Cancel</button>
</div>

<script>
function openForm(id) {
    document.getElementById(id).style.display = 'block';
    document.getElementById('overlay').style.display = 'block';
}
function closeForm() {
    document.querySelectorAll('.form-popup').forEach(f => f.style.display = 'none');
    document.getElementById('overlay').style.display = 'none';
}

// Add button to open delete form
function openDeleteForm() {
    openForm('deleteForm');
}
</script>
</body>
</html>