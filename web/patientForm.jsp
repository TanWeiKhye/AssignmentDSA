<%-- 
    Document   : patientForm
    Created on : Aug 15, 2025, 3:03:52 PM
    Author     : hongj
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Patient Registration</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f6f8fb;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .form-box {
            background: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 3px 8px rgba(0,0,0,0.15);
            width: 90%;
            max-width: 700px;
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #1f6feb;
        }

        label {
            display: block;
            margin-top: 10px;
            font-weight: bold;
            color: #333;
        }

        input, select {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border-radius: 6px;
            border: 1px solid #ccc;
            box-sizing: border-box;
        }

        .button-row {
            display: flex;
            justify-content: space-between;
            margin-top: 30px;
        }

        button {
            flex: 1;
            padding: 12px;
            margin: 0 10px;
            font-size: 1em;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }

        .next-btn {
            background: #1f6feb;
            color: white;
        }

        .back-btn {
            background: #f28b82;
            color: white;
        }

        button:hover {
            opacity: 0.9;
        }
    </style>
</head>
<body>
<div class="form-box">
    <h2>Patient Registration</h2>
    <form action="MaintainPatientServlet" method="post">
        <input type="hidden" name="action" value="patientAdd">
        <label>IC</label>
        <input type="text" name="ic" required>

        <label>Name</label>
        <input type="text" name="name" required>

        <label>Phone</label>
        <input type="text" name="phone">

        <label>Email</label>
        <input type="email" name="email">

        <label>Gender</label>
        <select name="gender">
            <option value="">-- Select Gender --</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
        </select>

        <label>Date of Birth</label>
        <input type="date" name="dob">

        <label>Address</label>
        <input type="text" name="address">

        <div class="button-row">
            <button type="submit" class="next-btn">Submit</button>
            <button type="button" class="back-btn" onclick="window.location.href='home.jsp'">Back</button>
            
        </div>
    </form>
</div>
</body>
</html>