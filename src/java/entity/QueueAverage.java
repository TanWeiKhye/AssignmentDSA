/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author hongj
 */
public class QueueAverage {
    private String timePeriod;  
    private double averageTimeInMinutes;

    public QueueAverage(String timePeriod, double averageTimeInMinutes) {
        this.timePeriod = timePeriod;
        this.averageTimeInMinutes = averageTimeInMinutes;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public double getAverageTimeInMinutes() {
        return averageTimeInMinutes;
    }
}
