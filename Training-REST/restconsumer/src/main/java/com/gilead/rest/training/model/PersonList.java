package com.gilead.rest.training.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonList {
    private List<Person> results;

    public List<Person> getResults() {
        return results;
    }

    public void setResults(List<Person> result) {
        results = result;
    }
}
