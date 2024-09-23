package com.company.controllers;

import com.company.models.Book;
import com.company.models.Person;
import com.company.services.BookService;
import com.company.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final PersonService personService;
    private final BookService bookService;

    @Autowired
    public BookController(PersonService personService, BookService bookService) {
        this.personService = personService;
        this.bookService = bookService;
    }

    @GetMapping()
    public String showBooks(@RequestParam(value = "receive", required = false) String receive,
                            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                            @RequestParam(value = "sort_by_year", required = false) String sortByYearStr,
                            Model model) {
        List<Book> books;

        if (receive == null) receive = "any";
        if (pageNumber == null || pageNumber < 0) pageNumber = 0;
        boolean sortByYear;
        sortByYear = sortByYearStr != null && sortByYearStr.equals("true");

        while (true) {
            if (receive.equals("free")) {
                model.addAttribute("titleText", "Свободные книги:");
                books = bookService.getFree(pageNumber, sortByYear);
            } else if (receive.equals("busy")) {
                model.addAttribute("titleText", "Занятые книги:");
                books = bookService.getBusy(pageNumber, sortByYear);
            } else {
                model.addAttribute("titleText", "Все книги:");
                books = bookService.getAll(pageNumber, sortByYear);
            }
            if (!books.isEmpty() || pageNumber == 0) break;
            else pageNumber--;
        }
        model.addAttribute("receive", receive);
        model.addAttribute("next", pageNumber + 1);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("books", books);
        return "book/index";
    }


    @GetMapping("/search")
    public String search(@RequestParam(value = "title", required = false) String title,
                         Model model) {

        if (title == null || title.isEmpty()) model.addAttribute("search", false);
        else {
            List<Book> books = bookService.getBooksByTitleStartingWith(title);
            System.out.println(books.size());
            model.addAttribute("search", true);
            model.addAttribute("books", books);
        }
        return "book/search";
    }


    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model) {
        Book book = bookService.getForId(id);
        model.addAttribute("book", book);
        if (book.getOwner() != null) {
            model.addAttribute("owner", book.getOwner());
        } else {
            model.addAttribute("person", new Person());
            model.addAttribute("people", personService.getAll());
        }
        return "book/show";
    }

    @GetMapping("/new")
    public String addForm(Model model) {
        model.addAttribute("book", new Book());
        return "book/add";
    }

    @PostMapping()
    public String save(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            return "/book/add";
        }
        bookService.add(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable("id") int id, Model model) {
        Book book = bookService.getForId(id);
        model.addAttribute("book", book);
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            return "/book/edit";
        }
        bookService.edit(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.getBack(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/take")
    public String take(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookService.take(id, person);
        return "redirect:/books";
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }
}
