package com.example.recyclerview;

public class NewContact {
    int id;
    String name;
    String number;
    String email;
    String photo;
    String contactid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NewContact() {
    }

    public NewContact(int id, String name, String number, String email, String photo, String contactid) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.email = email;
        this.photo = photo;
        this.contactid = contactid;
    }

    public String getContactid() {
        return contactid;
    }

    public void setContactid(String contactid) {
        this.contactid = contactid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
