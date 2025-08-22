<%-- 
    Document   : admin
    Created on : Aug 15, 2025, 3:16:45 PM
    Author     : hongj
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="entity.Patient" %>
<%
    String message = (String) request.getAttribute("message");
    Patient[] patients = (Patient[]) request.getAttribute("patients"); 
    Patient[] walkInQueue = (Patient[]) request.getAttribute("walkInQueue"); 
    Patient searchResult = (Patient) request.getAttribute("searchResult");

    Patient doctorTAN = (Patient) application.getAttribute("doctorTAN");
    Patient doctorTEOH = (Patient) application.getAttribute("doctorTEOH");
    entity.Patient[] walkInQueuePersist = (entity.Patient[]) application.getAttribute("walkInQueue");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Clinic — Patient Management</title>
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
.form-popup input { width: 100%; padding: 8px; margin-top: 4px; }
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
    transition: background 0.2s ease;
}
.back-btn:hover {
    background: #d9534f;
}
.dropdown {
    position: relative;
    display: inline-block;
}

.dropbtn {
    padding: 8px 14px;
    border: none;
    background: #1f6feb;
    color: white;
    border-radius: 6px;
    cursor: pointer;
}

.dropdown-content {
    display: none;
    position: absolute;
    background-color: white;
    min-width: 180px;
    box-shadow: 0 4px 8px rgba(0,0,0,0.15);
    z-index: 1;
    border-radius: 6px;
    overflow: hidden;
}

.dropdown-content form {
    margin: 0;
}

.dropdown-content button {
    background: none;
    color: black;
    padding: 10px 16px;
    border: none;
    width: 100%;
    text-align: left;
    cursor: pointer;
}

.dropdown-content button:hover {
    background-color: #f1f1f1;
}

.dropdown:hover .dropdown-content {
    display: block;
}

</style>
</head>
<body>
<button class="back-btn" onclick="window.location.href='home.jsp'">← Back to Home</button>
<div class="container">
    <h1>Clinic — Patient Management</h1>

    <% if (message != null) { %>
        <div class="message"><%= message %></div>
    <% } %>

    <!-- Action buttons -->
    <div style="text-align:center; margin-bottom:20px;">
        <button onclick="openForm('addForm')">Add Patient</button>
        <button onclick="openForm('searchForm')">Search Patient</button>
        <button onclick="openForm('serveForm')">Serve Next</button>
        <button onclick="openForm('clearQueueForm')">Clear Queue</button>
        <button onclick="openForm('reportForm')">Report</button>
    </div>

    <div style="display: flex; gap: 20px; align-items: flex-start;">
    <!-- Left: Walk-in Queue -->
        <div style="flex: 1;" class="queue">
        <h2>Current Walk-in Queue (Total: <%= walkInQueue == null ? 0 : walkInQueue.length %>)</h2>
        <% if (walkInQueue == null || walkInQueue.length == 0) { %>
            <p>No patients in queue.</p>
        <% } else { %>
            <table>
                <tr>
                    <th>No.</th>
                    <th>IC</th>
                    <th>Name</th>
                </tr>
                <% for (int i = 0; i < walkInQueue.length; i++) { %>
                    <tr>
                        <td><%= i + 1 %></td>
                        <td><%= walkInQueue[i].getIcNum() %></td>
                        <td><%= walkInQueue[i].getName() %></td>
                    </tr>
                <% } %>
            </table>
        <% } %>
    </div>


        <!-- Right: Currently Served Patient -->
        <div style="flex: 1;" class="queue">
            <h2>Currently Serving</h2>


            <h3>Doctor TAN</h3>
            <% if (doctorTAN != null) { %>
                <p><strong>IC:</strong> <%= doctorTAN.getIcNum() %></p>
                <p><strong>Name:</strong> <%= doctorTAN.getName() %></p>
                <form method="post" action="MaintainPatientServlet" onsubmit="return confirm('End service for Doctor TAN?');">
                    <input type="hidden" name="action" value="endServe">
                    <input type="hidden" name="doctor" value="tan">
                    <button type="submit" style="background:#e67e22;">End Serve</button>
                </form>
            <% } else { %>
                <p>Not serving any patient.</p>
            <% } %>

            <h3>Doctor TEOH</h3>
            <% if (doctorTEOH != null) { %>
                <p><strong>IC:</strong> <%= doctorTEOH.getIcNum() %></p>
                <p><strong>Name:</strong> <%= doctorTEOH.getName() %></p>
                <form method="post" action="MaintainPatientServlet" onsubmit="return confirm('End service for Doctor TAN?');">
                    <input type="hidden" name="action" value="endServe">
                    <input type="hidden" name="doctor" value="teoh">
                    <button type="submit" style="background:#e67e22;">End Serve</button>
                </form>
            <% } else { %>
                <p>Not serving any patient.</p>
            <% } %>
        </div>

    </div>
</div>

<div class="overlay" id="overlay"></div>
<div class="form-popup" id="addForm">
    <h2>Add Patient</h2>
    <form method="post" action="MaintainPatientServlet">
        <input type="hidden" name="action" value="add">

        <label>IC</label>
        <input type="text" name="ic" required>

        <label>Name</label>
        <input type="text" name="name" required>

        <label>Gender</label>
        <select name="gender" required>
            <option value="" disabled selected>Select gender</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
        </select>

        <label>Date of Birth</label>
        <input type="date" name="dob" required>

        <label>Phone</label>
        <input type="text" name="phone">

        <label>Email</label>
        <input type="email" name="email">

        <label>Address</label>
        <input type="text" name="address">

        <button type="submit">Add</button>
        <button type="button" class="close-btn" onclick="closeForm()">Cancel</button>
    </form>
</div>

<div class="form-popup" id="searchForm">
    <h2>Search Patient</h2>
    <form method="post" action="MaintainPatientServlet">
        <input type="hidden" name="action" value="search">
        <label>IC</label>
        <input type="text" name="icSearch" required>
        <button type="submit">Search</button>
        <button type="button" class="close-btn" onclick="closeForm()">Cancel</button>
    </form>
</div>

<div class="form-popup" id="resultForm">
    <% if (searchResult != null) { %>
        <h2>Patient Found</h2>
        <p>
            <strong>IC:</strong> <%= searchResult.getIcNum() %><br>
            <strong>Name:</strong> <%= searchResult.getName() %><br>
            <strong>Gender:</strong> <%= searchResult.getGender() %><br>
            <strong>Date of Birth:</strong> <%= searchResult.getDateOfBirth() != null ? searchResult.getDateOfBirth().toString() : "N/A" %><br>
            <strong>Phone:</strong> <%= searchResult.getPhoneNum() %><br>
            <strong>Email:</strong> <%= searchResult.getEmail() %><br>
            <strong>Address:</strong> <%= searchResult.getAddress() %>
        </p>
        <form method="post" action="MaintainPatientServlet" style="display:inline;">
            <input type="hidden" name="action" value="addToQueue">
            <input type="hidden" name="ic" value="<%= searchResult.getIcNum() %>">
            <button type="submit">Add to Queue</button>
        </form>
        <form method="post" action="MaintainPatientServlet" style="display:inline;">
            <input type="hidden" name="action" value="updateForm">
            <input type="hidden" name="ic" value="<%= searchResult.getIcNum() %>">
            <button type="button" onclick="openForm('updateForm')">Update</button>
        </form>
        <form method="post" action="MaintainPatientServlet" style="display:inline;"
              onsubmit="return confirm('Delete this patient?');">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="ic" value="<%= searchResult.getIcNum() %>">
            <button type="submit" style="background:#e74c3c;">Delete</button>
        </form>
        <button type="button" class="close-btn" onclick="closeForm()">Cancel</button>
    <% } %>
</div>

<div class="form-popup" id="updateForm">
    <% if (searchResult != null) { %>
        <h2>Update Patient</h2>
        <form method="post" action="MaintainPatientServlet">
            <input type="hidden" name="action" value="update">
            <label>IC</label>
            <input type="text" name="ic" value="<%= searchResult.getIcNum() %>" readonly>

            <label>Name</label>
            <input type="text" name="name" value="<%= searchResult.getName() %>" required>

            <label>Gender</label>
            <select name="gender" required>
                <option value="Male" <%= "Male".equalsIgnoreCase(searchResult.getGender()) ? "selected" : "" %>>Male</option>
                <option value="Female" <%= "Female".equalsIgnoreCase(searchResult.getGender()) ? "selected" : "" %>>Female</option>
            </select>

            <label>Date of Birth</label>
            <input type="date" name="dob" value="<%= searchResult.getDateOfBirth() != null ? searchResult.getDateOfBirth().toString() : "" %>" required>

            <label>Phone</label>
            <input type="text" name="phone" value="<%= searchResult.getPhoneNum() %>">

            <label>Email</label>
            <input type="email" name="email" value="<%= searchResult.getEmail() %>">

            <label>Address</label>
            <input type="text" name="address" value="<%= searchResult.getAddress() %>">

            <button type="submit">Update</button>
            <button type="button" class="close-btn" onclick="closeForm()">Cancel</button>
        </form>
    <% } %>
</div>


<div class="form-popup" id="serveForm">
    <h2>Serve Next</h2>
    <form id="serveDoctorForm" method="post" action="MaintainPatientServlet">
        <input type="hidden" name="action" value="serve">
        <input type="hidden" name="doctor" id="doctorField">

        <label>Enter Queue Number to Serve:</label>
        <input type="number" name="queueNumber" min="1" required>

        <div style="margin-top: 12px;">
            <button type="button" onclick="submitServeForm('tan')">Doctor TAN</button>
            <button type="button" onclick="submitServeForm('teoh')">Doctor TEOH</button>
            <button type="button" class="close-btn" onclick="closeForm()">Cancel</button>
        </div>
    </form>
</div>

<div class="form-popup" id="reportForm">
    <h2>Report Generation</h2>
    <form method="post" action="MaintainPatientServlet">
        <input type="hidden" name="action" value="dailyReport">
        <button type="submit">Daily Patient Report</button>
    </form>

    <form method="post" action="MaintainPatientServlet">
        <input type="hidden" name="action" value="queueTimeReport">
        <button type="submit">Queue Time Report</button>
    </form>

    <button type="button" class="close-btn" onclick="closeForm()">Cancel</button>
</div>

<div class="form-popup" id="clearQueueForm">
    <h2>Clear Queue</h2>
    
    <form method="post" action="MaintainPatientServlet" onsubmit="return confirm('Are you sure you want to clear the entire queue?');">
        <input type="hidden" name="action" value="clear">
        <button type="submit" style="background:#e74c3c; width: 100%; margin-bottom: 10px;">Clear All Patients in Queue</button>
    </form>

    <button onclick="openForm('clearSpecificForm')" style="width: 100%;">Clear Specific Patient</button>

    <button type="button" class="close-btn" onclick="closeForm()">Cancel</button>
</div>

<div class="form-popup" id="clearSpecificForm">
    <h2>Clear Specific Patient</h2>
    <form method="post" action="MaintainPatientServlet" onsubmit="return confirm('Are you sure you want to remove this patient from the queue?');">
        <input type="hidden" name="action" value="clearSpecific">

        <label>Queue Number</label>
        <input type="number" name="queueNumber" min="1" required>

        <button type="submit" style="background:#f39c12;">Remove Patient</button>
        <button type="button" class="close-btn" onclick="closeForm()">Cancel</button>
    </form>
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

<% if (searchResult != null) { %>
    window.onload = function() {
        closeForm();
        openForm('resultForm');
    }
<% } %>
    
function submitServeForm(doctor) {
    document.getElementById('doctorField').value = doctor;
    document.getElementById('serveDoctorForm').submit();
}
</script>
</body>
</html>