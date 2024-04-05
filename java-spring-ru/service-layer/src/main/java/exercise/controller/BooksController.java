package exercise.controller;

import java.util.List;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BookService bookService;

    // BEGIN
    @GetMapping(path="")
    @ResponseStatus(HttpStatus.OK)
    public List<BookDTO> index(@RequestParam(defaultValue = "10") Long limit){
        return bookService.getAll().stream().limit(limit).toList();
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO show(@PathVariable Long id){
        return bookService.getById(id);
    }

    @PostMapping(path="")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody BookCreateDTO dto){
        return bookService.createBook(dto);
    }

    @PutMapping(path ="/{id}")
   // @ResponseStatus(HttpStatus.OK)
    public BookDTO update(@PathVariable Long id, @RequestBody BookUpdateDTO data){
        return bookService.update(id, data);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id){
        bookService.delete(id);
    }
    // END
}
