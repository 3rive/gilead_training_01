package com.gilead.security.api;

import com.gilead.security.library.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/library")
public class LibraryController {

    @GetMapping("/books")
    List<Book> books() {
        return List.of(
                new Book("9780134685991", "Effective Java"),
                new Book("9781491950357", "Designing Data-Intensive Applications"));
    }
}
