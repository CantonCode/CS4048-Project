package com.Rental;

import java.util.ArrayList;

import java.util.List;

public class Equipment {
    private String equipmentId;
    private String equipmentName;
    private ArrayList<String> description;
    private String size;
    private boolean rented;
    private String imageUrl;

    public Equipment(){
        //blank constructor
    }

    public Equipment(String equipmentId,String equipmentName, ArrayList<String> description, String size, boolean rented, String imageUrl){
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.description = description;
        this.size = size;
        this.rented = rented;
        this.imageUrl = imageUrl;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<String> description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
