package com.example.SpringBlog.controllers;


import com.example.SpringBlog.dtos.BlogRecordDto;
import com.example.SpringBlog.models.BlogModel;
import com.example.SpringBlog.repositories.BlogRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController

public class BlogController {

    @Autowired
    BlogRepository blogRepository;

    @PostMapping("/posts")
    public ResponseEntity<BlogModel> savePost(@RequestBody @Valid BlogRecordDto blogRecordDto) {
        var blogModel = new BlogModel();
        BeanUtils.copyProperties(blogRecordDto, blogModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(blogRepository.save(blogModel));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<BlogModel>> getAllPosts() {
        List<BlogModel> postList = blogRepository.findAll();
        if (!postList.isEmpty()) {
            for (BlogModel post : postList) {
                UUID id = post.getIdPost();
                post.add(linkTo(methodOn(BlogController.class).getOnePost(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(postList);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Object> getOnePost(@PathVariable(value = "id") UUID id) {
        Optional<BlogModel> post0 = blogRepository.findById(id);
        if (post0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
        }
        post0.get().add(linkTo(methodOn(BlogController.class).getAllPosts()).withRel("Posts Lists"));
        return ResponseEntity.status(HttpStatus.OK).body(post0.get());
    }

    @PutMapping("posts/{id}")
    public ResponseEntity<Object> updatePost(@PathVariable(value = "id") UUID id, @RequestBody @Valid BlogRecordDto blogRecordDto) {
        Optional<BlogModel> post0 = blogRepository.findById(id);
        if (post0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
        }
        var postModel = post0.get();
        BeanUtils.copyProperties(blogRecordDto, postModel);
        return ResponseEntity.status(HttpStatus.OK).body(blogRepository.save(postModel));

    }

    @DeleteMapping("posts/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable(value = "id") UUID id) {
        Optional<BlogModel> post0 = blogRepository.findById(id);
        if (post0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post Not Found.");
        }
        blogRepository.delete(post0.get());
        ;
        return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully.");
    }
}