<%-- 
    Document   : treatment
    Created on : 1 Sept 2025, 1:28:26 am
    Author     : Cham Voon Loong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="entity.Treatment"%>
<%@page import="adt.TreeInterface"%>
<%@page import="adt.AVLTree"%>
<%@page import="java.util.Iterator" %>

<%
    TreeInterface<Treatment> treatmentRecord = new AVLTree<Treatment>();
    treatmentRecord = (AVLTree<Treatment>)request.getAttribute("treatmentRecords");
    Treatment searchResult = (Treatment)request.getAttribute("searchResult");

%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Treatment Management</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
  <link href="css/treatment.css" rel="stylesheet"/>
</head>
<body>
  <div class="overlay"></div>

  <!-- Navbar -->
  <nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">Clinic System</a>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto">
          <li class="nav-item"><a class="nav-link active" href="#">Home</a></li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">Manage Settings</a>
            <ul class="dropdown-menu">
              <li><a class="dropdown-item" href="#">Diagnosis</a></li>
              <li><a class="dropdown-item" href="#">Treatment</a></li>
              <li><a class="dropdown-item" href="#">Medication</a></li>
            </ul>
          </li>
          <li class="nav-item"><a class="nav-link" href="#">Reports</a></li>
        </ul>
      </div>
    </div>
  </nav>

  <!-- Main Content -->
  <div class="main-content">
    <div class="form-container">
      <fieldset>
        <legend>Treatment</legend>
        <div class="top-actions">
          <!-- Add -->
          <i class="fas fa-plus-circle fa-2x text-success" id="addBtn" title="Add Treatment"></i>
          <!-- Search -->
          <div class="search-wrapper">
            <i class="fas fa-search fa-2x text-primary search-icon" title="Search Treatment"></i>
            <form method="post" action="MaintainTreatmentServlet">
                <button type="submit" name="action" value="search" style="display: none;"></button>
                <input type="text" class="search-input" placeholder="Search treatment..." name="searchTreatment">
            </form>
          </div>
          <!-- Sort -->
          <i id="sortIcon" class="fas fa-sort-alpha-down fa-2x text-secondary" title="Sort Asc/Desc"></i>
        </div>

    <!-- Treatment List -->
    <div class="treatment-list" id="treatmentList">
      <% if (treatmentRecord.isEmpty()){ %>
        <span>No Treatment Record</span>

        <% } else {
              for(Treatment t: (AVLTree<Treatment>)treatmentRecord){
        %>
            <div class="treatment-row">
              <span><%= t.getTreatment() %></span>
              <div class="action-icons">
                <!-- Edit button -->
                <i class="fas fa-edit text-warning editBtn"
                   data-bs-toggle="modal"
                   data-bs-target="#editModal"
                   data-treatment="<%=t.getTreatment()%>" onclick="setValue(this.getAttribute('data-treatment'))"
                   title="Edit"></i>

                <!-- Delete button -->
                <form method="post" action="MaintainTreatmentServlet">
                    <input type="hidden" value="<%=t.getTreatment()%>" name="deleteTreatment" id="deleteTreatment"/>
                    <button style="margin:0; border:0; padding:0; background:none;" type="submit" name="action" value="delete"><i class="fas fa-trash-alt text-danger deleteBtn" title="Delete"></i></button>
                </form>
              </div>
            </div>
      <% }} %>
    </div>


        <!-- Add Modal -->
        <form method="post" action="MaintainTreatmentServlet">
            <div class="modal fade" id="addModal" tabindex="-1">
              <div class="modal-dialog modal-dialog-centered"> <!-- centered -->
                <div class="modal-content">
                  <div class="modal-header"><h5 class="modal-title">Add Treatment</h5></div>
                  <div class="modal-body">
                    <input type="text" id="newTreatment" name="addTreatment" class="form-control" placeholder="Enter treatment name" required>
                  </div>
                  <div class="modal-footer">
                    <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button class="btn btn-success" id="saveAdd" type="submit" name="action" value="add">Add</button>
                  </div>
                </div>
              </div>
            </div>
        </form>

        <!-- Edit Modal -->
        <form method="post" action="MaintainTreatmentServlet">
          <div class="modal fade" id="editModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
              <div class="modal-content">
                <div class="modal-header"><h5 class="modal-title">Edit Treatment</h5></div>
                <div class="modal-body">
                  <input type="text" id="editTreatment" name="editTreatment" class="form-control" required/>
                  <input type="hidden" id="originalTreatment" name="originalTreatment" value="" />
                </div>
                <div class="modal-footer">
                  <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                  <button class="btn btn-primary" type="submit" name="action" value="update">Save</button>
                </div>
              </div>
            </div>
          </div>
        </form>



  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
<script>
    
    
  // Modal instances
  const addModal = new bootstrap.Modal(document.getElementById("addModal"));
  const editModal = new bootstrap.Modal(document.getElementById("editModal"));

  // Elements
  const addBtn = document.getElementById("addBtn");
  const saveAdd = document.getElementById("saveAdd");
  const treatmentList = document.getElementById("treatmentList");
  const newTreatmentInput = document.getElementById("newTreatment");

  const editInput = document.getElementById("editTreatment");
  const saveEdit = document.getElementById("saveEdit");

  // Add handler: show modal
  addBtn.addEventListener("click", () => {
    newTreatmentInput.value = "";
    addModal.show();
  });



  // Edit modal state
  let currentRowSpan = null;

  // Attach events to a row, robust to missing classes
  function attachEditEvents(row) {
    const editIcon = row.querySelector(".editBtn") || row.querySelector(".fa-edit");
    const deleteIcon = row.querySelector(".deleteBtn") || row.querySelector(".fa-trash-alt");

    if (editIcon) {
      // open modal and fill input
      editIcon.addEventListener("click", () => {
        currentRowSpan = row.querySelector("span");
        editInput.value = currentRowSpan ? currentRowSpan.textContent.trim() : "";
        editModal.show();
      });
    }

    if (deleteIcon) {
      deleteIcon.addEventListener("click", () => {
        const name = row.querySelector("span") ? row.querySelector("span").textContent.trim() : "";
        confirmDelete(name, row);
      });
    }
  }

  // Delete with confirm; if row missing try to find it by name
  function confirmDelete(name, row) {
    if (!name) return;
    if (!confirm("Delete treatment: " + name + "?")) return;

    // If row not passed, try to find it by name
    if (!row) {
      const rows = document.querySelectorAll(".treatment-row");
      for (const r of rows) {
        const s = r.querySelector("span");
        if (s && s.textContent.trim() === name.trim()) {
          row = r;
          break;
        }
      }
    }

    if (row) row.remove();
    // TODO: call server (AJAX) to delete from AVL backend
    console.log("Deleted (frontend):", name);
  }

  // Optional: search expand/collapse (if you have search UI)
  const searchWrapper = document.querySelector(".search-wrapper");
  const searchIcon = document.querySelector(".search-icon");
  const searchInput = document.querySelector(".search-input");
  if (searchIcon && searchWrapper && searchInput) {
    searchIcon.addEventListener("click", () => {
      searchWrapper.classList.toggle("active");
      if (searchWrapper.classList.contains("active")) searchInput.focus();
    });
    searchInput.addEventListener("blur", () => {
      if (searchInput.value === "") searchWrapper.classList.remove("active");
    });
  }

  // Toggle sort icon (UI only)
  const sortIcon = document.getElementById("sortIcon");
  if (sortIcon) {
    sortIcon.addEventListener("click", function () {
      this.classList.toggle("fa-sort-alpha-down");
      this.classList.toggle("fa-sort-alpha-up");
      // TODO: call server to retrieve sorted data (AVL in-order or reverse)
    });
  }
  

   //Set entered value to the hidden input
   //Fill edit modal with the treatment name
    document.getElementById("editModal").addEventListener("show.bs.modal", event => {
      const button = event.relatedTarget; // The <i> that triggered the modal
      const treatmentName = button.getAttribute("data-treatment");

      document.getElementById("originalTreatment").value = treatmentName;
      document.getElementById("editTreatment").value = treatmentName;
    });

  
  function setValue(value){
      document.getElementById("originalTreatment").value = value;
      console.log(value);
  };

  // Debug helper: log JS errors to console
  window.addEventListener("error", (e) => console.error("JS error:", e.error || e.message));
</script>

</body>
</html>

