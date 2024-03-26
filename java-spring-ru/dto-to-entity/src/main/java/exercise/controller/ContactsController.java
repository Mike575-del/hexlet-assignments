package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import exercise.model.Contact;
import exercise.repository.ContactRepository;
import exercise.dto.ContactDTO;
import exercise.dto.ContactCreateDTO;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    private ContactRepository contactRepository;

    // BEGIN
    @PostMapping(path="")
    public ResponseEntity<ContactDTO> index(@RequestBody ContactCreateDTO contactData){
        var contact = toEntity(contactData);
        contactRepository.save(contact);

        ContactDTO contactDTO = toDTO(contact);

        return ResponseEntity.status(HttpStatus.CREATED).body(contactDTO);

    }

    private Contact toEntity(ContactCreateDTO dto){
        var contact = new Contact();
        contact.setFirstName(dto.getFirstName());
        contact.setLastName(dto.getLastName());
        contact.setPhone(dto.getPhone());

        return contact;
    }

    private ContactDTO toDTO(Contact contact){
        var dto = new ContactDTO();
        dto.setId(contact.getId());
        dto.setFirstName(contact.getFirstName());
        dto.setLastName(contact.getLastName());
        dto.setPhone(contact.getPhone());
        dto.setUpdatedAt(contact.getUpdatedAt());
        dto.setCreatedAt(contact.getCreatedAt());

        return dto;
    }
    // END
}
