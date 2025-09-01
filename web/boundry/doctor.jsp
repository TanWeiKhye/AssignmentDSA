<%@page import="adt.TreeInterface, entity.Doctor, adt.AVLTree,adt.ArrayList,adt.ListInterface, entity.Schedule, java.util.Date, java.util.Calendar, java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
        AVLTree<Doctor> doctors = (AVLTree<Doctor>) request.getAttribute("doctors");
        String currentDoctorIc = request.getParameter("ic");
        Doctor currentDoctor = null;
        
        // Initialize date format
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        
        if (currentDoctorIc != null && !currentDoctorIc.isEmpty()) {
            currentDoctor = doctors.search(new Doctor(currentDoctorIc));
        }
        
        // If no doctor is selected, show the first one
        if (currentDoctor == null && doctors.size() > 0) {
            // We need to get the first doctor from the AVLTree
            // This is a workaround since AVLTree doesn't have a direct getFirst method
            for (Doctor doc : doctors) {
                currentDoctor = doc;
                currentDoctorIc = doc.getIc();
                break;
            }
        }
        
        String formattedDateOB = "";
        if (currentDoctor != null) {
            Date dateOfBirth = currentDoctor.getDateOB();
            formattedDateOB = outputFormat.format(dateOfBirth);
        }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Clinic Management System</title>

        <link rel="stylesheet" href="css/style.css" />
        <link rel="stylesheet" href="css/modern-normalize.css" />
        <link rel="stylesheet" href="css/util.css" />

        <link rel="stylesheet" href="css/components/doctor.css" />
        
        <style>
            /* Modal Styles */
            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                overflow: auto;
                background-color: rgba(0,0,0,0.4);
            }
            
            .modal-content {
                background-color: #fefefe;
                margin: 5% auto;
                padding: 20px;
                border: 1px solid #888;
                width: 80%;
                max-width: 600px;
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0,0,0,0.2);
            }
            
            .close {
                color: #aaa;
                float: right;
                font-size: 28px;
                font-weight: bold;
                cursor: pointer;
            }
            
            .close:hover {
                color: black;
            }
            
            .form-group {
                margin-bottom: 15px;
            }
            
            .form-group label {
                display: block;
                margin-bottom: 5px;
                font-weight: bold;
            }
            
            .form-group input, .form-group select, .form-group textarea {
                width: 100%;
                padding: 8px;
                border: 1px solid #ddd;
                border-radius: 4px;
                box-sizing: border-box;
            }
            
            .form-actions {
                display: flex;
                justify-content: flex-end;
                gap: 10px;
                margin-top: 20px;
            }
            
            .education-item {
                display: flex;
                gap: 10px;
                margin-bottom: 10px;
            }
            
            .education-item input {
                flex: 1;
            }
            
            .btn-remove {
                background: #ff4757;
                color: white;
                border: none;
                padding: 8px 12px;
                border-radius: 4px;
                cursor: pointer;
            }
            
            .btn-add {
                background: #2ed573;
                color: white;
                border: none;
                padding: 8px 12px;
                border-radius: 4px;
                cursor: pointer;
                margin-bottom: 15px;
            }
            
            .no-doctor-selected {
                text-align: center;
                padding: 40px;
                color: #666;
            }
        </style>
    </head>
    <body>
        <main>
            <header>
                <a href="<%= request.getContextPath()%>" class="back">&larr;</a>
                Doctor Management
            </header>
            <section class="doctor">
                <div class="panel_left">
                    <h2>Doctors</h2>
                    <div class="doctor_list">
                        <%
                            for (Doctor doctor : doctors) {
                                String doctorIc = doctor.getIc();
                        %>
                        <a href="DoctorManagement?ic=<%= doctorIc %>" class="doctor_spec <%= (doctorIc.equals(currentDoctorIc)) ? "active" : ""%>"><%= doctor.getName()%></a>
                        <%
                            }
                        %>
                        <a href="#" id="addDoctorBtn" class="doctor_spec">+ Add More</a>
                        <a href="#" id="reportBtn" class="doctor_spec">📄 Report</a>
                    </div>
                </div>

                <% if (currentDoctor != null) { %>
                <div class="panel_right">
                    <div class="doctor_card">
                        <div class="profile_header">
                            <div class="profile_header_info">
                                <h3><%= currentDoctor.getName() %></h3>
                                <h4>Cardiologist</h4>
                                <p><%= currentDoctor.getEmail()%></p>
                            </div>
                            <div class="profile_header_action">
                                <button class="btn">Edit Profile</button>
                                <form method="post" action="DoctorManagement" onsubmit="return confirm('Are you sure you want to delete this doctor?');" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="ic" value="<%= currentDoctor.getIc() %>">
                                    <button type="submit" class="btn btn-danger">Delete Doctor</button>
                                </form>
                                <button class="btn">View History</button>
                            </div>
                        </div>
                        <div class="profile_detail">
                            <h2>Personal Information</h2>
                            <div class="profile_detail_info">
                                <div class="profile_detail_info_item">
                                    <label for="dob">Date of Birth: </label>
                                    <input
                                        type="text"
                                        name="dob"
                                        id="dob"
                                        value="<%= formattedDateOB %>"
                                        disabled
                                        />
                                </div>
                                <div class="profile_detail_info_item">
                                    <label for="gender">Gender: </label>
                                    <select name="gender" id="gender" disabled>
                                        <option><%= currentDoctor.getGender()%></option>
                                    </select>
                                </div>
                                <div class="profile_detail_info_item">
                                    <label for="phone">Phone: </label>
                                    <input
                                        type="tel"
                                        name="phone"
                                        id="phone"
                                        value="+6<%= currentDoctor.getPhoneNumber()%>"
                                        disabled
                                        />
                                </div>
                            </div>

                            <h2>Education & Qualifications</h2>
                            <div class="profile_detail_edu">
                                <%
                                for (int i = 0; i < currentDoctor.getEdu().size(); i++) {
                                    out.print("<div class=\"profile_detail_edu_item\">" + currentDoctor.getEdu().get(i) + "</div>");
                                }
                                %>
                                
                            </div>
                        </div>
                    </div>

                    <div class="doctor_card_2_list">
                        <div class="doctor_card_2">
                            <div class="schedules">
                                <h2>Duty Schedules (Next 7 Days)</h2>

                                <div class="schedules_table">
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>Date</th>
                                                <th>Day</th>    
                                                <th>Shift</th>
                                                <th>Location</th>
                                                <th>Status</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                            ListInterface<Schedule> schedules = currentDoctor.getSchedules();

                                            Calendar calendar = Calendar.getInstance();
                                            for (int i = 0; i < 7; i++) {
                                                Date currentDate = calendar.getTime();
                                                String dateStr = outputFormat.format(currentDate);

                                                String status = "Unavailable";
                                                String location = "-";
                                                String shift = "Unavailable";

                                                for (int j = 0; j < schedules.size(); j++) {
                                                    Schedule s = schedules.get(j);
                                                    if (s.getDoctorIc().equals(currentDoctor.getIc()) && outputFormat.format(s.getDate()).equals(dateStr)) {
                                                        status = s.getStatus();
                                                        location = s.getLocation();
                                                        shift = "9:00 AM - 5:00 PM";
                                                        break;
                                                    }
                                                }

                                                String statusClass = status.equals("Available") ? "status-available" : "status-unavailable";
                                        %>
                                        <tr>
                                            <td><%= dateStr %></td>
                                            <td><%= dayFormat.format(currentDate) %></td>
                                            <td><%= shift %></td>
                                            <td><%= location %></td>
                                            <td class="<%= statusClass %>"><%= status %></td>
                                        </tr>
                                        <%
                                                calendar.add(Calendar.DAY_OF_YEAR, 1);
                                            }
                                        %>
                                        </tbody>
                                    </table>
                                </div>

                                <a href="">View Full Schedule</a>
                            </div>
                        </div>

                        <div class="doctor_card_2">
                            <div class="available">
                                <h2>Availability</h2>

                                <div class="availability-status">
                                    <p>
                                        <strong>Current Status:</strong>
                                        <span class="status-available">Available</span>
                                    </p>
                                    <p><strong>Next Available Slot:</strong> Today, 03:00 PM</p>
                                </div>
                                <div class="availability-actions">
                                    <!-- Show Make Available Button -->
                                    <button class="btn btn-success" onclick="openMakeAvailableModal()">Make Available</button>

                                    <!-- Show Enter Consultation Room only if today’s schedule is available -->
                                    <%
                                        String todayStr = outputFormat.format(new Date());
                                        boolean isAvailableToday = false;

                                        for (int j = 0; j < schedules.size(); j++) {
                                            Schedule s = schedules.get(j);
                                            if (s.getDoctorIc().equals(currentDoctor.getIc()) && s.getDate().equals(todayStr) && s.getStatus().equals("Available")) {
                                                isAvailableToday = true;
                                                break;
                                            }
                                        }

                                        if (isAvailableToday) {
                                    %>
                                        <button class="btn-info btn" onclick="openConsultationModal()">Enter Consultation Room</button>
                                    <% } %>
                                    
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <% } else { %>
                <div class="panel_right">
                    <div class="no-doctor-selected">
                        <h2>No Doctor Selected</h2>
                        <p>Please select a doctor from the list or add a new one.</p>
                    </div>
                </div>
                <% } %>
            </section>
            
            <!-- Add Doctor Modal -->
            <div id="addDoctorModal" class="modal">
                <div class="modal-content">
                    <span class="close">&times;</span>
                    <h2>Add New Doctor</h2>
                    <form id="addDoctorForm" action="DoctorManagement" method="POST">
                        <input type="hidden" name="action" value="add">
                        
                        <div class="form-group">
                            <label for="name">Full Name:</label>
                            <input type="text" id="name" name="name" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="ic">IC Number:</label>
                            <input type="text" id="ic" name="ic" pattern="[0-9]{6}-[0-9]{2}-[0-9]{4}" placeholder="e.g., 123456-78-9012" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="dob">Date of Birth:</label>
                            <input type="date" id="dob" name="dob" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="phone">Phone Number:</label>
                            <input type="tel" id="phone" name="phone" pattern="[0-9]{10,11}" placeholder="e.g., 0123456789" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="email">Email:</label>
                            <input type="email" id="email" name="email" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="gender">Gender:</label>
                            <select id="gender" name="gender" required>
                                <option value="">Select Gender</option>
                                <option value="Male">Male</option>
                                <option value="Female">Female</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label>Education & Qualifications:</label>
                            <div id="education-container">
                                <div class="education-item">
                                    <input type="text" name="education" placeholder="e.g., MBBS, University of Malaya" required>
                                    <button type="button" class="btn-remove" onclick="removeEducation(this)">Remove</button>
                                </div>
                            </div>
                            <button type="button" class="btn-add" onclick="addEducation()">Add Another Qualification</button>
                        </div>
                        
                        <div class="form-actions">
                            <button type="button" class="btn" onclick="closeModal()">Cancel</button>
                            <button type="submit" class="btn-success">Add Doctor</button>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Edit Doctor Modal -->
            <div id="editDoctorModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeEditModal()">&times;</span>
                    <h2>Edit Doctor Profile</h2>
                    <form id="editDoctorForm" action="DoctorManagement" method="POST">
                        <input type="hidden" name="action" value="edit">
                        <input type="hidden" name="originalIc" value="<%= currentDoctor.getIc() %>">

                        <div class="form-group">
                            <label>Full Name:</label>
                            <input type="text" name="name" value="<%= currentDoctor.getName() %>" required>
                        </div>

                        <div class="form-group">
                            <label>IC Number:</label>
                            <input type="text" name="ic" value="<%= currentDoctor.getIc() %>" disabled>
                        </div>

                        <div class="form-group">
                            <label>Date of Birth:</label>
                            <input type="date" name="dob" value="<%= formattedDateOB %>" required>
                        </div>

                        <div class="form-group">
                            <label>Phone:</label>
                            <input type="tel" name="phone" value="<%= currentDoctor.getPhoneNumber() %>" required>
                        </div>

                        <div class="form-group">
                            <label>Email:</label>
                            <input type="email" name="email" value="<%= currentDoctor.getEmail() %>" required>
                        </div>

                        <div class="form-group">
                            <label>Gender:</label>
                            <select name="gender" required>
                                <option value="Male" <%= currentDoctor.getGender().equals("Male") ? "selected" : "" %>>Male</option>
                                <option value="Female" <%= currentDoctor.getGender().equals("Female") ? "selected" : "" %>>Female</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Education & Qualifications:</label>
                            <div id="edit-education-container">
                                <%
                                    for (int i = 0; i < currentDoctor.getEdu().size(); i++) {
                                %>
                                    <div class="education-item">
                                        <input type="text" name="education" value="<%= currentDoctor.getEdu().get(i) %>" required>
                                        <button type="button" class="btn-remove" onclick="removeEducation(this)">Remove</button>
                                    </div>
                                <%
                                    }
                                %>
                            </div>
                            <button type="button" class="btn-add" onclick="addEducation()">Add Another Qualification</button>
                        </div>

                        <div class="form-actions">
                            <button type="button" class="btn" onclick="closeEditModal()">Cancel</button>
                            <button type="submit" class="btn-success">Save Changes</button>
                        </div>
                    </form>
                </div>
            </div>
            <!-- Report Modal -->
            <div id="reportModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeReportModal()">&times;</span>
                    <h2>Select Report Type</h2>
                    <div style="display: flex; justify-content: center; gap: 20px; margin-top: 20px;">
                        <button class="btn btn-success" onclick="generateReport(1)">Daily Availability Report</button>
                        <button class="btn btn-success" onclick="generateReport(2)">Doctor Schedule Report</button>
                    </div>
                    <div style="text-align: center; margin-top: 30px;">
                        <button class="btn" onclick="closeReportModal()">Cancel</button>
                    </div>
                </div>
            </div>
            <!-- Consultation Room Modal -->
            <div id="consultationModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeConsultationModal()">&times;</span>
                    <h2>Select Consultation Room</h2>
                    <div style="display: flex; justify-content: center; gap: 20px; margin-top: 20px;">
                        <button class="btn btn-success" onclick="enterRoom(1)">Consultation Room 1</button>
                        <button class="btn btn-success" onclick="enterRoom(2)">Consultation Room 2</button>
                    </div>
                    <div style="text-align: center; margin-top: 30px;">
                        <button class="btn" onclick="closeConsultationModal()">Cancel</button>
                    </div>
                </div>
            </div>
            <!-- Make Available Modal -->
            <div id="makeAvailableModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeMakeAvailableModal()">&times;</span>
                    <h2>Make Doctor Available</h2>
                    <form action="DoctorManagement" method="post">
                        <input type="hidden" name="action" value="makeAvailable">
                        <input type="hidden" name="ic" value="<%= currentDoctor.getIc() %>">

                        <div class="form-group">
                            <label for="scheduleDate">Select Date:</label>
                            <input type="date" name="scheduleDate" required>
                        </div>

                        <div class="form-group">
                            <label for="status">Availability:</label>
                            <select name="status" id="status" required onchange="toggleRoomField(this.value)">
                                <option value="">--Select--</option>
                                <option value="Available">Available</option>
                                <option value="Unavailable">Unavailable</option>
                            </select>
                        </div>

                        <div class="form-actions">
                            <button type="submit" class="btn-success">Confirm</button>
                            <button type="button" class="btn" onclick="closeMakeAvailableModal()">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
            <!-- Report 1 Modal -->
            <div id="report1Modal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeReport1Modal()">&times;</span>
                    <h2>Doctor Availability Report</h2>

                    <form method="get" action="DoctorManagement">
                        <div class="form-group">
                            <label for="reportDate">Select Date:</label>
                            <input type="date" id="reportDate" name="reportDate" required>
                        </div>
                        <input type="hidden" name="action" value="report1">
                        <div class="form-actions">
                            <button type="submit" class="btn-success">Generate Report</button>
                            <button type="button" class="btn" onclick="closeReport1Modal()">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>

        </main>
        
        <script>
            // Get modal elements
            var modal = document.getElementById("addDoctorModal");
            var btn = document.getElementById("addDoctorBtn");
            var span = document.getElementsByClassName("close")[0];
            
            // Open modal when clicking Add More
            btn.onclick = function(event) {
                event.preventDefault();
                modal.style.display = "block";
            }
            
            // Close modal when clicking X
            span.onclick = function() {
                modal.style.display = "none";
            }
            
            // Close modal when clicking outside
            window.onclick = function(event) {
                if (event.target == modal) {
                    modal.style.display = "none";
                }
            }
            
            // Function to close modal
            function closeModal() {
                modal.style.display = "none";
            }
            
            // Function to add education field
            function addEducation() {
                var container = document.getElementById("education-container");
                var newItem = document.createElement("div");
                newItem.className = "education-item";
                newItem.innerHTML = `
                    <input type="text" name="education" placeholder="e.g., MBBS, University of Malaya" required>
                    <button type="button" class="btn-remove" onclick="removeEducation(this)">Remove</button>
                `;
                container.appendChild(newItem);
            }
            
            // Function to remove education field
            function removeEducation(button) {
                var container = document.getElementById("education-container");
                if (container.children.length > 1) {
                    button.parentElement.remove();
                }
            }
            
            // Form validation
            document.getElementById("addDoctorForm").addEventListener("submit", function(event) {
                // Basic validation is handled by HTML5 required attribute
                // Additional validation can be added here if needed
            });
            // Edit Profile Modal
            const editModal = document.getElementById("editDoctorModal");
            document.querySelector(".profile_header_action .btn").onclick = function() {
                editModal.style.display = "block";
            };

            function closeEditModal() {
                editModal.style.display = "none";
            }

            // Delete confirmation
            function deleteDoctor(ic) {
                if (confirm("Are you sure you want to delete this doctor?")) {
                    window.location.href = `DoctorManagement?action=delete&ic=${ic}`;
                }
            }
            // Report modal elements
            const reportModal = document.getElementById("reportModal");
            const reportBtn = document.getElementById("reportBtn");

            reportBtn.onclick = function(event) {
                event.preventDefault();
                reportModal.style.display = "block";
            };

            function closeReportModal() {
                reportModal.style.display = "none";
            }

            // Close report modal when clicking outside
            window.addEventListener("click", function(event) {
                if (event.target === reportModal) {
                    closeReportModal();
                }
            });

            // Placeholder function for report generation
            function generateReport(type) {
                alert("Generating Report " + type);
                // You can redirect to a servlet or JSP page here:
                // window.location.href = "ReportServlet?type=" + type;
            }
            // Consultation modal open/close
            const consultationModal = document.getElementById("consultationModal");

            function openConsultationModal() {
                consultationModal.style.display = "block";
            }

            function closeConsultationModal() {
                consultationModal.style.display = "none";
            }

            // Handle room selection
            function enterRoom(roomNumber) {
                const statusElement = document.querySelector(".availability-status span");
                if (statusElement) {
                    statusElement.className = "status-room";
                    statusElement.textContent = "In Consultation (Room " + roomNumber + ")";
                }
                closeConsultationModal();
            }
            const makeAvailableModal = document.getElementById("makeAvailableModal");

            function openMakeAvailableModal() {
                makeAvailableModal.style.display = "block";
            }

            function closeMakeAvailableModal() {
                makeAvailableModal.style.display = "none";
            }

            window.addEventListener("click", function(event) {
                if (event.target === makeAvailableModal) {
                    closeMakeAvailableModal();
                }
            });
            function generateReport(type) {
                if (type === 1) {
                    closeReportModal();
                    document.getElementById("report1Modal").style.display = "block";
                } else if (type === 2) {
                    window.location.href = "DoctorManagement?action=report2";
                }
            }

        function closeReport1Modal() {
            document.getElementById("report1Modal").style.display = "none";
        }

        window.onclick = function(event) {
            if (event.target === document.getElementById("report1Modal")) {
                closeReport1Modal();
            }
        }

        </script>
    </body>
</html>