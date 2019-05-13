package com.example.fatcalapplication.Entity;

public class Consumption {
    private Integer consumptionid;
    private String consumptiondate;
    private int quantityservings;
    private Appuser userid;
    private Food foodid;
    public Consumption() {
    }

    public Consumption(Integer consumptionid) {
        this.consumptionid = consumptionid;
    }

    public Consumption(Integer consumptionid, String consumptiondate, int quantityservings) {
        this.consumptionid = consumptionid;
        this.consumptiondate = consumptiondate;
        this.quantityservings = quantityservings;
    }

    public Integer getConsumptionid() {
        return consumptionid;
    }

    public void setConsumptionid(Integer consumptionid) {
        this.consumptionid = consumptionid;
    }

    public String getConsumptiondate() {
        return consumptiondate;
    }

    public void setConsumptiondate(String consumptiondate) {
        this.consumptiondate = consumptiondate;
    }

    public int getQuantityservings() {
        return quantityservings;
    }

    public void setQuantityservings(int quantityservings) {
        this.quantityservings = quantityservings;
    }

    public Appuser getUserid() {
        return userid;
    }

    public void setUserid(Appuser userid) {
        this.userid = userid;
    }

    public Food getFoodid() {
        return foodid;
    }

    public void setFoodid(Food foodid) {
        this.foodid = foodid;
    }

}
