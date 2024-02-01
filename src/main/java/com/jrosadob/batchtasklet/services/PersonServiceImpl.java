package com.jrosadob.batchtasklet.services;

import java.util.List;

import com.jrosadob.batchtasklet.entities.Person;
import com.jrosadob.batchtasklet.repositories.PersonRepository;
import com.jrosadob.batchtasklet.services.interfaces.PersonService;

public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    public PersonServiceImpl(final PersonRepository personRepository) {
        super();
        this.personRepository = personRepository;
    }

    @Override
    public void save(Person person) {
        personRepository.save(person);
    }

    @Override
    public void saveAll(List<Person> personList) {
        personRepository.saveAll(personList);
    }
}
