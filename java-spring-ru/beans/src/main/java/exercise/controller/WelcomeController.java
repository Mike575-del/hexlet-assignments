package exercise.controller;

import exercise.daytime.Daytime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

// BEGIN
@RestController
public class WelcomeController{

    @Autowired
    private Daytime daytime;

    @GetMapping("/welcome")
    public ResponseEntity<String> getWelcome(){
        return ResponseEntity.ok().body("It is " + daytime.getName() + " now! Welcome to Spring!");
    }
}
// END
