package servlet;

import adt.AVLTree;
import adt.TreeInterface;
import adt.ArrayList;
import adt.ListInterface;
import entity.Doctor;
import entity.Schedule;
import entity.DoctorScheduleStats;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DoctorServlet", urlPatterns = {"/DoctorManagement"})
public class DoctorServlet extends HttpServlet {

    private static AVLTree<Doctor> doctors = new AVLTree<>();
    private static final String ABSOLUTE_FILE_PATH = "C:\\Users\\hongj\\OneDrive\\Documents\\NetBeansProjects\\AssignmentDSA\\web\\data\\doctors.txt";
    private static final String FILE_PATH = "C:\\Users\\hongj\\OneDrive\\Documents\\NetBeansProjects\\AssignmentDSA\\web\\data\\schedule.txt";

    @Override
    public void init() throws ServletException {
        try {
            loadDoctorData();
            generateNext7DaysScheduleForAllDoctors();
        } catch (IOException | ParseException e) {
            throw new ServletException("Failed to load doctor data", e);
        }

        System.out.println("Records initialized from file.");
    }

    private void loadDoctorData() throws IOException, ParseException {
        String filePath = ABSOLUTE_FILE_PATH;
        doctors = new AVLTree<>(); // reinitialize the list

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines and separators
                line = line.trim();
                if (line.isEmpty() || line.equals("---")) continue;

                // Read basic info
                String[] data = line.replaceAll("\\|+$", "").split("\\|"); // Remove trailing "|"
                if (data.length < 6) continue;

                String name = data[0];
                String ic = data[1];
                Date dateOB = new SimpleDateFormat("yyyy-MM-dd").parse(data[2]);
                String phoneNumber = data[3];
                String email = data[4];
                String gender = data[5];

                ListInterface<String> edu = new ArrayList<>();
                ListInterface<Schedule> schedules = new ArrayList<>();

                // Read education line
                line = reader.readLine();
                if (line != null && !line.trim().isEmpty() && !line.equals("---")) {
                    String[] eduData = line.replaceAll("\\|+$", "").split("\\|");
                    for (String eduItem : eduData) {
                        if (!eduItem.trim().isEmpty()) {
                            edu.add(eduItem.trim());
                        }
                    }
                }

                // Read schedule lines until "---"
                while ((line = reader.readLine()) != null && !line.trim().equals("---")) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    String[] scheduleData = line.replaceAll("\\|+$", "").split("\\|");
                    if (scheduleData.length == 4) {
                        Date scheduleDate = new SimpleDateFormat("yyyy-MM-dd").parse(scheduleData[0]);
                        String status = scheduleData[1];
                        String location = scheduleData[2];
                        String doctorIcFromFile = scheduleData[3]; // or assign based on doctor IC above
                        schedules.add(new Schedule(doctorIcFromFile, scheduleDate, status, location));
                    }
                }

                Doctor doctor = new Doctor(name, ic, dateOB, phoneNumber, email, gender, edu, schedules);
                doctors.insert(doctor);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Doctor file not found. It will be created upon saving.");
        }
    }

    private void saveDoctorData() {
        String filePath = ABSOLUTE_FILE_PATH;
        File file = new File(filePath);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Ensure directory exists
        File directory = file.getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Doctor doctor : doctors) {
                // Basic info
                writer.write(doctor.getName() + "|" +
                             doctor.getIc() + "|" +
                             dateFormat.format(doctor.getDateOB()) + "|" +
                             doctor.getPhoneNumber() + "|" +
                             doctor.getEmail() + "|" +
                             doctor.getGender());
                writer.newLine();

                // Education
                ListInterface<String> educations = doctor.getEdu();
                for (int i = 0; i < educations.size(); i++) {
                    writer.write(educations.get(i));
                    if (i < educations.size() - 1) {
                        writer.write("|");
                    }
                }
                writer.newLine();

                // Schedules
                ListInterface<Schedule> schedules = doctor.getSchedules();
                for (int i = 0; i < schedules.size(); i++) {
                    Schedule s = schedules.get(i);
                    writer.write(dateFormat.format(s.getDate()) + "|" +
                        s.getStatus() + "|" +
                        s.getLocation() + "|" +
                        s.getDoctorIc());
                    writer.newLine();
                }

                // Separator
                writer.write("---");
                writer.newLine();
            }

            System.out.println("Doctor data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving doctor data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void saveAllSchedulesToFile() {
        String scheduleFilePath = FILE_PATH;
        File file = new File(scheduleFilePath);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Doctor doctor : doctors) {
                ListInterface<Schedule> schedules = doctor.getSchedules();
                for (int i = 0; i < schedules.size(); i++) {
                    Schedule s = schedules.get(i);
                    writer.write(dateFormat.format(s.getDate()) + "|" +
                                 s.getStatus() + "|" +
                                 s.getLocation() + "|" +
                                 s.getDoctorIc());
                    writer.newLine();
                }
            }
            System.out.println("✅ schedule.txt generated successfully.");
        } catch (IOException e) {
            System.err.println("❌ Error saving schedule.txt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("report1".equals(action)) {
            String selectedDateStr = request.getParameter("reportDate");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date selectedDate = sdf.parse(selectedDateStr);

                ListInterface<Doctor> doctorList = new ArrayList<>();
                for (Doctor doc : doctors) {
                    doctorList.add(doc);
                }

                ListInterface<String> doctorNames = new ArrayList<>();
                ListInterface<String> doctorStatuses = new ArrayList<>();

                int availableCount = 0;
                int unavailableCount = 0;

                for (int i = 0; i < doctorList.size(); i++) {
                    Doctor doc = doctorList.get(i);
                    boolean found = false;
                    ListInterface<Schedule> schedules = doc.getSchedules();

                    for (int j = 0; j < schedules.size(); j++) {
                        Schedule s = schedules.get(j);
                        if (sdf.format(s.getDate()).equals(selectedDateStr)) {
                            doctorNames.add(doc.getName());
                            doctorStatuses.add(s.getStatus());

                            if (s.getStatus().equalsIgnoreCase("Available")) {
                                availableCount++;
                            } else {
                                unavailableCount++;
                            }

                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        doctorNames.add(doc.getName());
                        doctorStatuses.add("Unavailable");
                        unavailableCount++;
                    }
                }

                request.setAttribute("reportDate", selectedDateStr);
                request.setAttribute("doctorNames", doctorNames);
                request.setAttribute("doctorStatuses", doctorStatuses);
                request.setAttribute("availableCount", availableCount);
                request.setAttribute("unavailableCount", unavailableCount);

                request.getRequestDispatcher("boundry/doctorAvailabilityReport.jsp").forward(request, response);
                return;

            } catch (ParseException e) {
                request.setAttribute("error", "Invalid date format: " + selectedDateStr);
                request.setAttribute("doctors", doctors);
                request.getRequestDispatcher("boundry/doctor.jsp").forward(request, response);
                return;
            }
        }else if ("report2".equals(action)) {
            generateScheduleFrequencyReport(request, response);
            return;
        }

        // Default behavior (no action or unknown action)
        request.setAttribute("doctors", doctors);
        request.getRequestDispatcher("boundry/doctor.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String name = request.getParameter("name");
            String ic = request.getParameter("ic");
            String dobStr = request.getParameter("dob");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String gender = request.getParameter("gender");
            String[] educationArray = request.getParameterValues("education");

            try {
                Date dateOB = new SimpleDateFormat("yyyy-MM-dd").parse(dobStr);
                ListInterface<String> edu = new ArrayList<>();
                if (educationArray != null) {
                    for (String eduEntry : educationArray) {
                        if (eduEntry != null && !eduEntry.trim().isEmpty()) {
                            edu.add(eduEntry.trim());
                        }
                    }
                }
                
                

                ListInterface<Schedule> schedules = new ArrayList<>();
                Doctor newDoctor = new Doctor(name, ic, dateOB, phone, email, gender, edu, schedules);

                Doctor existing = doctors.search(new Doctor(ic)); // assumes compareTo uses IC

                if (existing == null) {
                    doctors.insert(newDoctor);
                    generateNext7DaysScheduleForAllDoctors();
                    saveDoctorData();
                    request.setAttribute("message", "Doctor added successfully!");
                } else {
                    request.setAttribute("error", "A doctor with this IC already exists.");
                }

                request.setAttribute("message", "Doctor added successfully!");
            } catch (ParseException e) {
                request.setAttribute("error", "Invalid date format.");
            }
        }else if ("edit".equals(action)) {
            String originalIc = request.getParameter("originalIc");
            Doctor existing = doctors.search(new Doctor(originalIc));

            if (existing != null) {
                String name = request.getParameter("name");
                String dobStr = request.getParameter("dob");
                String phone = request.getParameter("phone");
                String email = request.getParameter("email");
                String gender = request.getParameter("gender");
                String[] educationArray = request.getParameterValues("education");

                try {
                    Date dateOB = new SimpleDateFormat("yyyy-MM-dd").parse(dobStr);
                    ListInterface<String> edu = new ArrayList<>();
                    if (educationArray != null) {
                        for (String eduEntry : educationArray) {
                            if (eduEntry != null && !eduEntry.trim().isEmpty()) {
                                edu.add(eduEntry.trim());
                            }
                        }
                    }

                    // Create new doctor object with same IC
                    Doctor updatedDoctor = new Doctor(name, originalIc, dateOB, phone, email, gender, edu, existing.getSchedules());

                    // Replace the doctor in the AVL Tree
                    doctors.delete(existing);
                    doctors.insert(updatedDoctor);
                    saveDoctorData();

                    request.setAttribute("message", "Doctor updated successfully!");
                } catch (ParseException e) {
                    request.setAttribute("error", "Invalid date format.");
                }
            }
        }else if ("makeAvailable".equals(action)) {
            String ic = request.getParameter("ic");
            String dateStr = request.getParameter("scheduleDate");
            String status = request.getParameter("status");

            if (ic != null && dateStr != null && status != null &&
                !ic.isEmpty() && !dateStr.isEmpty() && !status.isEmpty()) {

                Doctor doctor = doctors.search(new Doctor(ic));

                if (doctor != null) {
                    try {
                        // ✅ Parse the string to Date object
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = sdf.parse(dateStr);

                        // ✅ Now pass Date to Schedule constructor
                        Schedule newSchedule = new Schedule(ic, date, status, "Clinic Room");

                        // Check if doctor already has a schedule for the date
                        boolean updated = false;
                        for (int i = 0; i < doctor.getSchedules().size(); i++) {
                            Schedule s = doctor.getSchedules().get(i);
                            if (sdf.format(s.getDate()).equals(dateStr)) {
                                doctor.getSchedules().replace(i, newSchedule);
                                updated = true;
                                break;
                            }
                        }

                        if (!updated) {
                            doctor.getSchedules().add(newSchedule);
                        }

                        saveDoctorData(); // Persist the changes

                    } catch (ParseException e) {
                        e.printStackTrace();
                        request.setAttribute("error", "Invalid date format.");
                    }
                }
            }

            response.sendRedirect("DoctorManagement?ic=" + ic);
        }
    }
    
    private void generateNext7DaysScheduleForAllDoctors() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        long oneDayMillis = 24 * 60 * 60 * 1000;

        for (Doctor doctor : doctors) {
            ListInterface<Schedule> schedules = doctor.getSchedules();

            for (int i = 0; i < 7; i++) {
                Date futureDate = new Date(today.getTime() + i * oneDayMillis);

                boolean alreadyScheduled = false;
                for (int j = 0; j < schedules.size(); j++) {
                    if (dateFormat.format(schedules.get(j).getDate()).equals(dateFormat.format(futureDate))) {
                        alreadyScheduled = true;
                        break;
                    }
                }

                if (!alreadyScheduled) {
                    Schedule schedule = new Schedule(
                            doctor.getIc(),
                            futureDate,
                            "Available",
                            "Main Clinic"
                    );
                    schedules.add(schedule);
                }
            }
        }

        // After all 7-day schedules are generated
        saveAllSchedulesToFile();
    }
    private void generateScheduleFrequencyReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (doctors == null || doctors.isEmpty()) {
        request.setAttribute("message", "No doctors found in the system.");
        request.getRequestDispatcher("boundry/doctorScheduleReport.jsp").forward(request, response);
        return;
    }

    ListInterface<DoctorScheduleStats> reportData = new ArrayList<>();

    for (int i = 0; i < doctors.size(); i++) {
        Doctor doctor = doctors.get(i);
        if (doctor == null) continue;

        ListInterface<Schedule> schedules = doctor.getSchedules();
        int total = schedules != null ? schedules.size() : 0;
        int available = 0;
        int unavailable = 0;

        if (schedules != null) {
            for (int j = 0; j < schedules.size(); j++) {
                Schedule s = schedules.get(j);
                if (s != null) {
                    if ("Available".equalsIgnoreCase(s.getStatus())) available++;
                    else unavailable++;
                }
            }
        }

        double percentage = total > 0 ? (available * 100.0 / total) : 0;

        // Create DoctorScheduleStats with correct constructor
        reportData.add(new DoctorScheduleStats(doctor.getName(), total, available, unavailable, percentage));
    }

    request.setAttribute("reportData", reportData);
    request.getRequestDispatcher("boundry/doctorScheduleReport.jsp").forward(request, response);
}
}