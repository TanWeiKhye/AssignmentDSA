<%-- 
    Document   : queueTimeReport
    Created on : Aug 19, 2025, 11:47:27 PM
    Author     : hongj
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="adt.PatientArrayList" %>
<%@ page import="entity.QueueAverage" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    Double overallAvg = (Double) request.getAttribute("overallAvg");
    PatientArrayList<QueueAverage> periodAverages =
        (PatientArrayList<QueueAverage>) request.getAttribute("periodAverages");

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
    <title>Queue Time Report</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 40px;
            background-color: #f5f7fa;
        }

        .report-container {
            max-width: 800px;
            margin: auto;
            background-color: white;
            padding: 30px 40px;
            border: 1px solid #ccc;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }

        .top-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .back-btn {
            background-color: #3498db;
            color: white;
            text-decoration: none;
            padding: 8px 16px;
            border-radius: 4px;
            font-size: 16px;
        }

        .back-btn:hover {
            background-color: #2980b9;
        }

        form {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        input[type="date"] {
            padding: 6px 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        button {
            padding: 8px 16px;
            background-color: #3498db;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
        }

        button:hover {
            background-color: #2980b9;
        }

        h1 {
            text-align: center;
            color: #2c3e50;
            margin-bottom: 10px;
        }

        .report-date {
            text-align: center;
            font-size: 18px;
            margin-bottom: 25px;
            color: #555;
        }

        p {
            font-size: 18px;
            color: #333;
            margin-bottom: 25px;
        }

        h3 {
            color: #2c3e50;
            margin-bottom: 15px;
        }

        ul {
            list-style-type: none;
            padding-left: 0;
        }

        li {
            background-color: #fdfdfd;
            padding: 12px 16px;
            margin-bottom: 8px;
            border-left: 6px solid #3498db;
            border-radius: 4px;
            box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
            font-size: 16px;
            color: #333;
        }

        .no-data {
            color: #777;
            font-style: italic;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="report-container">

    <div class="top-bar">
        <form action="MaintainPatientServlet" method="get" style="display:inline;"> 
                <button class="back-btn">← Back</button> 
        </form>

        <form method="get" action="MaintainPatientServlet">
            <input type="hidden" name="action" value="queueTimeReport">
            <input type="date" name="reportDate" value="<%= reportDate.toString() %>" required>
            <button type="submit">View</button>
        </form>
    </div>

    <h1>Queue Time Report</h1>
    <div class="report-date">Report for: <%= formattedDate %></div>

    <p><strong>Overall Average:</strong> <%= String.format("%.2f", overallAvg) %> minutes</p>

    <h3>Time Period Averages:</h3>
    <ul>
        <% if (periodAverages != null && periodAverages.size() > 0) {
               for (int i = 0; i < periodAverages.size(); i++) {
                   QueueAverage q = periodAverages.get(i); %>
            <li><%= q.getTimePeriod() %> → <%= String.format("%.2f", q.getAverageTimeInMinutes()) %> minutes</li>
        <%   }
           } else { %>
            <li class="no-data">No period data available.</li>
        <% } %>
    </ul>

</div>
</body>
</html>