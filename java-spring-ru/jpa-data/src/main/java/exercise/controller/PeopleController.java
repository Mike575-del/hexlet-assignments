package exercise.controller;

import exercise.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.model.Person;

@RestController
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping(path = "/{id}")
    public Person show(@PathVariable long id) {
        return personRepository.findById(id).get();
    }

    // BEGIN
    @GetMapping
    public List<Person> getPersonList(){
        return personRepository.findAll().stream().toList();
    }

    @PostMapping
    public ResponseEntity<Person> createNewPerson(@RequestBody Person person){
        personRepository.save(person);
        return ResponseEntity.status(201).body(person);
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id){
        personRepository.deleteById(id);
    }
    // END
}
