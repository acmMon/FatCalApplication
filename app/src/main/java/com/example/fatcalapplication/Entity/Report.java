package com.example.fatcalapplication.Entity;

import java.math.BigDecimal;

public class Report {
    private Integer reportid;
    private String reportdate;
    private BigDecimal totalcalconsumed;
    private BigDecimal totalcalburned;
    private Integer totalstepstaken;
    private BigDecimal caloriegoal;
    private Appuser userid;

    public Report() {
    }

    public Report(Integer reportid) {
        this.reportid = reportid;
    }

    public Report(Integer reportid, String reportdate) {
        this.reportid = reportid;
        this.reportdate = reportdate;
    }

    public Integer getReportid() {
        return reportid;
    }

    public void setReportid(Integer reportid) {
        this.reportid = reportid;
    }

    public String getReportdate() {
        return reportdate;
    }

    public void setReportdate(String reportdate) {
        this.reportdate = reportdate;
    }

    public BigDecimal getTotalcalconsumed() {
        return totalcalconsumed;
    }

    public void setTotalcalconsumed(BigDecimal totalcalconsumed) {
        this.totalcalconsumed = totalcalconsumed;
    }

    public BigDecimal getTotalcalburned() {
        return totalcalburned;
    }

    public void setTotalcalburned(BigDecimal totalcalburned) {
        this.totalcalburned = totalcalburned;
    }

    public Integer getTotalstepstaken() {
        return totalstepstaken;
    }

    public void setTotalstepstaken(Integer totalstepstaken) {
        this.totalstepstaken = totalstepstaken;
    }

    public BigDecimal getCaloriegoal() {
        return caloriegoal;
    }

    public void setCaloriegoal(BigDecimal caloriegoal) {
        this.caloriegoal = caloriegoal;
    }

    public Appuser getUserid() {
        return userid;
    }

    public void setUserid(Appuser userid) {
        this.userid = userid;
    }
}
