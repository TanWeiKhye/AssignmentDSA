/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import adt.AVLTree;
import adt.ArrayList;
import adt.TreeInterface;
import entity.Diagnosis;
import entity.Treatment;
import java.io.IOException;
import java.io.PrintWriter;
import adt.ListInterface;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.Iterable;

/**
 *
 * @author Cham Voon Loong
 */
public class MedicalModuleReportServlet extends HttpServlet {
    
    
    @Override
    public void init() throws ServletException{
        super.init();
        
        ListInterface<Treatment> initData = new ArrayList();
        ListInterface<Diagnosis> initDiagnosis = new ArrayList();
        
        initData.add(new Treatment("Rest"));
        initData.add(new Treatment("Hydration"));
        initData.add(new Treatment("Avoid Stress"));
        initData.add(new Treatment("Ice compression"));
        initData.add(new Treatment("Blant diet"));
        
        initDiagnosis.add(new Diagnosis("Common Cold"));
        initDiagnosis.add(new Diagnosis("Fever"));
        initDiagnosis.add(new Diagnosis("Stomach Ache"));
        initDiagnosis.add(new Diagnosis("Not Feeling Well"));
        initDiagnosis.add(new Diagnosis("Fatugue"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String reportType = request.getParameter("report");

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
         
        // Create the sample data provided by the user
        ListInterface<Treatment> initData = new ArrayList<>();
        initData.add(new Treatment("Rest"));
        initData.add(new Treatment("Hydration"));
        initData.add(new Treatment("Avoid Stress"));
        initData.add(new Treatment("Ice compression"));
        initData.add(new Treatment("Blant diet"));
        
        ListInterface<String> initDiagnosis = new ArrayList<>();
        initDiagnosis.add("Common Cold");
        initDiagnosis.add("Fever");
        initDiagnosis.add("Stomach Ache");
        initDiagnosis.add("Not Feeling Well");
        initDiagnosis.add("Fatugue");
        
        out.println("<h2>Simple Item Lists Report</h2>");
        
        out.println("<h3>Available Treatments:</h3>");
        out.println("<ul>");
        for (int i = 0; i < initData.size(); i++) {
            out.println("<li>" + initData.get(i) + "</li>");
        }
        out.println("</ul>");

        out.println("<h3>Common Diagnoses:</h3>");
        out.println("<ul>");
        for (int i = 0; i < initDiagnosis.size(); i++) {
            out.println("<li>" + initDiagnosis.get(i) + "</li>");
        }
        out.println("</ul></body></html>");
      
        }
    }}



