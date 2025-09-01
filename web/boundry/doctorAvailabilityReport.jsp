<%@ page import="java.util.*, java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="adt.ListInterface" %>
<html>
<head>
    <title>Doctor Availability Report</title>
    <style>
        body { font-family: Arial; padding: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }
        th { background-color: #f4f4f4; }
        .available { color: green; font-weight: bold; }
        .unavailable { color: red; font-weight: bold; }
    </style>
</head>
<body>
    <h1>Doctor Availability Report</h1>
    <p><strong>Date:</strong> <%= request.getAttribute("reportDate") %></p>

    <table>
        <tr>
            <th>Doctor Name</th>
            <th>Status</th>
        </tr>
        <%
            ListInterface<String> doctorNames = (ListInterface<String>) request.getAttribute("doctorNames");
            ListInterface<String> doctorStatuses = (ListInterface<String>) request.getAttribute("doctorStatuses");

            if (doctorNames != null && doctorStatuses != null) {
                for (int i = 0; i < doctorNames.size(); i++) {
                    String name = doctorNames.get(i);
                    String status = doctorStatuses.get(i);
        %>
        <tr>
            <td><%= name %></td>
            <td class="<%= "Available".equalsIgnoreCase(status) ? "available" : "unavailable" %>">
                <%= status %>
            </td>
        </tr>
        <%
                }
            }
        %>
    </table>

    <p><strong>Total Available:</strong> <%= request.getAttribute("availableCount") %></p>
    <p><strong>Total Unavailable:</strong> <%= request.getAttribute("unavailableCount") %></p>

    <a href="DoctorManagement" style="margin-top: 20px; display: inline-block;">← Back to Doctor Management</a>
</body>
</html>