package exercise.service;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.BookMapper;
import exercise.mapper.JsonNullableMapper;
import exercise.model.Author;
import exercise.model.Book;
import exercise.repository.AuthorRepository;
import exercise.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {
    // BEGIN
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private JsonNullableMapper jsonNullableMapper;


    public List<BookDTO> getAll(){
        return bookRepository.findAll().stream().map(p -> bookMapper.map(p)).toList();
    }

    public BookDTO getById(Long id){
        return bookMapper.map(findBookByIdInRepo(id));
    }

    public BookDTO createBook(BookCreateDTO dto) {
        var author = findAuthorByIdInRepo(dto.getAuthorId());
        var book = bookMapper.map(dto);
        author.addBook(book);
        authorRepository.save(author);

        bookRepository.save(book);

        return bookMapper.map(book);
    }

    public BookDTO update(Long id, BookUpdateDTO data){
        var author = findAuthorByIdInRepo(jsonNullableMapper.unwrap(data.getAuthorId()));

        var updatedBook = findBookByIdInRepo(id);

        bookMapper.update(data, updatedBook);

        author.addBook(updatedBook);
        authorRepository.save(author);

        bookRepository.save(updatedBook);

        return bookMapper.map(updatedBook);
    }

    public void delete(Long id){
        var deletedBook = findBookByIdInRepo(id);
        var author = deletedBook.getAuthor();
        author.removeBook(deletedBook);
        authorRepository.save(author);

        bookRepository.delete(deletedBook);
    }

    private Book findBookByIdInRepo(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
    }

    private Author findAuthorByIdInRepo(Long id){
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "not found"));
    }
    // END
}
