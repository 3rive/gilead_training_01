package com.gilead.rest.training.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
    private Name name;
    private Location location;
    private String email;
    private String gender;

    public void setName(Name name) {
        this.name = name;
    }

    public Name getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name)
                .append("Address: ").append(location)
                .append("Email ").append(email)
                .append("Gender: ").append(gender);
        return sb.toString();
    }
}
