package com.jrosadob.batchtasklet.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jrosadob.batchtasklet.entities.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {
}
