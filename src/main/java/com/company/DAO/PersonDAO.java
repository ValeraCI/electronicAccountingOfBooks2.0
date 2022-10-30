/*package com.company.DAO;

import com.company.Models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    private final BookDAO bookDAO;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate, BookDAO bookDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookDAO = bookDAO;
    }

    public List<Person> getAny(){
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper(Person.class));
    }

    public Person get(int id){
        return (Person) jdbcTemplate.query("SELECT * FROM Person WHERE id = ?",
                new BeanPropertyRowMapper(Person.class), new Object[]{id}).stream().findAny().orElse(null);
    }

    public Person getForLFP(String LFP){
        return (Person) jdbcTemplate.query("SELECT * FROM Person WHERE LFP = ?",
                new BeanPropertyRowMapper(Person.class), new Object[]{LFP}).stream().findAny().orElse(null);
    }

    public void add(Person person){
        jdbcTemplate.update("INSERT INTO Person(lfp, year_of_birth) values(?, ?)",
                person.getLFP(), person.getYearOfBirth());
    }

    public void edit(Person person, int id){
        jdbcTemplate.update("UPDATE Person SET lfp=?, year_of_birth=? WHERE id=?",
                person.getLFP(), person.getYearOfBirth(), id);
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }

    public List<Person> getWithoutBooks() {
        return getAny()
                .stream()
                .filter(person -> bookDAO.getForPersonID(person.getId()).size() == 0)
                .collect(Collectors.toList());
    }

    public List<Person> getWithBooks() {
        return getAny()
                .stream()
                .filter(person -> bookDAO.getForPersonID(person.getId()).size() != 0)
                .collect(Collectors.toList());
    }
}
*/