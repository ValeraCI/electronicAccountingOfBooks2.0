package com.company.models;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "lab_Person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty(message = "ФИО не должно быть пустым")
    @Size(min = 6, max = 50, message = "Длинна ФИО от 6 до 50 символов")
    @Column(name = "LFP")
    private String LFP;

    @Min(value = 1925, message = "Человек слишком стар")
    @Column(name = "year_of_birth")
    private int yearOfBirth;

    @OneToMany(mappedBy = "owner")
    List<Book> rentedBook;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLFP() {
        return LFP;
    }

    public void setLFP(String LFP) {
        this.LFP = LFP;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public List<Book> getRentedBook() {
        return rentedBook;
    }

    public void setRentedBook(List<Book> rentedBook) {
        this.rentedBook = rentedBook;
    }

    @Override
    public String toString() {

        return "Person{" +
                "id=" + id +
                ", LFP='" + LFP + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                '}';
    }
}
