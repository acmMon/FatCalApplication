package com.example.fatcalapplication.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class Appuser {
    private Integer userid;
    private String name;
    private String surname;
    private String email;
    private String dob;
    private BigDecimal height;
    private BigDecimal weight;
    private String gender;
    private String address;
    private String postcode;
    private Integer levelofactivity;
    private BigDecimal stepspermile;

    public Appuser() {
    }

    public Appuser(Integer userid) {
        this.userid = userid;
    }

    public Appuser(Integer userid, String name, String surname, String email, String dob, BigDecimal height, BigDecimal weight, String gender) {
        this.userid = userid;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Integer getLevelofactivity() {
        return levelofactivity;
    }

    public void setLevelofactivity(Integer levelofactivity) {
        this.levelofactivity = levelofactivity;
    }

    public BigDecimal getStepspermile() {
        return stepspermile;
    }

    public void setStepspermile(BigDecimal stepspermile) {
        this.stepspermile = stepspermile;
    }


}
