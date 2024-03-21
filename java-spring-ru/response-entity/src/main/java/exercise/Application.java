package exercise;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import exercise.model.Post;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getListOfPosts(@RequestParam (defaultValue = "10") Integer limit){
        var result = posts.stream().limit(limit).toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(result);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Optional<Post>> getPostById(@PathVariable String id){
        var result = posts.stream().filter(p -> p.getId().equals(id)).findFirst();
        return result.isEmpty()? ResponseEntity.status(404).build() : ResponseEntity.ok().body(result);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> publicNewPost(@RequestBody Post post){
        posts.add(post);
        return ResponseEntity.status(201).body(post);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Optional<Post>> updatePost(@PathVariable String id, @RequestBody Post data){
        var findedPost = posts.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (findedPost.isEmpty()){
            return ResponseEntity.status(204).build();
        } else {
            var updatedPost = findedPost.get();
            updatedPost.setId(data.getId());
            updatedPost.setTitle(data.getTitle());
            updatedPost.setBody(data.getBody());

            return ResponseEntity.ok().body(Optional.of(data));
        }
    }
    // END

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
