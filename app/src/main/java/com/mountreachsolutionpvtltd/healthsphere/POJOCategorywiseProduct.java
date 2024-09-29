package com.mountreachsolutionpvtltd.healthsphere;

public class POJOCategorywiseProduct {

    String id,categoryname,medicalname,productcategory,productimage,productname,productprice,productrating,productoffer,productdescription;

    public POJOCategorywiseProduct(String id, String categoryname, String medicalname, String productcategory, String productimage, String productname, String productprice, String productrating, String productoffer, String productdescription) {
        this.id = id;
        this.categoryname = categoryname;
        this.medicalname = medicalname;
        this.productcategory = productcategory;
        this.productimage = productimage;
        this.productname = productname;
        this.productprice = productprice;
        this.productrating = productrating;
        this.productoffer = productoffer;
        this.productdescription = productdescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getMedicalname() {
        return medicalname;
    }

    public void setMedicalname(String medicalname) {
        this.medicalname = medicalname;
    }

    public String getProductcategory() {
        return productcategory;
    }

    public void setProductcategory(String productcategory) {
        this.productcategory = productcategory;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getProductrating() {
        return productrating;
    }

    public void setProductrating(String productrating) {
        this.productrating = productrating;
    }

    public String getProductoffer() {
        return productoffer;
    }

    public void setProductoffer(String productoffer) {
        this.productoffer = productoffer;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }
}
