package com.example.recyclerview;

public class Contact {
    int id;
    String firstName;
    String lastName;
    String number;
    String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Contact() {
    }

    public Contact(int id, String firstName, String lastName, String number) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
    }

    public Contact(String firstName, String lastName, String number) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


}
