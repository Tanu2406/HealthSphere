package com.mountreachsolutionpvtltd.healthsphere.Admin.POJOClass;

public class POJOViewAllCustomerDetails {
    String id,image,name,mobileno,emailid,address,username;
    double latitude,longitude;
    public POJOViewAllCustomerDetails(String id, String image, String name, String mobileno, String emailid, double latitude, double longitude, String address, String username) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.mobileno = mobileno;
        this.emailid = emailid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
