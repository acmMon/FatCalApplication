package com.example.fatcalapplication.Entity;

import java.math.BigDecimal;

public class Food
{
    private Integer foodid;
    private String name;
    private String category;
    private BigDecimal calorieamount;
    private String servingunit;
    private BigDecimal servingamount;
    private BigDecimal fat;

    public Food() {
    }

    public Food(Integer foodid) {
        this.foodid = foodid;
    }

    public Food(Integer foodid, String name, String category) {
        this.foodid = foodid;
        this.name = name;
        this.category = category;
    }

    public Integer getFoodid() {
        return foodid;
    }

    public void setFoodid(Integer foodid) {
        this.foodid = foodid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getCalorieamount() {
        return calorieamount;
    }

    public void setCalorieamount(BigDecimal calorieamount) {
        this.calorieamount = calorieamount;
    }

    public String getServingunit() {
        return servingunit;
    }

    public void setServingunit(String servingunit) {
        this.servingunit = servingunit;
    }

    public BigDecimal getServingamount() {
        return servingamount;
    }

    public void setServingamount(BigDecimal servingamount) {
        this.servingamount = servingamount;
    }

    public BigDecimal getFat() {
        return fat;
    }

    public void setFat(BigDecimal fat) {
        this.fat = fat;
    }
}
