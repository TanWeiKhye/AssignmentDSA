/**
 * author: TanWeiKhye
 **/
package servlet;

import control.MaintainConsultation;
import control.MaintainPatient;
import entity.Consultation;
import entity.Doctor;
import entity.Patient;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MaintainConsultationServlet extends HttpServlet { // CHANGED CLASS NAME
    private MaintainConsultation consultationManager;
    
    @Override
    public void init() throws ServletException {
        super.init();
        consultationManager = new MaintainConsultation();
        getServletContext().setAttribute("consultationManager", consultationManager);
        System.out.println("MaintainConsultationServlet initialized");
    }
    
    // Add this method to your servlet
    private void handleException(HttpServletRequest request, HttpServletResponse response, 
                           Exception e, String operation) throws ServletException, IOException {
    System.err.println("ERROR in " + operation + ": " + e.getMessage());
    e.printStackTrace(); // This will show the full stack trace in server logs
    
    String errorMsg = "Error in " + operation + ": " + e.getMessage();
    if (e.getMessage() == null) {
        errorMsg = "Error in " + operation + ". Please check server logs for details.";
    }
    
    request.setAttribute("message", errorMsg);
    request.getRequestDispatcher("consultationManagement.jsp").forward(request, response);
}
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        // Get patient manager from context
        MaintainPatient patientManager = (MaintainPatient) getServletContext().getAttribute("patientManager");
        
        if (patientManager == null) {
            request.setAttribute("message", "Patient system not initialized. Please access patient management first.");
            request.getRequestDispatcher("consultationManagement.jsp").forward(request, response);
            return;
        }
        
        // Load consultations from file
        try {
            String consultationFile = getServletContext().getRealPath("/") + "data/consultations.txt";
            consultationManager.loadConsultationsFromFile(consultationFile, patientManager);
            System.out.println("Loaded " + consultationManager.getConsultationCount() + " consultations");
        } catch (IOException e) {
            System.err.println("Error loading consultation data: " + e.getMessage());
            request.setAttribute("message", "Error loading consultation data: " + e.getMessage());
        }
        
        if ("viewPatientHistory".equals(action)) {
            String patientIc = request.getParameter("patientIc");
            adt.ListInterface<Consultation> patientConsultations = 
                consultationManager.getConsultationsByPatient(patientIc);
            
            // Convert to array for JSP
            Consultation[] consultationsArray = new Consultation[patientConsultations.size()];
            for (int i = 0; i <= patientConsultations.size(); i++) {
                consultationsArray[i] = patientConsultations.get(i);
            }
            
            request.setAttribute("patientConsultations", consultationsArray);
            request.getRequestDispatcher("patientHistory.jsp").forward(request, response);
            
        } else if ("viewDoctorSchedule".equals(action)) {
            String doctorIc = request.getParameter("doctorIc");
            adt.ListInterface<Consultation> doctorConsultations = 
                consultationManager.getConsultationsByDoctor(doctorIc);
            
            // Convert to array for JSP
            Consultation[] consultationsArray = new Consultation[doctorConsultations.size()];
            for (int i = 0; i <= doctorConsultations.size(); i++) {
                consultationsArray[i] = doctorConsultations.get(i);
            }
            
            request.setAttribute("doctorConsultations", consultationsArray);
            request.getRequestDispatcher("doctorSchedule.jsp").forward(request, response);
            
        } else {
            // Get all consultation lists for display
            request.setAttribute("consultations", consultationManager.getAllConsultations());
            
            // Convert ListInterface to arrays for JSP
            adt.ListInterface<Consultation> scheduled = consultationManager.getScheduledConsultations();
            adt.ListInterface<Consultation> inProgress = consultationManager.getInProgressConsultations();
            adt.ListInterface<Consultation> completed = consultationManager.getCompletedConsultations();
            
            System.out.println("Scheduled consultations: " + scheduled.size());
            System.out.println("InProgress consultations: " + inProgress.size());
            System.out.println("Completed consultations: " + completed.size());
            
            Consultation[] scheduledArray = new Consultation[scheduled.size()];
            Consultation[] inProgressArray = new Consultation[inProgress.size()];
            Consultation[] completedArray = new Consultation[completed.size()];
            
            for (int i = 0; i <= scheduled.size(); i++) scheduledArray[i] = scheduled.get(i);
            for (int i = 0; i <= inProgress.size(); i++) inProgressArray[i] = inProgress.get(i);
            for (int i = 0; i <= completed.size(); i++) completedArray[i] = completed.get(i);
            
            request.setAttribute("scheduledConsultations", scheduledArray);
            request.setAttribute("inProgressConsultations", inProgressArray);
            request.setAttribute("completedConsultations", completedArray);
            
            request.getRequestDispatcher("consultationManagement.jsp").forward(request, response); // FIXED JSP NAME
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        // Get patient manager from context
        MaintainPatient patientManager = (MaintainPatient) getServletContext().getAttribute("patientManager");
        
        if (patientManager == null) {
            request.setAttribute("message", "Patient system not initialized. Please access patient management first.");
            request.getRequestDispatcher("consultationManagement.jsp").forward(request, response);
            return;
        }
        
        // Load consultations before processing POST actions
        try {
            String consultationFile = getServletContext().getRealPath("/") + "data/consultations.txt";
            consultationManager.loadConsultationsFromFile(consultationFile, patientManager);
        } catch (IOException e) {
            System.err.println("Error loading consultation data: " + e.getMessage());
            request.setAttribute("message", "Error loading consultation data: " + e.getMessage());
        }
        
        try {
            switch (action) {
                case "createConsultation":
                    handleCreateConsultation(request, response, patientManager);
                    break;
                case "startConsultation":
                    handleStartConsultation(request, response);
                    break;
                case "completeConsultation":
                    handleCompleteConsultation(request, response);
                    break;
                case "cancelConsultation":
                    handleCancelConsultation(request, response);
                    break;
                case "scheduleFollowUp":
                    handleScheduleFollowUp(request, response);
                    break;
                case "rescheduleConsultation":
                    handleRescheduleConsultation(request, response);
                    break;
                case "deleteConsultation":
                    handleDeleteConsultation(request, response);
                    break;
                default:
                    request.setAttribute("message", "Unknown action: " + action);
                    request.getRequestDispatcher("consultationManagement.jsp").forward(request, response); // FIXED
            }
        } catch (Exception e) {
             handleException(request, response, e, action); 
        }
    }
    
    private void handleCreateConsultation(HttpServletRequest request, HttpServletResponse response, MaintainPatient patientManager)
            throws ServletException, IOException {
        String patientIc = request.getParameter("patientIc");
        String doctorIc = request.getParameter("doctorIc");
        LocalDateTime consultationDateTime = parseDateTime(request.getParameter("consultationDateTime"));
        
        System.out.println("Creating consultation for patient IC: " + patientIc + ", doctor IC: " + doctorIc);
        
        Patient patient = patientManager.searchPatientByIC(patientIc);
        if (patient == null) {
            String errorMsg = "Patient not found with IC: " + patientIc + ". Available patients: ";
            // Add debug info about available patients
            for (Patient p : patientManager.getAllPatientsArray()) {
                errorMsg += p.getIc() + " (" + p.getName() + "), ";
            }
            request.setAttribute("message", errorMsg);
            request.getRequestDispatcher("consultationManagement.jsp").forward(request, response);
            return;
        }
        
        // Create doctor properly
        Doctor doctor = new Doctor();
        if (doctorIc != null && !doctorIc.trim().isEmpty()) {
            doctor.setIc(doctorIc);
        } else {
            doctor.setIc("UNKNOWN_DOCTOR"); // Default value
        }
        
        boolean success = consultationManager.createConsultation(patient, doctor, consultationDateTime, null);
        
        if (success) {
            // Save to file after creation
            try {
                String consultationFile = getServletContext().getRealPath("/") + "data/consultations.txt";
                consultationManager.saveConsultationsToFile(consultationFile);
                request.setAttribute("message", "Consultation scheduled successfully");
            } catch (IOException e) {
                request.setAttribute("message", "Consultation created but failed to save to file: " + e.getMessage());
            }
        } else {
            request.setAttribute("message", "Failed to schedule consultation");
        }
        
        request.getRequestDispatcher("consultationManagement.jsp").forward(request, response); // FIXED
    }
    
    private void handleStartConsultation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String consultationId = request.getParameter("consultationId");
        
        boolean success = consultationManager.startConsultation(consultationId);
        
        if (success) {
            request.setAttribute("message", "Consultation started successfully");
        } else {
            request.setAttribute("message", "Failed to start consultation");
        }
        
        request.getRequestDispatcher("consultationManagement.jsp").forward(request, response); // FIXED
    }
    
    private void handleCompleteConsultation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String consultationId = request.getParameter("consultationId");
        
        boolean success = consultationManager.completeConsultation(consultationId);
        
        if (success) {
            request.setAttribute("message", "Consultation completed successfully");
        } else {
            request.setAttribute("message", "Failed to complete consultation");
        }
        
        request.getRequestDispatcher("consultationManagement.jsp").forward(request, response); // FIXED
    }
    
    private void handleCancelConsultation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String consultationId = request.getParameter("consultationId");
        
        boolean success = consultationManager.cancelConsultation(consultationId);
        
        if (success) {
            request.setAttribute("message", "Consultation cancelled successfully");
        } else {
            request.setAttribute("message", "Failed to cancel consultation");
        }
        
        request.getRequestDispatcher("consultationManagement.jsp").forward(request, response); // FIXED
    }
    
    private void handleScheduleFollowUp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String consultationId = request.getParameter("consultationId");
        LocalDateTime nextAppointment = parseDateTime(request.getParameter("nextAppointment"));
        
        boolean success = consultationManager.scheduleFollowUp(consultationId, nextAppointment);
        
        if (success) {
            request.setAttribute("message", "Follow-up appointment scheduled successfully");
        } else {
            request.setAttribute("message", "Failed to schedule follow-up appointment");
        }
        
        request.getRequestDispatcher("consultationManagement.jsp").forward(request, response); // FIXED
    }
    
    private void handleRescheduleConsultation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String consultationId = request.getParameter("consultationId");
        LocalDateTime newDateTime = parseDateTime(request.getParameter("newDateTime"));
        
        boolean success = consultationManager.rescheduleConsultation(consultationId, newDateTime);
        
        if (success) {
            request.setAttribute("message", "Consultation rescheduled successfully");
        } else {
            request.setAttribute("message", "Failed to reschedule consultation - doctor may not be available");
        }
        
        request.getRequestDispatcher("consultationManagement.jsp").forward(request, response); // FIXED
    }
    
    private void handleDeleteConsultation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String consultationId = request.getParameter("consultationId");
        
        boolean success = consultationManager.deleteConsultation(consultationId);
        
        if (success) {
            request.setAttribute("message", "Consultation deleted successfully");
        } else {
            request.setAttribute("message", "Failed to delete consultation");
        }
        
        request.getRequestDispatcher("consultationManagement.jsp").forward(request, response); // FIXED
    }
    
    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDateTime.parse(dateTimeStr, formatter);
    }