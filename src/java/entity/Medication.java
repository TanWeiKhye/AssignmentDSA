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

public class Medication implements Comparable<Medication> {
    
    //Data Field
    private String medication;
    private int    qty;
    
    //Constructors
    public Medication(){
        
    }
    
    public Medication(String medication, int qty){
           this.medication = medication;
           this.qty        = qty;
    }
    
    //Getter
    public String getMedication(){
        return this.medication;
    }
    
    public int getQty(){
        return this.qty;
    }
    
    //Setter
    public void setMedication(String medication){
        this.medication = medication;
    }
    
    public void setQty(int qty){
        this.qty = qty;
    }
    
    //toString
    @Override
    public String toString(){
        return this.medication;
    }
    
    //equals
    @Override
    public boolean equals(Object obj) {
        
        //Check if is same reference
        if (this == obj) return true;
        
        //Check if is null or different class
        if (obj == null || getClass() != obj.getClass()) return false;
        
        //Casting then compare values
        Medication medication = (Medication) obj;                   
        return Objects.equals(this.medication, medication.getMedication());  
    }
    
    //hashCode()
    @Override
    public int hashCode(){
        return Objects.hash(medication);
    }
    
    @Override
    public int compareTo(Medication other) {
        return this.medication.compareToIgnoreCase(other.medication);
    }
}
