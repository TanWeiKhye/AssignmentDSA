<%-- 
    Document   : appointmentSummary
    Created on : Aug 15, 2025, 3:04:48 PM
    Author     : hongj
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="entity.Patient" %>
<%
    Patient p = (Patient) request.getAttribute("newPatient");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Appointment Summary</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f9f9f9;
            padding: 40px;
            display: flex;
            justify-content: center;
        }

        .summary-container {
            background: #ffffff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            max-width: 500px;
            width: 100%;
        }

        .summary-container h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #2c3e50;
        }

        .summary-container p {
            font-size: 16px;
            margin: 8px 0;
            color: #34495e;
        }

        .status {
            font-weight: bold;
            color: #27ae60;
            margin-top: 20px;
        }

        .back-button {
            display: block;
            text-align: center;
            margin-top: 30px;
            background-color: #3498db;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 6px;
        }

        .back-button:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
    <div class="summary-container">
        <h2>Appointment Summary</h2>

        <% if (p != null) { %>
            <p><strong>IC:</strong> <%= p.getIcNum() %></p>
            <p><strong>Name:</strong> <%= p.getName() %></p>
            <p><strong>Gender:</strong> <%= p.getGender() != null ? p.getGender() : "N/A" %></p>
            <p><strong>Date of Birth:</strong> <%= p.getDateOfBirth() != null ? p.getDateOfBirth() : "N/A" %></p>
            <p><strong>Phone:</strong> <%= p.getPhoneNum() %></p>
            <p><strong>Email:</strong> <%= p.getEmail() %></p>
            <p><strong>Address:</strong> <%= p.getAddress() %></p>
            <p class="status">Added to queue successfully!</p>
        <% } else { %>
            <p>No appointment data found.</p>
        <% } %>

        <a href="home.jsp" class="back-button">Back to Home</a>
    </div>
</body>
</html>
