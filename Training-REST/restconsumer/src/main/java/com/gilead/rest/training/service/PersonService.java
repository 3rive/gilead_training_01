package com.gilead.rest.training.service;

import com.gilead.rest.training.dao.PersonDao;
import com.gilead.rest.training.dto.PersonDTO;
import com.gilead.rest.training.model.Person;
import com.gilead.rest.training.model.PersonList;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PersonService {

    private static WebTarget resource = ClientBuilder.newBuilder()
            .build().target("https://api.randomuser.me/");

    public static void loadPeople(int offset, int num) throws ParseException {
        List<Person> personList = new ArrayList<Person>();
        List<PersonDTO> personDTOs = new ArrayList<PersonDTO>();
        PersonList res = resource.queryParam("seed", offset)
                .queryParam("results", num).queryParam("page", 10)
                .request(MediaType.APPLICATION_JSON).get(PersonList.class);
        if(res.getResults() !=null){
            personList = res.getResults();
            for (Person person : personList){
                PersonDTO personDTO = getPersonDTO(person);
                personDTOs.add(personDTO);
            }

        }
        PersonDao personDao = new PersonDao();
        for (PersonDTO person : personDTOs){
            personDao.insert(person);
        }

    }

    private static PersonDTO getPersonDTO(Person person) throws ParseException {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId().getValue());
        personDTO.setFirstname(person.getName().getFirst());
        personDTO.setLastname(person.getName().getLast());
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
        java.util.Date dobdate = sdf1.parse(person.getDob().getDate());
        Date dob = new Date(dobdate.getTime());
        personDTO.setDob(dob);
        personDTO.setGender(person.getGender());
        personDTO.setMobile(person.getPhone());
        return personDTO;
    }

    public static List<PersonDTO> getPeople() {
        return null;
    }
}


