<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="adt.TreeInterface, entity.Doctor, adt.AVLTree"%>
<%
        AVLTree<Doctor> doctors = (AVLTree<Doctor>) request.getAttribute("doctors");
        String lastIc = "";
        for (Doctor doctor : doctors) {
            lastIc = doctor.lastNumOfIc(doctor.getIc());
            break;
        }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Clinic Management System</title>

        <!-- FONT -->
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Arvo:ital,wght@0,400;0,700;1,400;1,700&family=Exo+2:ital,wght@0,100..900;1,100..900&family=Fredoka:wght@300..700&display=swap"
            rel="stylesheet"
            />

        <!-- ICON -->
        <link
            href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css"
            rel="stylesheet"
            />

        <!-- MUST CSS -->
        <link rel="stylesheet" href="css/style.css" />
        <link rel="stylesheet" href="css/modern-normalize.css" />
        <link rel="stylesheet" href="css/util.css" />

        <!-- MAIN CSS -->
        <link rel="stylesheet" href="css/components/home.css" />
    </head>
    <body>
        <header>
            <div class="logo"></div>
            <div class="nav"></div>
        </header>
        <main>
            <div class="container">
                <h1>Welcome to the Clinic Management System</h1>
                <div class="link">
                    <a href="/AssignmentDSA/home.jsp" class="link_item">
                        Patient Management Module
                    </a>
                    <a href="DoctorManagement?ic=<%= lastIc %>" class="link_item">
                        Doctor Management Module
                    </a>
                    <a href="MaintainConsultationServlet" class="link_item">
                        Consultation Management Module
                    </a>
                    <a href="/AssignmentDSA/medicalRecordAdmin.jsp" class="link_item">
                        Medical Treatment Management Module
                    </a>
                    <a href="/AssignmentDSA/pharmacy.jsp" class="link_item">
                        Pharmacy Management Module
                    </a>
                </div>
            </div>
        </main>
    </body>
</html>
