package com.example.creditservice;

public class Debt {


    private int id ;
    private String productLabel,productImage,productStatusCreated,getProductStatusUpdated;
    private int productDebt ;


    public Debt(int id, String productLabel, int productDebt , String productImage, String productStatusCreated,String getProductStatusUpdated) {
        this.id = id;
        this.productLabel = productLabel;
        this.productDebt = productDebt;
        this.productStatusCreated = productStatusCreated;
        this.getProductStatusUpdated = getProductStatusUpdated;
        this.productImage=productImage;
    }

    public int getId() {
        return id;
    }

    public String getProductLabel() {
        return productLabel;
    }

    public int getProductDebt() {
        return productDebt;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductStatusCreated() {
        return productStatusCreated;
    }

    public String getGetProductStatusUpdated() {
        return getProductStatusUpdated;
    }
}
