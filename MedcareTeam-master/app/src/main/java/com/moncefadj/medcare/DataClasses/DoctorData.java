package com.moncefadj.medcare.DataClasses;

public class DoctorData {

    private String id;
    private String name;
    private String email;
    private String password;
    private String key;
    private String phone;
    private String specialty;
    private String fullSpecialty;
    private String address;
    private String rate;
    private String desc;
    private String profileImg;


    public DoctorData() {
    }

    public DoctorData(String id, String name, String email, String password, String key, String phone, String specialty, String fullSpecialty, String address, String rate, String desc, String profileImg) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.key = key;
        this.phone = phone;
        this.specialty = specialty;
        this.fullSpecialty = fullSpecialty;
        this.address = address;
        this.rate = rate;
        this.desc = desc;
        this.profileImg = profileImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getFullSpecialty() {
        return fullSpecialty;
    }

    public void setFullSpecialty(String fullSpecialty) {
        this.fullSpecialty = fullSpecialty;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
