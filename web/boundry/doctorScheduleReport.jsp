<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.DoctorScheduleStats, adt.ListInterface, java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Doctor Schedule Frequency Report</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; background-color: #f6f8fb; }
        h2 { text-align: center; margin-bottom: 30px; }
        table { width: 80%; margin: 0 auto; border-collapse: collapse; background: white; }
        th, td { padding: 12px 20px; border: 1px solid #ddd; text-align: center; }
        th { background-color: #4CAF50; color: white; }
        tr:nth-child(even) { background-color: #f2f2f2; }
        .btn-back {
            display: block;
            width: 200px;
            margin: 30px auto;
            padding: 10px;
            text-align: center;
            background: #333;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
    </style>
</head>
<body>
    <h2>Doctor Schedule Frequency Report</h2>
    <%
ListInterface<DoctorScheduleStats> reportData = (ListInterface<DoctorScheduleStats>) request.getAttribute("reportData");
%>
<table>
    <thead>
        <tr>
            <th>Doctor Name</th>
            <th>Total Scheduled Days</th>
            <th>Available</th>
            <th>Unavailable</th>
            <th>Availability %</th>
        </tr>
    </thead>
    <tbody>
<%
if (reportData != null && reportData.size() > 0) {
    for (int i = 0; i < reportData.size(); i++) {
        DoctorScheduleStats stats = reportData.get(i);
        if (stats != null) {
%>
        <tr>
            <td><%= stats.getDoctorName() %></td>
            <td><%= stats.getTotalSlots() %></td>
            <td><%= stats.getAvailable() %></td>
            <td><%= stats.getUnavailable() %></td>
            <td><%= stats.getAvailabilityPercentage() %> %</td>
        </tr>
<%
        }
    }
} else {
%>
        <tr><td colspan="5">No data available.</td></tr>
<%
}
%>
    </tbody>
</table>

    <a href="DoctorManagement" class="btn-back">← Back to Doctor Panel</a>
</body>
</html>