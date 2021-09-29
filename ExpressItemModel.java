package com.shopravis.ravisrunner.Model;

import java.io.Serializable;

public class ExpressItemModel implements Serializable {
    private Boolean isPickedUp;

    private String name;
    private String aisle;
    private String category;
    private String packsize;
    private String size1;
    private String size2;
    private String size3;
    private String total_cost;
    private String unit_type;

    private double price;

    private int part_number,
            quantity;

    public Boolean getPickedUp() {
        return isPickedUp;
    }

    public void setPickedUp(Boolean pickedUp) {
        isPickedUp = pickedUp;
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

    public String getAisle() {
        return aisle;
    }

    public void setSetAisle(String aisle) {
        this.aisle = aisle;
    }

    public String getUnit_type() {
        return unit_type;
    }

    public void setUnit_type(String unit_type) {
        this.unit_type = unit_type;
    }

    public int getPart_number() {
        return part_number;
    }

    public void setPart_number(int part_number) {
        this.part_number = part_number;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPackSize() {
        return packsize;
    }

    public void setPackSize(String packsize) {
        this.packsize = packsize;
    }

    public String getSize1() {
        return size1;
    }

    public void setSize1(String size1) {
        this.size1 = size1;
    }

    public String getSize2() {
        return size2;
    }

    public void setSize2(String size2) {
        this.size2 = size2;
    }

    public String getSize3() {
        return size3;
    }

    public void setSize3(String size3) {
        this.size3 = size3;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

