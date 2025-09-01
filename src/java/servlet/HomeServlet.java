package servlet;

import adt.AVLTree;
import adt.ArrayList;
import adt.ListInterface;
import entity.Doctor;
import entity.Schedule;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

@WebServlet(name = "HomeServlet", urlPatterns = {""})
public class HomeServlet extends HttpServlet {
    
    private AVLTree<Doctor> doctors = new AVLTree<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void init() throws ServletException {
        doctors = new AVLTree<>();

        try {
            loadDoctorData();
        } catch (IOException e) {
            throw new ServletException("Failed to load doctor data", e);
        } catch (ParseException e) {
            throw new ServletException("Failed to parse the data", e);
        }

        System.out.println("Records initialized from file.");
    }

    private void loadDoctorData() throws IOException, ParseException {
        String filePath = getServletContext().getRealPath("/records/doctors.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentDoctorIc = null;
            
            while ((line = reader.readLine()) != null) {
                // Check for the separator between doctor records
                if (line.trim().isEmpty() || line.equals("---")) {
                    continue;
                }

                String[] data = line.split("\\|");

                if (data.length >= 6) {
                    String name = data[0];
                    String ic = data[1];
                    currentDoctorIc = ic; // Store for schedule creation
                    Date dateOB = new SimpleDateFormat("yyyy-MM-dd").parse(data[2]);
                    String phoneNumber = data[3];
                    String email = data[4];
                    String gender = data[5];

                    ListInterface<String> edu = new ArrayList<>();
                    ListInterface<Schedule> schedules = new ArrayList<>();

                    // Read education data
                    line = reader.readLine();
                    if (line != null && !line.equals("---")) {
                        String[] eduData = line.split("\\|");
                        for (String eduItem : eduData) {
                            if (eduItem != null && !eduItem.trim().isEmpty()) {
                                edu.add(eduItem.trim());
                            }
                        }
                    }

                    // Read schedules data
                    while ((line = reader.readLine()) != null && !line.equals("---")) {
                        if (line.trim().isEmpty()) continue;
                        
                        String[] scheduleData = line.split("\\|");
                        if (scheduleData.length == 5) {
                            Date scheduleDate = dateFormat.parse(scheduleData[0]);
                            String day = scheduleData[1];
                            String shift = scheduleData[2];
                            String location = scheduleData[3];
                            String status = scheduleData[4];
                            
                            // Create Schedule with String date
                            Schedule schedule = new Schedule(currentDoctorIc, scheduleDate, status, location);
                            schedules.add(schedule);
                        }
                    }

                    Doctor doctor = new Doctor(name, ic, dateOB, phoneNumber, email, gender, edu, schedules);
                    doctors.insert(doctor);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No doctor file found. It will be created.");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setAttribute("doctors", doctors);
        
        RequestDispatcher rd = request.getRequestDispatcher("boundry/home.jsp");
        rd.forward(request, response);
    }
}