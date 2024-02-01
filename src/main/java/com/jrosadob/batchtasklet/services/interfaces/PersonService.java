package com.jrosadob.batchtasklet.services.interfaces;

import java.util.List;

import com.jrosadob.batchtasklet.entities.Person;

public interface PersonService {
    void save(Person person);
    void saveAll(List<Person> personList);
}
