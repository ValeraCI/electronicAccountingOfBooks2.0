package com.company.models;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Calendar;


@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Название не может быть пустым")
    @Size(min = 2, max = 200, message = "Название должно иметь длинну от 2 до 200 символов")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Укажите автора")
    @Size(min = 2, max = 50, message = "Имя автора должно иметь длинну от 2 до 50 символов")
    @Column(name = "author")
    private String author;

    @Min(value = 1800, message = "Книга слишком стара")
    @Column(name = "year")
    private int year;

    @ManyToOne()
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    Person owner;

    @Column(name = "rental_start_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Calendar rentalStartDate;

    @Transient
    private boolean Overdue = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Calendar getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(Calendar rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public boolean isOverdue() {
        return Overdue;
    }

    public void setOverdue(boolean overdue) {
        Overdue = overdue;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                '}';
    }
}
