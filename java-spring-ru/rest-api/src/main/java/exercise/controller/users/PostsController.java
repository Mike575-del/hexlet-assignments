package exercise.controller.users;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api/users")
public class PostsController{

    private List<Post> posts = Data.getPosts();

    @GetMapping("/{id}/posts")
    public List<Post> getUserPosts(@PathVariable int id, @RequestParam(defaultValue = "10") Integer limit){
        return posts.stream().filter(p -> p.getUserId() == id).toList();
    }

    @PostMapping("/{id}/posts")
    public ResponseEntity<Post> createUserPost(@PathVariable int id, @RequestBody Post data){
        var result = new Post(id, data.getSlug(), data.getTitle(), data.getBody());
        posts.add(result);

        return ResponseEntity.status(201).body(result);
    }
}
// END
