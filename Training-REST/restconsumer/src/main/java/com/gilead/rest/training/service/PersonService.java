package com.gilead.rest.training.service;

import com.gilead.rest.training.model.Person;
import com.gilead.rest.training.model.PersonList;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;


import java.util.List;

public class PersonService {

    private static WebTarget resource = ClientBuilder.newBuilder()
            .build().target("https://api.randomuser.me/");

    public static List<Person> fetchPeople(int offset, int num) {
        PersonList res = resource.queryParam("seed", 1)
                .queryParam("results", num).queryParam("page", 1)
                .request(MediaType.APPLICATION_JSON).get(PersonList.class);
        for(Person p : res.getResults()){
            System.out.println(p.getName());
        }
        return res.getResults();
    }
}


