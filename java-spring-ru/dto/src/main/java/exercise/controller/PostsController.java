package exercise.controller;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping(path="/posts")
public class PostsController{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping(path="")
    public List<PostDTO> show(@RequestParam (defaultValue = "10") long limit){
        return postRepository.findAll().stream().limit(limit).map(this::toPostDTO).toList();
    }

    @GetMapping(path="/{id}")
    public PostDTO index(@PathVariable Long id){

        return toPostDTO(postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found")));
    }

    private PostDTO toPostDTO(Post post){
        var dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        dto.setComments(commentRepository.findByPostId(post.getId())
                .stream().map(this::toCommentDTO).toList());

        return dto;
    }

    private CommentDTO toCommentDTO(Comment comment){
        var dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setBody(comment.getBody());

        return dto;
    }

}
// END
