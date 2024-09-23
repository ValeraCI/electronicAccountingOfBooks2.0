package com.company.services;

import com.company.models.Book;
import com.company.models.Person;
import com.company.repositories.BooksRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final int SIZE = 5;

    private final BooksRepository booksRepository;

    public BookService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
        ;
    }

    public List<Book> getAll(int page, boolean sortByYear) {
        if (sortByYear) return booksRepository.findAll(PageRequest.of(page, SIZE, Sort.by("publicationYear"))).getContent();
        return booksRepository.findAll(PageRequest.of(page, SIZE)).getContent();
    }

    public List<Book> getFree(int page, boolean sortByYear) {

        List<Book> books;

        if (sortByYear) books = booksRepository.findAll(Sort.by("publicationYear"));
        else books = booksRepository.findAll();


        return books
                .stream()
                .filter(b -> b.getOwner() == null)
                .skip(page * SIZE)
                .limit(SIZE)
                .collect(Collectors.toList());
    }

    public List<Book> getBusy(int page, boolean sortByYear) {
        List<Book> books;

        if (sortByYear) books = booksRepository.findAll(Sort.by("publicationYear"));
        else books = booksRepository.findAll();

        return books
                .stream()
                .filter(b -> b.getOwner() != null)
                .skip(page * SIZE)
                .limit(SIZE)
                .collect(Collectors.toList());
    }

    public Book getForId(int id) {
        Book book = booksRepository.getOne(id);
        System.out.println(book.getOwner());
        return book;
    }

    @Transactional(readOnly = false)
    public void edit(int id, Book editBook) {
        editBook.setId(id);
        booksRepository.save(editBook);
    }

    @Transactional(readOnly = false)
    public void add(Book book) {
        booksRepository.save(book);
    }

    @Transactional(readOnly = false)
    public void delete(int id) {
        Book book = booksRepository.getOne(id);
        booksRepository.delete(book);
    }

    @Transactional(readOnly = false)
    public void take(int id, Person person) {
        Book book = booksRepository.getOne(id);
        book.setOwner(person);
        book.setRentalStartDate(new GregorianCalendar());
        booksRepository.save(book);
    }

    @Transactional(readOnly = false)
    public void getBack(int id) {
        Book book = booksRepository.getOne(id);
        book.setOwner(null);
        book.setRentalStartDate(null);
        booksRepository.save(book);
    }

    public List<Book> getBooksByTitleStartingWith(String titleStartingWith) {
        List<Book> booksByTitleStartingWith = booksRepository.getBooksByTitleStartingWith(titleStartingWith);
        booksByTitleStartingWith
                .forEach(b -> {
                    Person owner = b.getOwner();
                    System.out.println(owner);
                });

        return booksByTitleStartingWith;
    }

}
