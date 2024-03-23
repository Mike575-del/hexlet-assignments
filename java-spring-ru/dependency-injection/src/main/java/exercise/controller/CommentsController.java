package exercise.controller;

import exercise.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("comments")
public class CommentsController{

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping(path = "")
    public List<Comment> show(@RequestParam (name = "limit", defaultValue = "10") Long limit){
        return  commentRepository.findAll().stream().limit(limit).toList();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Comment> index(@PathVariable long id){
        var result = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(path = "")
    public ResponseEntity<Comment> create(@RequestBody Comment comment){
        commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PutMapping(path = "/{id}")
    public void update(@PathVariable long id, @RequestBody Comment data){
        var updatedComment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        updatedComment.setBody(data.getBody());
        updatedComment.setPostId(data.getPostId());
        commentRepository.save(updatedComment);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteComment(@PathVariable long id){
        var deletedComment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        commentRepository.delete(deletedComment);
    }
}
// END
