<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="entity.Consultation, java.time.format.DateTimeFormatter"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Doctor Consultation Schedule</title>
<style>
    body { font-family: Arial, sans-serif; background: #f6f8fb; padding: 20px; }
    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
    th, td { padding: 8px; border: 1px solid #ddd; text-align: left; }
    th { background-color: #f2f2f2; }
</style>
</head>
<body>
<h1>Doctor Consultation Schedule</h1>
<a href="MaintainConsultationServlet">← Back to Consultations</a>

<%
    Consultation[] doctorConsultations = (Consultation[]) request.getAttribute("doctorConsultations");
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>

<% if (doctorConsultations != null && doctorConsultations.length > 0) { %>
<table>
    <tr>
        <th>ID</th>
        <th>Patient</th>
        <th>Date & Time</th>
        <th>Status</th>
        <th>Next Appointment</th>
    </tr>
    <% for (Consultation con : doctorConsultations) { %>
    <tr>
        <td><%= con.getConsultationId() %></td>
        <td><%= con.getPatient() != null ? con.getPatient().getName() : "N/A" %></td>
        <td><%= con.getConsultationDateTime() != null ? con.getConsultationDateTime().format(dtf) : "N/A" %></td>
        <td><%= con.getStatus() != null ? con.getStatus() : "N/A" %></td>
        <td><%= con.getNextAppointment() != null ? con.getNextAppointment().format(dtf) : "N/A" %></td>
    </tr>
    <% } %>
</table>
<% } else { %>
<p>No consultations found for this doctor.</p>
<% } %>
</body>
</html>
