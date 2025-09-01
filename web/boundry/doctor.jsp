<%@page import="adt.TreeInterface, entity.Doctor, adt.AVLTree, entity.Schedule, java.util.Date, java.util.Calendar, java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
        AVLTree<Doctor> doctors = (AVLTree<Doctor>) request.getAttribute("doctors");
        String currentDoctorIc = request.getParameter("ic");
        Doctor currentDoctor = doctors.search(new Doctor(currentDoctorIc));
        
        Date dateOfBirth = currentDoctor.getDateOB();
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateOB = outputFormat.format(dateOfBirth);
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
                        <a href="" class="doctor_spec">+ Add More</a>
                    </div>
                </div>

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
                                
                                %>
                                <div class="profile_detail_edu_item"><%= currentDoctor.getEdu().get(i) %></div>
                                <%
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
                                                Calendar calander = Calendar.getInstance();
                                            for (int i = 0; i < 7; i++) {
                                            String statusClass = "";
                                            if (currentDoctor.getSchedules().get(0).getStatus().equals("Available")) {
                                                statusClass = "status-available";
                                                } else if (currentDoctor.getSchedules().get(0).getStatus().equals("Booked")) {
                                                statusClass = "status-unavailable";
                                                }
                                                
                                                Date currentDate = calander.getTime();
                                            
                                            %>
                                            <tr>
                                                <td><%= outputFormat.format(currentDate) %></td>
                                                <td><%= new SimpleDateFormat("EEEE").format(currentDate) %></td>
                                                <td></td>
                                                <td><%= currentDoctor.getSchedules().get(0).getLocation() %></td>
                                                <td class="<%= statusClass%>"><%= currentDoctor.getSchedules().get(0).getStatus() %></td>
                                            </tr>
                                            <%
                                                calander.add(Calendar.DAY_OF_YEAR, 1);
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
                                    <button class="btn-success btn">Mark Available</button>
                                    <button class="btn-warning btn">Mark On Break</button>
                                    <button class="btn-danger btn">Mark Unavailable</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </main>
    </body>
</html>

