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

public class MaintainConsultationServlet extends HttpServlet { 
    private MaintainConsultation consultationManager;
    private String consultationFile;
    
    @Override
    public void init() throws ServletException {
        super.init();
        consultationManager = new MaintainConsultation();
        consultationFile = getServletContext().getRealPath("/") + "data/consultations.txt";
        
        
        MaintainPatient patientManager = (MaintainPatient) getServletContext().getAttribute("patientManager");
        if (patientManager != null){
            try{
                consultationManager.loadConsultationsFromFile(consultationFile,patientManager);
            } catch (IOException e){
                System.err.println("Error loading consultations: " + e.getMessage());
            }
        }
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
    
    request.getSession().setAttribute("message", errorMsg);
    response.sendRedirect("MaintainConsultationServlet");
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
            consultationManager.loadConsultationsFromFile(consultationFile, patientManager);
            System.out.println("Loaded " + consultationManager.getConsultationCount() + " consultations");
        } catch (IOException e) {
            System.err.println("Error loading consultation data: " + e.getMessage());
            request.setAttribute("message", "Error loading consultation data: " + e.getMessage());
        }
        
        if ("viewPatientHistory".equals(action)) {
            String patientIc = request.getParameter("patientIc");
            adt.ListInterface<Consultation> patientConsultations = 
                consultationManager.getConsultationsByPatientIc(patientIc);
            
            // Convert to array for JSP
            Consultation[] consultationsArray = new Consultation[patientConsultations.size()];
            for (int i = 0; i < patientConsultations.size(); i++) {
                consultationsArray[i] = patientConsultations.get(i);
            }
            
            request.setAttribute("patientConsultations", consultationsArray);
            request.getRequestDispatcher("patientHistory.jsp").forward(request, response);
            
        } else if ("viewDoctorSchedule".equals(action)) {
            String doctorIc = request.getParameter("doctorIc");
            adt.ListInterface<Consultation> doctorConsultations = 
                consultationManager.getConsultationsByDoctorIc(doctorIc);
            
            // Convert to array for JSP
            Consultation[] consultationsArray = new Consultation[doctorConsultations.size()];
            for (int i = 0; i < doctorConsultations.size(); i++) {
                consultationsArray[i] = doctorConsultations.get(i);
            }
            
            request.setAttribute("doctorConsultations", consultationsArray);
            request.getRequestDispatcher("doctorSchedule.jsp").forward(request, response);
            
        } else {
            // Convert all consultations to arrays for JSP
        adt.ListInterface<Consultation> allConsultationsList = consultationManager.getAllConsultations();
        Consultation[] allConsultations = new Consultation[allConsultationsList.size()];
        for (int i = 0; i < allConsultationsList.size(); i++) {
            allConsultations[i] = allConsultationsList.get(i);
        }

        adt.ListInterface<Consultation> scheduledList = consultationManager.getScheduledConsultations();
        Consultation[] scheduledArray = new Consultation[scheduledList.size()];
        for (int i = 0; i < scheduledList.size(); i++) scheduledArray[i] = scheduledList.get(i);

        adt.ListInterface<Consultation> inProgressList = consultationManager.getInProgressConsultations();
        Consultation[] inProgressArray = new Consultation[inProgressList.size()];
        for (int i = 0; i < inProgressList.size(); i++) inProgressArray[i] = inProgressList.get(i);

        adt.ListInterface<Consultation> completedList = consultationManager.getCompletedConsultations();
        Consultation[] completedArray = new Consultation[completedList.size()];
        for (int i = 0; i < completedList.size(); i++) completedArray[i] = completedList.get(i);

        // Set attributes
        request.setAttribute("consultations", allConsultations);
        request.setAttribute("scheduledConsultations", scheduledArray);
        request.setAttribute("inProgressConsultations", inProgressArray);
        request.setAttribute("completedConsultations", completedArray);

        request.getRequestDispatcher("consultationManagement.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        // Get patient manager from context
        MaintainPatient patientManager = (MaintainPatient) getServletContext().getAttribute("patientManager");
        
        if (patientManager == null) {
            request.getSession().setAttribute("message", "Patient system not initialized. Please access patient management first.");
            response.sendRedirect("MaintainConsultationServlet");
            return;
        }
        
        // Load consultations before processing POST actions
        try {
            consultationManager.loadConsultationsFromFile(consultationFile, patientManager);
        } catch (IOException e) {
            request.getSession().setAttribute("message", "Error loading consultation data: " + e.getMessage());
            response.sendRedirect("MaintainConsultationServlet");
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
                    request.getSession().setAttribute("message", "Unknown action: " + action);
                    response.sendRedirect("MaintainConsultationServlet");
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
            request.getSession().setAttribute("message", errorMsg);
            response.sendRedirect("MaintainConsultationServlet");
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
            // Save to file after creations
            try {
                consultationManager.saveConsultationsToFile(consultationFile);
                request.setAttribute("message", "Consultation scheduled successfully");
            } catch (IOException e) {
                request.setAttribute("message", "Consultation created but failed to save to file: " + e.getMessage());
                return;
            }
        } else {
            request.getSession().setAttribute("message", "Failed to schedule consultation");
        }
        
        response.sendRedirect("MaintainConsultationServlet");
    }
    
    private void handleStartConsultation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String consultationId = request.getParameter("consultationId");
        
        boolean success = consultationManager.startConsultation(consultationId,consultationFile);
        
        if (success) {
            request.getSession().setAttribute("message", "Consultation started successfully");
        } else {
            request.getSession().setAttribute("message", "Failed to start consultation");
        }
        
        response.sendRedirect("MaintainConsultationServlet");
    }
    
    private void handleCompleteConsultation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String consultationId = request.getParameter("consultationId");
        
        boolean success = consultationManager.completeConsultation(consultationId, consultationFile);
        
        if (success) {
            request.getSession().setAttribute("message", "Consultation completed successfully");
        } else {
            request.getSession().setAttribute("message", "Failed to complete consultation");
        }
        
        response.sendRedirect("MaintainConsultationServlet");
    }
    
    private void handleCancelConsultation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String consultationId = request.getParameter("consultationId");
        
        boolean success = consultationManager.cancelConsultation(consultationId, consultationFile);
        
        if (success) {
            request.getSession().setAttribute("message", "Consultation cancelled successfully");
        } else {
            request.getSession().setAttribute("message", "Failed to cancel consultation");
        }
        
        response.sendRedirect("MaintainConsultationServlet");
    }
    
    private void handleScheduleFollowUp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String consultationId = request.getParameter("consultationId");
        LocalDateTime nextAppointment = parseDateTime(request.getParameter("nextAppointment"));
        
        boolean success = consultationManager.scheduleFollowUp(consultationId, nextAppointment);
        
        if (success) {
            consultationManager.saveConsultationsToFile(consultationFile);
            request.getSession().setAttribute("message", "Follow-up appointment scheduled successfully");
        } else {
            request.getSession().setAttribute("message", "Failed to schedule follow-up appointment");
        }
        
        response.sendRedirect("MaintainConsultationServlet");
    }
    
    private void handleRescheduleConsultation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String consultationId = request.getParameter("consultationId");
        LocalDateTime newDateTime = parseDateTime(request.getParameter("newDateTime"));
        
        boolean success = consultationManager.rescheduleConsultation(consultationId, newDateTime);
        
        if (success) {
            consultationManager.saveConsultationsToFile(consultationFile);
            request.getSession().setAttribute("message", "Consultation rescheduled successfully");
        } else {
            request.getSession().setAttribute("message", "Failed to reschedule consultation - doctor may not be available");
        }
        
        response.sendRedirect("MaintainConsultationServlet");
    }
    
    private void handleDeleteConsultation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String consultationId = request.getParameter("consultationId");
        
        boolean success = consultationManager.deleteConsultation(consultationId);
        
        if (success) {
            request.getSession().setAttribute("message", "Consultation deleted successfully");
        } else {
            request.getSession().setAttribute("message", "Failed to delete consultation");
        }
        
        response.sendRedirect("MaintainConsultationServlet");
    }
    
    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDateTime.parse(dateTimeStr, formatter);
    }
}