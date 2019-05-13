package com.example.fatcalapplication;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import java.math.BigDecimal;

@Entity
public class UserSteps {
    @PrimaryKey(autoGenerate = true)
    public int usid;
    @ColumnInfo(name = "userid")
    public int userId;
    @ColumnInfo(name = "steps_date")
    public String stepsDate;
    @ColumnInfo(name = "steps_time")
    public String stepsTime;
    @ColumnInfo(name = "steps_taken")
    public String stepsTaken;
    public UserSteps(int userId, String stepsDate, String stepsTime, String stepsTaken) {
        this.userId = userId;
        this.stepsDate=stepsDate;
        this.stepsTime=stepsTime;
        this.stepsTaken=stepsTaken;
    }

    public int getId() {
        return usid;
    }

    public int getUserId()
    {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getStepsDate() {
        return stepsDate;
    }
    public void setStepsDate(String stepsDate) {
        this.stepsDate = stepsDate;
    }
    public String getStepsTime() {
        return stepsTime;
    }
    public void setStepsTime(String stepsTime) {
        this.stepsTime = stepsTime;
    }
    public String getStepsTaken() {
        return stepsTaken;
    }
    public void setStepsTaken(String stepsTaken) {
        this.stepsTaken = stepsTaken;
    }
}
