<%-- 
    Document   : diagnosis
    Created on : 1 Sept 2025, 6:57:50 pm
    Author     : Cham Voon Loong
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="adt.TreeInterface, adt.AVLTree, entity.Treatment"%>

<%
    TreeInterface<Treatment> allTreatments = (TreeInterface<Treatment>) application.getAttribute("treatmentRecords");
    if (allTreatments == null) {
        allTreatments = new AVLTree();
        allTreatments.insert(new Treatment("Rest (Default)"));
        allTreatments.insert(new Treatment("Hydration (Default)"));
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patient Diagnosis</title>
    <style>
        body { font-family: sans-serif; background-color: #f4f4f9; color: #333; }
        .container { max-width: 700px; margin: 50px auto; padding: 30px; background: #fff; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }
        h1, h2 { text-align: center; color: #444; }
        .patient-info { border: 1px solid #ddd; border-radius: 8px; padding: 20px; margin-bottom: 20px; background: #fafafa; display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
        .patient-info p { margin: 5px 0; }
        .form-group { margin-bottom: 20px; }
        label { display: block; margin-bottom: 8px; font-weight: bold; }
        input[type="text"], select { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        .treatment-row { display: flex; align-items: center; margin-bottom: 10px; gap: 10px; }
        .treatment-row select { flex-grow: 1; }
        button { padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
        .btn-add { background-color: #28a745; color: white; }
        .btn-remove { background-color: #dc3545; color: white; }
        .btn-submit { background-color: #007bff; color: white; width: 100%; padding: 12px; }
    </style>
</head>
<body>

    <div class="container">
        <h1>Patient Diagnosis Form</h1>
        
        <!-- Hard-coded Patient Info -->
        <h2>Patient Details</h2>
        <div class="patient-info">
            <p><strong>Name:</strong> John Doe</p>
            <p><strong>Age:</strong> 45</p>
            <p><strong>Gender:</strong> Male</p>
            <p><strong>Contact:</strong> 012-345 6789</p>
            <p><strong>Doctor in Charge:</strong> Dr. Mary Carter</p>
        </div>
        

        <hr style="margin: 30px 0;">
        
        <form action="MaintainDiagnosisServlet" method="post">
            <h2>New Diagnosis Record</h2>
            <div class="form-group">
                <label for="diagnosis">Diagnosis:</label>
                <input type="text" id="diagnosis" name="diagnosis" placeholder="e.g., Common Cold" required>
            </div>

            <div class="form-group">
                <label>Treatments:</label>
                <div id="treatments-container">
                    <div class="treatment-row">
                        <select name="treatments" required>
                            <option value="" disabled selected>-- Select a treatment --</option>
                            <% for (Treatment t :(AVLTree<Treatment>) allTreatments) { %>
                                <option value="<%= t.getTreatment() %>"><%= t.getTreatment() %></option>
                            <% } %>
                        </select>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <button type="button" id="add-treatment-btn" class="btn-add">Add Another Treatment</button>
            </div>
            
            <button type="submit" class="btn-submit">Save Diagnosis</button>
        </form>
    </div>

    <script>
        const addBtn = document.getElementById('add-treatment-btn');
        const container = document.getElementById('treatments-container');
        const firstTreatmentRow = container.querySelector('.treatment-row');

        addBtn.addEventListener('click', function() {
            const newRow = firstTreatmentRow.cloneNode(true);
            const removeBtn = document.createElement('button');
            removeBtn.type = 'button';
            removeBtn.textContent = 'Remove';
            removeBtn.className = 'btn-remove';
            removeBtn.addEventListener('click', () => newRow.remove());
            newRow.appendChild(removeBtn);
            container.appendChild(newRow);
        });
    </script>

</body>
</html>


