<%-- 
    Document   : dailyReport
    Created on : Aug 19, 2025, 11:41:33 PM
    Author     : hongj
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="adt.PatientArrayList" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    PatientArrayList<String> records = (PatientArrayList<String>) request.getAttribute("dailyReport");
    int totalPatients = (records != null) ? records.size() : 0;

    String reportDateStr = request.getParameter("reportDate");
    LocalDate reportDate = (reportDateStr != null && !reportDateStr.isEmpty()) 
                           ? LocalDate.parse(reportDateStr)
                           : LocalDate.now();

    String formattedDate = reportDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Daily Patient Report</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 40px;
            background-color: #f9f9f9;
        }

        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 10px;
        }

        .report-container {
            background-color: #fff;
            border: 1px solid #ccc;
            padding: 25px 35px;
            border-radius: 8px;
            max-width: 800px;
            margin: auto;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }

        .total-count, .report-date {
            font-weight: bold;
            color: #2c3e50;
            font-size: 18px;
            margin-bottom: 15px;
        }

        form {
            text-align: center;
            margin-bottom: 25px;
        }

        input[type="date"] {
            padding: 8px 12px;
            font-size: 16px;
            border-radius: 4px;
            border: 1px solid #ccc;
            margin-right: 10px;
        }

        button, .back-btn {
            padding: 10px 20px;
            background-color: #337ab7;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
        }

        button:hover, .back-btn:hover {
            background-color: #286090;
        }

        ul {
            list-style-type: none;
            padding: 0;
        }

        li {
            padding: 10px 15px;
            margin-bottom: 10px;
            border-bottom: 1px solid #e0e0e0;
            background-color: #fafafa;
            font-size: 16px;
            color: #333;
        }

        li:last-child {
            border-bottom: none;
        }

        .no-data {
            color: #777;
            text-align: center;
            font-style: italic;
        }


        .top-bar {
            display: flex;
            justify-content: flex-start;
            margin-bottom: 10px;
        }

        .back-btn {
            padding: 8px 16px;
            background-color: #337ab7;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-size: 16px;
        }

        .back-btn:hover {
            background-color: #286090;
        }

    </style>
</head>
<body>
    <div class="report-container">
        <div class="top-bar">
            <form action="MaintainPatientServlet" method="get" style="display:inline;"> 
                <button class="back-btn">← Back</button> 
            </form>
        </div>
        <h1>Daily Patient Report</h1>

        <form method="get" action="MaintainPatientServlet">
            <input type="hidden" name="action" value="dailyReport">
            <input type="date" name="reportDate" value="<%= reportDate.toString() %>" required>
            <button type="submit">View Report</button>
        </form>

        <div class="report-date">Report for: <%= formattedDate %></div>
        <div class="total-count">Total Patients: <%= totalPatients %></div>

        <% if (records != null && !records.isEmpty()) { %>
            <ul>
                <% for (int i = 0; i < records.size(); i++) { %>
                    <%
                        String record = records.get(i);
                        String timePart = record.replaceAll(".*\\[(\\d{2}):(\\d{2}).*\\].*", "$1:$2");
                        int hour = Integer.parseInt(timePart.split(":")[0]);
                        int minute = Integer.parseInt(timePart.split(":")[1]);
                        String amPm = hour < 12 ? "am" : "pm";
                        int displayHour = (hour % 12 == 0) ? 12 : hour % 12;
                        String formattedTime = String.format("%d:%02d%s", displayHour, minute, amPm);
                        String updatedRecord = record.replaceFirst("\\[\\d{2}:\\d{2}.*?\\]", "[" + formattedTime + "]");
                    %>
                    <li><%= updatedRecord %></li>
                <% } %>
            </ul>
        <% } else { %>
            <p class="no-data">No records found for this date.</p>
        <% } %>


    </div>
</body>
</html>