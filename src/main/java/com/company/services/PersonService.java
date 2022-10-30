package com.company.services;

import com.company.models.Book;
import com.company.models.Person;
import com.company.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

//TODO решить n+1 проблему

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final int SIZE = 5;

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> getAll(int page){
        //page - номер страницы, size - размер
        return peopleRepository.findAll(PageRequest.of(page, SIZE)).getContent();
    }

    public List<Person> getAll(){
        return peopleRepository.findAll();
    }

    public List<Person> getWithoutBooks(int page){
        List<Person> peopleWithoutBooks = peopleRepository.findAll()
                .stream()
                .filter(p -> p.getRentedBook().size() == 0)
                .skip(page*SIZE)
                .limit(SIZE)
                .collect(Collectors.toList());

        return peopleWithoutBooks;
    }

    public List<Person> getWithBooks(int page){
        List<Person> peopleWithBooks = peopleRepository.findAll()
                .stream()
                .filter(p -> p.getRentedBook().size() != 0)
                .skip(page*SIZE)
                .limit(SIZE)
                .collect(Collectors.toList());

        return peopleWithBooks;
    }

    public Person getFromId(int id){
        Person person = peopleRepository.getOne(id);
        for (Book book: person.getRentedBook()) {
            Calendar today = new GregorianCalendar();
            //в теории такой ситуации быть не может, но для подстраховки пусть будет
            if (book.getRentalStartDate() == null){
                book.setOverdue(false);
                continue;
            }
            long elapsedTime = today.getTimeInMillis() - book.getRentalStartDate().getTimeInMillis();
            if (elapsedTime/(24 * 60 * 60 * 1000) >= 10)  book.setOverdue(true);
            else book.setOverdue(false);
        }
        return person;
    }

    @Transactional(readOnly = false)
    public void edit(int id, Person editPerson){
        editPerson.setId(id);
        peopleRepository.save(editPerson);
    }

    @Transactional(readOnly = false)
    public void add(Person person){
        peopleRepository.save(person);
    }

    @Transactional(readOnly = false)
    public void delete(int id){
        Person person = peopleRepository.getOne(id);
        peopleRepository.delete(person);
    }

    public Person getByLFP(String LFP){
        return peopleRepository.getPersonByLFP(LFP);
    }
}