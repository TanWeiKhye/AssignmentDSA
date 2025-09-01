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
import static java.lang.System.out;
import java.util.Optional;

/**
 *
 * @author Cham Voon Loong
 */
public class MaintainTreatmentServlet extends HttpServlet {
    
    private Treatment treatment;
    private TreeInterface<Treatment> treatmentRecords;
    
    
    @Override
    public void init() throws ServletException {
        super.init();

        // Initialize the AVL tree
        treatmentRecords = new AVLTree<>();

        treatmentRecords.insert(new Treatment("Rest"));
        treatmentRecords.insert(new Treatment("Hydration"));
        treatmentRecords.insert(new Treatment("Avoid Stress"));
        treatmentRecords.insert(new Treatment("Ice compression"));
        treatmentRecords.insert(new Treatment("Blant diet"));

        // Store in ServletContext for global accessibility
        getServletContext().setAttribute("treatmentRecords", treatmentRecords);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check the session for special display records (e.g., from a search)
        TreeInterface<Treatment> displayRecords = 
                (TreeInterface<Treatment>) request.getSession().getAttribute("displayRecords");

        if (displayRecords != null) {
            // If we found search results in the session, use them for display
            request.setAttribute("treatmentRecords", displayRecords);

            // IMPORTANT: Remove the attribute from the session so the full list appears on the next refresh
            request.getSession().removeAttribute("displayRecords");
        } else {
            // Otherwise, show the full list from the ServletContext
            request.setAttribute("treatmentRecords", getServletContext().getAttribute("treatmentRecords"));
        }

        // Also, pass along any search message and remove it from the session
        if (request.getSession().getAttribute("searchMessage") != null) {
            request.setAttribute("searchMessage", request.getSession().getAttribute("searchMessage"));
            request.getSession().removeAttribute("searchMessage");
        }

        // Forward to the JSP for display
        request.getRequestDispatcher("treatment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        TreeInterface<Treatment> treatmentRecords =
            (TreeInterface<Treatment>) getServletContext().getAttribute("treatmentRecords");
     
        System.out.println(request.getParameter("deleteTreatment"));
        
        try{
            String action = request.getParameter("action");
            
            switch(action){
                case "add":
                    add(request, response);
                    break;
                case "update":
                    update(request, response);
                    break;
                case "delete":
                    delete(request, response);
                    break;
                case "search":
                    search(request, response);
                    break;
            }

            response.sendRedirect(request.getContextPath() + "/MaintainTreatmentServlet");
        
        }catch (Exception e){
            // If ANY error happens in the block above, it will be caught here
            System.out.println("\n!!!!!! CRITICAL ERROR FORWARDING TO JSP !!!!!!");
            e.printStackTrace(); // This prints the full error to your server console
            System.out.println("!!!!!! END OF ERROR !!!!!!");
        }

    }
    
    private void add(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String newRecord = request.getParameter("addTreatment");
        treatmentRecords.insert(new Treatment(newRecord));
    }
    
    private void update(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String original = request.getParameter("originalTreatment");
        String updated = request.getParameter("editTreatment");

        if(original != null && updated != null){

            treatmentRecords.delete(new Treatment(original));
            treatmentRecords.insert(new Treatment(updated));
        }
    }
    
    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String value = request.getParameter("deleteTreatment");
        treatmentRecords.delete(new Treatment(value));
    }
    
    private void search(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String searchQuery = request.getParameter("searchTreatment");
    
        Treatment searchResult = treatmentRecords.search(new Treatment(searchQuery));

        if (searchResult != null) {
            
            TreeInterface<Treatment> resultList = new AVLTree<>();
            resultList.insert(searchResult);

            // Store in session
            request.getSession().setAttribute("displayRecords", resultList);
        } else {
            request.getSession().setAttribute("displayRecords", new AVLTree<Treatment>());
            request.getSession().setAttribute("searchMessage", "Treatment '" + searchQuery + "' not found.");
        }
    }
}

