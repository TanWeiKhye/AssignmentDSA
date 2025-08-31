<%-- 
    Document   : medicalRecordAdmin
    Created on : 31 Aug 2025, 1:26:52 pm
    Author     : Cham Voon Loong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clinic Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
    <!-- FontAwesome Icons CDN -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <!--CSS for this page -->
    <link href="css/medicalRecordAdmin.css" rel="stylesheet" />
</head>
<body>

    <!-- Overlay for dimming the background -->
    <div class="overlay"></div>

    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">Medical Record Module</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="#">Home</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">Manage Settings</a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <li><a class="dropdown-item" href="#">Diagnosis</a></li>
                            <li><a class="dropdown-item" href="#">Treatment</a></li>
                            <li><a class="dropdown-item" href="#">Medication</a></li>
                        </ul>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">Reports</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="main-content">
        <div class="button-container">
            <!-- Diagnosis Button -->
            <a href="#" class="btn btn-large">
                <i class="fas fa-stethoscope"></i> Diagnosis
            </a>

            <!-- Treatment Button -->
            <a href="#" class="btn btn-large">
                <i class="fas fa-pills"></i> Treatment
            </a>

            <!-- Medication Button -->
            <a href="#" class="btn btn-large">
                <i class="fas fa-medkit"></i> Medication
            </a>

            <!-- Reports Button -->
            <a href="#" class="btn btn-large">
                <i class="fas fa-file-alt"></i> Reports
            </a>
        </div>
    </div>

    <!-- Bootstrap JS & dependencies -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
</body>
</html>
