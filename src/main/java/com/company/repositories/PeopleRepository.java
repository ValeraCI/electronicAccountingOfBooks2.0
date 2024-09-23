package com.company.repositories;

import com.company.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Person getPersonByLFP(String LFP);
}
