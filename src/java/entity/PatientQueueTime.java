/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author hongj
 */
import java.time.LocalDateTime;

public class PatientQueueTime {
    private String icNum;
    private LocalDateTime queueTime;

    public PatientQueueTime(String icNum, LocalDateTime queueTime) {
        this.icNum = icNum;
        this.queueTime = queueTime;
    }

    public String getIcNum() {
        return icNum;
    }

    public LocalDateTime getQueueTime() {
        return queueTime;
    }

    public void setQueueTime(LocalDateTime queueTime) {
        this.queueTime = queueTime;
    }
}
