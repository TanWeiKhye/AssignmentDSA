<%-- 
    Document   : home
    Created on : Aug 15, 2025, 3:15:52 PM
    Author     : hongj
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clinic — Entry</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f6f8fb;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            position: relative;
        }
        .back-btn {
            position: absolute;
            top: 20px;
            left: 20px;
            background-color: #888;
            color: white;
            text-decoration: none;
            padding: 8px 14px;
            border-radius: 5px;
            font-size: 14px;
        }
        .back-btn:hover {
            background-color: #555;
        }
        .box {
            text-align: center;
            background: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 3px 8px rgba(0,0,0,0.15);
        }
        button {
            margin: 15px;
            padding: 12px 20px;
            font-size: 1.1em;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }
        .patient-btn {
            background: #1f6feb;
            color: white;
        }
        .admin-btn {
            background: #f28b82;
            color: white;
        }
        button:hover { opacity: 0.9; }
    </style>
</head>
<body>
    <a href="/AssignmentDSA/" class="back-btn">← Back to Home</a>
<div class="box">

    <h1>Welcome to Clinic Queue System</h1>
    <p>Please choose your role:</p>
    <form action="patientForm.jsp" method="get" style="display:inline;">
        <button class="patient-btn">Patient</button>
    </form>
    <form action="MaintainPatientServlet" method="get" style="display:inline;">
        <button class="admin-btn">Admin</button>
    </form>
</div>
</body>
</html>