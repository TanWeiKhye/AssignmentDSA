/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import entity.Patient;
import entity.PatientQueueTime;
import entity.QueueAverage;
import control.MaintainPatient;
import adt.ArrayList;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.util.logging.*;


public class MaintainPatientServlet extends HttpServlet {

    private MaintainPatient patientManager;
    private static final String DATA_DIR = "data";
    private static final Logger LOGGER = Logger.getLogger(MaintainPatientServlet.class.getName());
    private PatientQueueTime[] queueTimeRecords = new PatientQueueTime[100]; 
    private int queueTimeCount = 0;

    @Override
    public void init() throws ServletException {
        super.init();
        patientManager = new MaintainPatient();

        try {
            loadPatientData();  
            loadQueueData();     
        } catch (IOException e) {
            throw new ServletException("Failed to load patient data", e);
        }

        getServletContext().setAttribute("patientManager", patientManager);
        getServletContext().setAttribute("walkInQueue", patientManager.getQueueAsArray());
    }


    private void loadPatientData() throws IOException {
        patientManager.clearPatientsOnly(); 

        String patientFile = getDataFilePath("patients.txt");
        File file = new File(patientFile);
        
        if (!file.exists()) {
            LOGGER.info("No patient file found. It will be created.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(patientFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 8) {
                    Patient p = new Patient(
                        d[0], d[1], d[2],
                        parseDate(d[3]), d[4], d[5], d[6],
                        parseDate(d[7])
                    );
                    if (patientManager.searchPatientByIC(d[0]) == null) {
                        patientManager.addPatient(p);
                    }
                }
            }
        }
    }
    
    private void loadQueueData() throws IOException {
        String queueFile = getDataFilePath("queue.txt");
        patientManager.clearQueue();
        try (BufferedReader br = new BufferedReader(new FileReader(queueFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) { 
                    String ic = parts[1].trim(); 
                    Patient p = patientManager.searchPatientByIC(ic);
                    if (p != null && !patientManager.isInQueue(ic)) {
                        patientManager.addToQueue(p);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.info("queue.txt not found. Starting with empty queue.");
        }
    }

    private LocalDate parseDate(String s) {
        return (s == null || s.isEmpty() || s.equals("null")) ? null : LocalDate.parse(s);
    }

    private String getDataFilePath(String filename) {
        String webRootPath = getServletContext().getRealPath("/");
        return new File(webRootPath + "../../web/data/" + filename).getAbsolutePath();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("dailyReport".equals(action)) {
            handleDailyPatientReport(request, response); 
            return;
        }
        if ("queueTimeReport".equals(action)) {
            handleQueueTimeReport(request, response); 
            return;
        }

        patientManager.clearPatientsOnly();
        loadPatientData();

        patientManager.clearQueue(); 
        loadQueueData(); 

        getServletContext().setAttribute("walkInQueue", patientManager.getQueueAsArray());

        request.setAttribute("walkInQueue", patientManager.getQueueAsArray());
        request.setAttribute("patients", patientManager.getAllPatientsArray());
        request.setAttribute("consultRoom1", getServletContext().getAttribute("consultRoom1"));
        request.setAttribute("consultRoom2", getServletContext().getAttribute("consultRoom2"));

        request.getRequestDispatcher("admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        
        patientManager.clearPatientsOnly();
        loadPatientData();
        patientManager.clearQueue();
        loadQueueData();

        try {
            switch (action) {
                case "add":
                    handleAdd(req, res);
                    break;
                case "patientAdd":
                    handlePatientAdd(req, res);
                    break;    
                case "addToQueue":
                    handleAddToQueue(req, res);
                    break;
                case "search":
                    handleSearch(req, res);
                    break;
                case "update":
                    handleUpdate(req, res);
                    break;
                case "delete":
                    handleDelete(req, res);
                    break;
                case "serve":
                    handleServeNext(req, res);
                    break;
                case "endServe":
                    handleEndServe(req, res);
                    break;
                case "clear":
                    handleClearQueue(req, res);
                    break;
                case "clearSpecific":
                    handleClearSpecificPatient(req, res);
                    break;
                case "dailyReport":
                    handleDailyPatientReport(req, res);
                    break;
                case "queueTimeReport":
                    handleQueueTimeReport(req, res);
                    break;
                default:
                    req.setAttribute("message", "Unknown action: " + action);
                    req.getRequestDispatcher("admin.jsp").forward(req, res);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in doPost", e);
            req.setAttribute("message", "Error: " + e.getMessage());
            req.getRequestDispatcher("admin.jsp").forward(req, res);
        }
    }
    
    private void handleAdd(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String ic = validateRequiredField(req, "ic");
        String name = validateRequiredField(req, "name");
        String gender = validateRequiredField(req, "gender"); 
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String address = req.getParameter("address");
        LocalDate dob = parseDate(req.getParameter("dob"));

        Patient newPatient = new Patient(ic, name, gender, dob, phone, email, address, LocalDate.now());

        if (patientManager.searchPatientByIC(ic) != null) {
            req.setAttribute("message", "Patient with this IC already exists.");
            req.setAttribute("patients", patientManager.getAllPatientsArray());
            req.setAttribute("walkInQueue", patientManager.getQueueAsArray());
            req.getRequestDispatcher("admin.jsp").forward(req, res);
            return;
        }

        patientManager.addPatient(newPatient);
        patientManager.addToQueue(newPatient);  
        trackQueueTime(ic);                
        getServletContext().setAttribute("walkInQueue", patientManager.getQueueAsArray());
        savePatientData();
        saveQueueData(); 

        req.setAttribute("message", "Patient added successfully and added to queue.");
        req.setAttribute("patients", patientManager.getAllPatientsArray());
        req.setAttribute("walkInQueue", patientManager.getQueueAsArray());
        req.getRequestDispatcher("admin.jsp").forward(req, res);  
    }

    
    private void handlePatientAdd(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String ic = validateRequiredField(req, "ic");
        String name = validateRequiredField(req, "name");
        String gender = validateRequiredField(req, "gender"); 
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String address = req.getParameter("address");
        LocalDate dob = parseDate(req.getParameter("dob"));

        Patient existingPatient = patientManager.searchPatientByIC(ic);

        Patient patientToUse;

        if (existingPatient != null) {
            patientToUse = existingPatient;
            req.setAttribute("message", "Welcome back! You have been added to the queue.");
        } else {
            patientToUse = new Patient(ic, name, gender, dob, phone, email, address, LocalDate.now());
            patientManager.addPatient(patientToUse); 
            savePatientData(); 
            req.setAttribute("message", "Patient registered successfully and added to queue.");
        }

        if (!patientManager.isInQueue(ic)) {
            patientManager.addToQueue(patientToUse);
            trackQueueTime(ic);
            saveQueueData();
        } else {
            req.setAttribute("message", "You are already in the queue.");
        }

        getServletContext().setAttribute("walkInQueue", patientManager.getQueueAsArray());

        req.setAttribute("newPatient", patientToUse);
        req.setAttribute("walkInQueue", patientManager.getQueueAsArray());
        req.getRequestDispatcher("appointmentSummary.jsp").forward(req, res);
    }
    
    private void handleAddToQueue(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String ic = validateRequiredField(req, "ic");

        Patient patient = patientManager.searchPatientByIC(ic);
        if (patient == null) {
            req.setAttribute("message", "No patient found with IC: " + ic);
        } else {
            if (!patientManager.isInQueue(ic)) {
                patientManager.addToQueue(patient);
                req.setAttribute("message", "Patient added to queue.");
                saveQueueData(); 
                trackQueueTime(patient.getIcNum());
            } else {
                req.setAttribute("message", "Patient already in queue.");
            }
        }

        req.setAttribute("patients", patientManager.getAllPatientsArray());
        req.setAttribute("walkInQueue", patientManager.getQueueAsArray());
        req.getRequestDispatcher("admin.jsp").forward(req, res);
    }
    
    private void handleSearch(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String icSearch = validateRequiredField(req, "icSearch");
        Patient found = patientManager.searchPatientByIC(icSearch);

        if (found != null) {
            req.setAttribute("searchResult", found);
        } else {
            req.setAttribute("message", "No patient found with IC: " + icSearch);
        }

        req.setAttribute("patients", patientManager.getAllPatientsArray());
        req.setAttribute("walkInQueue", patientManager.getQueueAsArray());
        req.getRequestDispatcher("admin.jsp").forward(req, res);
    }
    
    private void handleUpdate(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String ic = validateRequiredField(req, "ic");
        String name = validateRequiredField(req, "name");
        String gender = validateRequiredField(req, "gender"); 
        LocalDate dob = parseDate(req.getParameter("dob"));
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String address = req.getParameter("address");

        Patient existing = patientManager.searchPatientByIC(ic);

        if (existing == null) {
            req.setAttribute("message", "No patient found with IC: " + ic);
        } else {
            existing.setName(name);
            existing.setGender(gender);
            existing.setDateOfBirth(dob);
            existing.setPhoneNum(phone);
            existing.setEmail(email);
            existing.setAddress(address);

            savePatientData(); 
            req.setAttribute("message", "Patient information updated successfully.");
        }

        req.setAttribute("patients", patientManager.getAllPatientsArray());
        req.setAttribute("walkInQueue", patientManager.getQueueAsArray());
        req.getRequestDispatcher("admin.jsp").forward(req, res);
    }

    
    private void handleDelete(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String ic = validateRequiredField(req, "ic");

        boolean removedFromPatients = patientManager.removePatientByIC(ic); 
        boolean removedFromQueue = patientManager.removeFromQueueByIC(ic);  

        if (removedFromPatients) {
            savePatientData();   
            saveQueueData();
            getServletContext().setAttribute("walkInQueue", patientManager.getQueueAsArray());

            req.setAttribute("message", "Patient deleted successfully.");
        } else {
            req.setAttribute("message", "Patient not found for deletion.");
        }

        req.setAttribute("patients", patientManager.getAllPatientsArray());
        req.setAttribute("walkInQueue", patientManager.getQueueAsArray());
        req.getRequestDispatcher("admin.jsp").forward(req, res);
    }

        private void handleServeNext(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String consultRoom = req.getParameter("consultRoom");
        String indexStr = req.getParameter("queueNumber");

        getServletContext().setAttribute("walkInQueue", patientManager.getQueueAsArray()); 

        int indexToServe = -1;
        try {
            if (indexStr != null && !indexStr.trim().isEmpty()) {
                indexToServe = Integer.parseInt(indexStr.trim()) - 1;
            }
        } catch (NumberFormatException e) {
            req.setAttribute("message", "Invalid queue number.");
            forwardWithConsultRoomData(req, res);
            return;
        }

        if (consultRoom == null || (!consultRoom.equals("consultRoom1") && !consultRoom.equals("consultRoom2"))) {
            req.setAttribute("message", "Please select a valid consultation room.");
            forwardWithConsultRoomData(req, res);
            return;
        }

        int queueFileSize = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(getDataFilePath("queue.txt")))) {
            while (br.readLine() != null) {
                queueFileSize++;
            }
        } catch (IOException e) {
            req.setAttribute("message", "Error reading queue file.");
            forwardWithConsultRoomData(req, res);
            return;
        }

        if (indexToServe < 0 || indexToServe >= queueFileSize) {
            req.setAttribute("message", "Queue number out of range. Total in queue: " + queueFileSize);
            forwardWithConsultRoomData(req, res);
            return;
        }
        
        System.out.println("Attempting to serve index: " + indexToServe);
        System.out.println("Queue size: " + patientManager.getQueueSize());

        Patient selectedPatient = patientManager.serveSpecificPatient(indexToServe); 

        if (selectedPatient != null) {
            if (consultRoom.equals("consultRoom1")) {
                Patient current = (Patient) getServletContext().getAttribute("consultRoom1");
                if (current != null) {
                    logServedPatient(current); 
                }
                getServletContext().setAttribute("consultRoom1", selectedPatient);
                getServletContext().setAttribute("walkInQueue", patientManager.getQueueAsArray());
                saveQueueData();

                Patient consultRoom2 = (Patient) getServletContext().getAttribute("consultRoom2");
                if (consultRoom2 != null && consultRoom2.getIcNum().equals(selectedPatient.getIcNum())) {
                    getServletContext().removeAttribute("consultRoom2");
                }

                req.setAttribute("message", "Consultation Room 1 is now serving: " + selectedPatient.getName());

            } else if (consultRoom.equals("consultRoom2")) {
                Patient current = (Patient) getServletContext().getAttribute("consultRoom2");
                if (current != null) {
                    logServedPatient(current); 
                }
                getServletContext().setAttribute("consultRoom2", selectedPatient);
                getServletContext().setAttribute("walkInQueue", patientManager.getQueueAsArray());
                saveQueueData();

                Patient consultRoom1 = (Patient) getServletContext().getAttribute("consultRoom1");
                if (consultRoom1 != null && consultRoom1.getIcNum().equals(selectedPatient.getIcNum())) {
                    getServletContext().removeAttribute("consultRoom1");
                }

                req.setAttribute("message", "Consultation Room 2 is now serving: " + selectedPatient.getName());
            }
        } else {
            req.setAttribute("message", "Unable to serve patient.");
        }

        forwardWithConsultRoomData(req, res);
    }
    
    private void handleEndServe(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String consultRoom = req.getParameter("consultRoom");

        if (consultRoom == null || (!consultRoom.equals("consultRoom1") && !consultRoom.equals("consultRoom2"))) {
            req.setAttribute("message", "Invalid consultation room specified.");
            forwardWithConsultRoomData(req, res);
            return;
        }

        if (consultRoom.equals("consultRoom1")) {
            Patient current = (Patient) getServletContext().getAttribute("consultRoom1");
            if (current != null) {
                logServedPatient(current); 
            }
            getServletContext().removeAttribute("consultRoom1");
            req.setAttribute("message", "Consultation Room 1 is now available.");
        } else {
            Patient current = (Patient) getServletContext().getAttribute("consultRoom2");
            if (current != null) {
                logServedPatient(current); 
            }
            getServletContext().removeAttribute("consultRoom2");
            req.setAttribute("message", "Consultation Room 2 is now available.");
        }

        forwardWithConsultRoomData(req, res);
    }
    
    private void handleClearQueue(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        patientManager.clearQueue(); 
        saveQueueData();  

        getServletContext().removeAttribute("consultRoom1");
        getServletContext().removeAttribute("consultRoom2");

        getServletContext().setAttribute("walkInQueue", patientManager.getQueueAsArray());

        req.setAttribute("message", "All patients have been cleared from the queue.");
        forwardWithConsultRoomData(req, res);
    }
    
    private void handleClearSpecificPatient(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String queueNumStr = req.getParameter("queueNumber");

        try {
            int index = Integer.parseInt(queueNumStr.trim()) - 1;

            Patient removed = patientManager.removeFromQueueByIndex(index);
            if (removed != null) {
                saveQueueData();
                getServletContext().setAttribute("walkInQueue", patientManager.getQueueAsArray());
                req.setAttribute("message", "Removed patient: " + removed.getName() + " from the queue.");
            } else {
                req.setAttribute("message", "Invalid queue number or patient not found.");
            }
        } catch (NumberFormatException e) {
            req.setAttribute("message", "Invalid queue number input.");
        }

        forwardWithConsultRoomData(req, res);
    }

    
    private void handleDailyPatientReport(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String filePath = getDataFilePath("served_records.txt");

        String dateStr = req.getParameter("reportDate");
        LocalDate selectedDate;
        try {
            selectedDate = (dateStr != null && !dateStr.isEmpty())
                    ? LocalDate.parse(dateStr)
                    : LocalDate.now();
        } catch (Exception e) {
            selectedDate = LocalDate.now(); 
        }

        ArrayList<String> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    LocalDateTime time = LocalDateTime.parse(parts[2].trim());
                    if (time.toLocalDate().equals(selectedDate)) {
                        records.add("[" + time.toLocalTime() + "] " + parts[1].trim() + " (IC: " + parts[0].trim() + ")");
                    }
                }
            }
        }

        req.setAttribute("dailyReport", records);
        req.setAttribute("reportDate", selectedDate.toString()); 
        req.getRequestDispatcher("dailyReport.jsp").forward(req, res);
    }
    
    private void handleQueueTimeReport(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String filePath = getDataFilePath("queue_times.txt");

        String dateStr = req.getParameter("reportDate");
        LocalDate selectedDate;
        try {
            selectedDate = (dateStr != null && !dateStr.isEmpty()) ? LocalDate.parse(dateStr) : LocalDate.now();
        } catch (Exception e) {
            selectedDate = LocalDate.now();
        }

        int totalMinutes = 0;
        int totalCount = 0;

        int[] minuteSumPerHour = new int[24];
        int[] countPerHour = new int[24];

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    LocalDate date = LocalDate.parse(parts[2].trim());
                    if (date.equals(selectedDate)) {
                        String queueTimeStr = parts[3].trim();
                        String durationStr = parts[5].trim();

                        int hour = Integer.parseInt(queueTimeStr.split("T")[1].split(":")[0]);
                        int minutes = Integer.parseInt(durationStr.split(" ")[0]);

                        totalMinutes += minutes;
                        totalCount++;

                        minuteSumPerHour[hour] += minutes;
                        countPerHour[hour]++;
                    }
                }
            }
        }

        double overallAvg = totalCount > 0 ? (double) totalMinutes / totalCount : 0;

        ArrayList<QueueAverage> hourlyAverages = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (countPerHour[i] > 0) {
                double avg = (double) minuteSumPerHour[i] / countPerHour[i];
                String label = String.format("%02d:00-%02d:00", i, i + 1);
                hourlyAverages.add(new QueueAverage(label, avg));
            }
        }

        req.setAttribute("overallAvg", overallAvg);
        req.setAttribute("periodAverages", hourlyAverages);
        req.setAttribute("reportDate", selectedDate.toString()); 

        req.getRequestDispatcher("queueTimeReport.jsp").forward(req, res);
    }

    private void forwardWithConsultRoomData(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("consultRoom1", getServletContext().getAttribute("consultRoom1"));
        req.setAttribute("consultRoom2", getServletContext().getAttribute("consultRoom2"));
        req.setAttribute("patients", patientManager.getAllPatientsArray());
        req.setAttribute("walkInQueue", patientManager.getQueueAsArray());
        req.getRequestDispatcher("admin.jsp").forward(req, res);
    }

    private void logServedPatient(Patient patient) {
        String now = java.time.LocalDateTime.now().toString();
        String recordFile = getDataFilePath("served_records.txt");
        String queueFile = getDataFilePath("queue_times.txt");

        try (
            BufferedWriter bw = new BufferedWriter(new FileWriter(recordFile, true));
            BufferedWriter qw = new BufferedWriter(new FileWriter(queueFile, true))
        ) {
            bw.write(String.join(", ",
                patient.getIcNum(),
                patient.getName(),
                now
            ));
            bw.newLine();


            LocalDateTime queuedAt = null;
            int foundIndex = -1;
            for (int i = 0; i < queueTimeCount; i++) {
                if (queueTimeRecords[i].getIcNum().equalsIgnoreCase(patient.getIcNum())) {
                    queuedAt = queueTimeRecords[i].getQueueTime();
                    foundIndex = i;
                    break;
                }
            }

            if (queuedAt != null) {
                java.time.Duration duration = java.time.Duration.between(queuedAt, java.time.LocalDateTime.now());
                qw.write(String.join(",",
                    patient.getIcNum(),
                    patient.getName(),
                    java.time.LocalDate.now().toString(),
                    queuedAt.toString(),
                    now,
                    duration.toMinutes() + " minutes"
                ));
                qw.newLine();

                for (int i = foundIndex; i < queueTimeCount - 1; i++) {
                    queueTimeRecords[i] = queueTimeRecords[i + 1];
                }
                queueTimeRecords[--queueTimeCount] = null;
            }

        } catch (IOException e) {
            LOGGER.warning("Unable to write to logs: " + e.getMessage());
        }
    }
    
    private void trackQueueTime(String icNum) {
        for (int i = 0; i < queueTimeCount; i++) {
            if (queueTimeRecords[i].getIcNum().equalsIgnoreCase(icNum)) {
                return; 
            }
        }

        if (queueTimeCount < queueTimeRecords.length) {
            queueTimeRecords[queueTimeCount++] = new PatientQueueTime(icNum, java.time.LocalDateTime.now());
        }
    }

    private String validateRequiredField(HttpServletRequest req, String field) throws ServletException {
        String value = req.getParameter(field);
        if (value == null || value.trim().isEmpty()) {
            throw new ServletException("Missing required field: " + field);
        }
        return value.trim();
    }

    private void savePatientData() throws IOException {
        String patientFile = getDataFilePath("patients.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(patientFile, false))) { 
            for (Patient p : patientManager.getAllPatientsArray()) {
                bw.write(String.join(",",
                    p.getIcNum(), 
                    p.getName(), 
                    p.getGender(), 
                    p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : "",
                    p.getPhoneNum() != null ? p.getPhoneNum() : "",
                    p.getEmail() != null ? p.getEmail() : "",
                    p.getAddress() != null ? p.getAddress() : "",
                    p.getDateRegistered() != null ? p.getDateRegistered().toString() : ""
                ));
                bw.newLine();
            }
        }
    }
    
    private void saveQueueData() throws IOException {
        String queueFile = getDataFilePath("queue.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(queueFile, false))) { 
            Patient[] queue = patientManager.getQueueAsArray();
            for (int i = 0; i < queue.length; i++) {
                Patient p = queue[i];
                int queueNumber = i + 1; 
                bw.write(queueNumber + "," + p.getIcNum() + "," + p.getName());
                bw.newLine();
            }
        }
    }
}