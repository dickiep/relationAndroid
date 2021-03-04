package com.dickies.android.relationbn.utils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Phil on 13/07/2018.
 */

public class Product {

    @SerializedName("title") private String name;
    @SerializedName("code")private String code;
    @SerializedName("location") private String location;
    @SerializedName("image") private String image;
    @SerializedName("id")private int id;
    @SerializedName("barcode")private String barcode;
    @SerializedName("stock")private int stock;


    public Product(String name, String code, String location, String image, int id, String barcode, int stock) {
        this.setBarcode(barcode);
        this.setImage(image);
        this.setName(name);
        this.setCode(code);
        this.setLocation(location);
        this.setId(id);
        this.setStock(stock);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
