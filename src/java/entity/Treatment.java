/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Cham Voon Loong
 */

import java.util.Objects;

public class Treatment implements Comparable<Treatment> {
    
    //Data Field
    private String treatment;
    
    //Constructor
    public Treatment(){
        
    }
    
    public Treatment(String treatment){
        this.treatment = treatment;
    }
    
    //Getter
    public String getTreatment(){
        return this.treatment;
    }
    
    //Setter
    public void setTreatment(String treatment){
        this.treatment = treatment;
    }
    
    //toString
    @Override
    public String toString(){
        return this.treatment;
    }
    
    //equals
    @Override
    public boolean equals(Object obj) {
        
        //Check if is same reference
        if (this == obj) return true;
        
        //Check if is null or different class
        if (obj == null || getClass() != obj.getClass()) return false;
        
        //Casting then compare values
        Treatment treatment = (Treatment) obj;                   
        return Objects.equals(this.treatment, treatment.getTreatment());  
    }
    
    //hashCode()
    @Override
    public int hashCode(){
        return Objects.hash(treatment);
    }
    
    @Override
    public int compareTo(Treatment other) {
        return this.treatment.compareToIgnoreCase(other.treatment);
    }
}
