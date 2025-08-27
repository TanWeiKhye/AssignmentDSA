<%-- 
    Document   : dailyReport
    Created on : Aug 19, 2025, 11:41:33 PM
    Author     : hongj
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="adt.ArrayList" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    ArrayList<String> records = (ArrayList<String>) request.getAttribute("dailyReport");
    int totalPatients = (records != null) ? records.size() : 0;

    String reportDateStr = request.getParameter("reportDate");
    LocalDate reportDate = (reportDateStr != null && !reportDateStr.isEmpty()) 
                           ? LocalDate.parse(reportDateStr)
                           : LocalDate.now();

    String formattedDate = reportDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

    // Time-based statistics
    int[] hourlyCounts = new int[24];
    int morning = 0, afternoon = 0, evening = 0;
    String firstPatientTime = null, lastPatientTime = null;

    if (records != null && !records.isEmpty()) {
        for (int i = 0; i < records.size(); i++) {
            String record = records.get(i);
            String timePart = record.replaceAll(".*\\[(\\d{2}):(\\d{2}).*\\].*", "$1:$2");
            int hour = Integer.parseInt(timePart.split(":")[0]);
            int minute = Integer.parseInt(timePart.split(":")[1]);

            hourlyCounts[hour]++;
            
            if (hour >= 6 && hour < 12) morning++;
            else if (hour >= 12 && hour < 18) afternoon++;
            else evening++;

            if (i == 0) firstPatientTime = timePart;
            if (i == records.size() - 1) lastPatientTime = timePart;
        }
    }

    // Find peak hour
    int peakHour = 0;
    for (int i = 1; i < 24; i++) {
        if (hourlyCounts[i] > hourlyCounts[peakHour]) {
            peakHour = i;
        }
    }
    String peakHourLabel = String.format("%02d:00 - %02d:00", peakHour, peakHour + 1);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>📋 Daily Patient Report</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f4f6f9;
            margin: 40px;
        }

        h1 {
            text-align: center;
            color: #2c3e50;
            margin-bottom: 30px;
        }

        .report-container {
            background-color: #fff;
            padding: 30px 40px;
            max-width: 900px;
            margin: auto;
            border-radius: 10px;
            box-shadow: 0 6px 20px rgba(0,0,0,0.1);
        }

        .section-title {
            color: #1a1a1a;
            font-size: 20px;
            border-bottom: 2px solid #ccc;
            padding-bottom: 6px;
            margin-top: 30px;
            margin-bottom: 15px;
        }

        .stat {
            font-size: 16px;
            color: #34495e;
            margin: 5px 0;
        }

        .report-date {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 10px;
            color: #2d3e50;
        }

        .form-section {
            text-align: center;
            margin-bottom: 30px;
        }

        input[type="date"] {
            padding: 10px;
            font-size: 16px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }

        button, .back-btn {
            padding: 10px 20px;
            background-color: #2980b9;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            margin-left: 10px;
            cursor: pointer;
        }

        button:hover, .back-btn:hover {
            background-color: #1c5980;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        th, td {
            padding: 12px 15px;
            border-bottom: 1px solid #ddd;
            text-align: left;
            font-size: 15px;
        }

        th {
            background-color: #f2f2f2;
            color: #333;
        }

        .no-data {
            color: #888;
            font-style: italic;
            text-align: center;
            margin-top: 20px;
        }

        .top-bar {
            margin-bottom: 20px;
        }

        .top-bar form {
            display: inline;
        }
    </style>
</head>
<body>
    <div class="report-container">
        <div class="top-bar">
            <form action="MaintainPatientServlet" method="get"> 
                <button class="back-btn">← Back</button> 
            </form>
        </div>

        <h1>📋 Daily Patient Report</h1>

        <div class="form-section">
            <form method="get" action="MaintainPatientServlet">
                <input type="hidden" name="action" value="dailyReport">
                <input type="date" name="reportDate" value="<%= reportDate.toString() %>" required>
                <button type="submit">View Report</button>
            </form>
        </div>

        <div class="report-date">Report for: <%= formattedDate %></div>

        <div class="section-title">Summary Statistics</div>
        <div class="stat">Total Patients: <strong><%= totalPatients %></strong></div>
        <div class="stat">Morning (6am–12pm): <%= morning %></div>
        <div class="stat">Afternoon (12pm–6pm): <%= afternoon %></div>
        <div class="stat">Evening (6pm–12am): <%= evening %></div>
        <div class="stat">Peak Hour: <%= peakHourLabel %> (<%= hourlyCounts[peakHour] %> patients)</div>
        <% if (firstPatientTime != null && lastPatientTime != null) { %>
            <div class="stat">First Patient Time: <%= firstPatientTime %></div>
            <div class="stat">Last Patient Time: <%= lastPatientTime %></div>
        <% } %>

        <div class="section-title">Patient Records</div>
        <% if (records != null && !records.isEmpty()) { %>
            <table>
                <tr>
                    <th>#</th>
                    <th>Record</th>
                </tr>
                <% for (int i = 0; i < records.size(); i++) { 
                    String record = records.get(i);
                    String timePart = record.replaceAll(".*\\[(\\d{2}):(\\d{2}).*\\].*", "$1:$2");
                    int hour = Integer.parseInt(timePart.split(":")[0]);
                    int minute = Integer.parseInt(timePart.split(":")[1]);
                    String amPm = hour < 12 ? "am" : "pm";
                    int displayHour = (hour % 12 == 0) ? 12 : hour % 12;
                    String formattedTime = String.format("%d:%02d%s", displayHour, minute, amPm);
                    String updatedRecord = record.replaceFirst("\\[\\d{2}:\\d{2}.*?\\]", "[" + formattedTime + "]");
                %>
                    <tr>
                        <td><%= i + 1 %></td>
                        <td><%= updatedRecord %></td>
                    </tr>
                <% } %>
            </table>
        <% } else { %>
            <p class="no-data">No patient records found for this date.</p>
        <% } %>
    </div>
</body>
</html>