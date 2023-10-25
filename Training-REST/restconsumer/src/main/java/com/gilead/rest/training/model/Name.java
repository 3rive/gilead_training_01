package com.gilead.rest.training.model;

public class Name {

    private String first;
    private String last;
    private String title;

    public String getFirst() {
        return first;
    }

    public void setFirst(String firstName) {
        first = firstName;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String lastName) {
        last = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(title).append(" ").append(first)
                .append(" ").append(last);
        return sb.toString();
    }
}
