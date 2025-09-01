/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import adt.TreeInterface;
import adt.AVLTree;

import entity.Treatment;
import entity.Diagnosis;

import servlet.MaintainTreatmentServlet;
import static java.lang.System.out;
import java.util.Optional;

/**
 *
 * @author Cham Voon Loong
 */

public class MaintainDiagnosisServlet extends HttpServlet {
    
    private TreeInterface<Treatment> allTreatments = new AVLTree<>();

    @Override
    public void init() throws ServletException {
      
        allTreatments.insert(new Treatment("Rest"));
        allTreatments.insert(new Treatment("Hydration"));
        allTreatments.insert(new Treatment("Ice Compression"));
        allTreatments.insert(new Treatment("Painkillers"));
        allTreatments.insert(new Treatment("Antibiotics"));
        
    }
    
  
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        

        request.setAttribute("allTreatments", allTreatments);
        
        request.getRequestDispatcher("diagnosis.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the single diagnosis value
        String diagnosis = request.getParameter("diagnosis");
        
        // Get the multiple treatment values.
        String[] selectedTreatments = request.getParameterValues("treatments");

        TreeInterface<Treatment> treatments = new AVLTree();
        
        for(int i = 0; i < selectedTreatments.length; i++){
            treatments.insert(new Treatment(selectedTreatments[i]));
        }
        
        Diagnosis diagnosisRecord = new Diagnosis(diagnosis, (AVLTree<Treatment>)treatments);

        // Here you would add logic to save this data to your master list or database.
        
        // Redirect back to the GET method to show the form again (Post-Redirect-Get pattern)
        response.sendRedirect("MaintainDiagnosisServlet");
    }
}
