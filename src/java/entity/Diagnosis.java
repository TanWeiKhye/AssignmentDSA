package entity;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Cham Voon Loong
 */

import java.util.Objects;
import entity.Treatment;
import adt.TreeInterface;
import adt.AVLTree;

public class Diagnosis implements Comparable<Diagnosis> {
    
    //Data Fields
    private String diagnosis;
    private TreeInterface<Treatment> treatment;
   
    
    //Constructors
    public Diagnosis(){
        treatment  = new AVLTree();
       
    }
    
    public Diagnosis(String diagnosis){
        this.diagnosis = diagnosis;
    }
    
    public Diagnosis(String diagnosis, AVLTree<Treatment> treatmeent){
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }
    
    //Getters
    public String getDiagnosis(){
        return this.diagnosis;
    }
    
    public AVLTree<Treatment> getTreatment(){
        return (AVLTree)this.treatment;
    }
    
    
    //Setters
    public void setDiagnosis(String diagnosis){
        this.diagnosis = diagnosis;
    }
    
    public void setTreatment(Treatment treatment){
        this.treatment.insert(treatment);
    }
    
    //equals
    @Override
    public boolean equals(Object obj) {
        
        //Check if is same reference
        if (this == obj) return true;
        
        //Check if is null or different class
        if (obj == null || getClass() != obj.getClass()) return false;
        
        //Casting then compare values
        Diagnosis diagnosis = (Diagnosis) obj;                   
        return Objects.equals(this.diagnosis, diagnosis.getDiagnosis());  
    }
    
    //hashCode()
    @Override
    public int hashCode(){
        return Objects.hash(diagnosis);
    }
    
    @Override
    public int compareTo(Diagnosis other) {
        return this.diagnosis.compareToIgnoreCase(other.diagnosis);
    }
}
