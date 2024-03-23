package exercise.controller;

import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping(path="")
    public List<Post> show(@RequestParam(name = "limit", defaultValue = "10") Integer limit){
        return postRepository.findAll().stream().limit(limit).toList();
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<Post> index(@PathVariable long id){
        var result = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post with id " + id +" not found"));
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(path="")
    public ResponseEntity<Post> createPost(@RequestBody Post post){
        postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/{id}")
    public void updatePost(@PathVariable long id, @RequestBody Post data){
        var updatedPost = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        updatedPost.setTitle(data.getTitle());
        updatedPost.setBody(data.getBody());
        postRepository.save(updatedPost);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id){
        var deletedPost = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(("Post not found")));
        commentRepository.deleteByPostId(id);
        postRepository.delete(deletedPost);
    }

}
// END
