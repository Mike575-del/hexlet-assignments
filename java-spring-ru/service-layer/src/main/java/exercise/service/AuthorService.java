package exercise.service;

import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.model.Author;
import exercise.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AuthorService {
    // BEGIN
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    public List<AuthorDTO> getAll(){
        return authorRepository.findAll().stream().map(p -> authorMapper.map(p)).toList();
    }

    public AuthorDTO getById(Long id){
        return authorMapper.map(findAuthorInRepo(id));
    }

    public AuthorDTO createAuthor(AuthorCreateDTO dto){
        var newAuthor = authorMapper.map(dto);
        authorRepository.save(newAuthor);
        return authorMapper.map(newAuthor);
    }

    public AuthorDTO updateAuthor(Long id, AuthorUpdateDTO data) {
        var updatedAuthor = findAuthorInRepo(id);

        authorMapper.update(data, updatedAuthor);
        authorRepository.save(updatedAuthor);

        return authorMapper.map(updatedAuthor);
    }

    public void deleteAuthor(Long id){
        var deletedAuthor = findAuthorInRepo(id);

        authorRepository.delete(deletedAuthor);
    }

    private Author findAuthorInRepo(Long id){
        var author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " not found"));
        return author;
    }
    // END
}
