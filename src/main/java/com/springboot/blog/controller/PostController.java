package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }



    //create blog post rest api
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    //get all blog posts rest api
    @GetMapping
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(name = "pageNo",defaultValue = AppConstant.DEFAULT_PAGE_NO, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstant.DEFAULT_SORT_DIRECTION,required = false) String sortDir

    ){
        return ResponseEntity.ok(postService.getAllPosts(pageNo,pageSize,sortBy,sortDir));
    }

    //get post by id rest api
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name="id") Long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePostById(@Valid @RequestBody PostDto postDto, @PathVariable(name="id") Long id){
        return ResponseEntity.ok(postService.updatePostById(postDto,id));
    }

    //delete post by id rest api
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable(name="id") Long id){
        postService.deletePostById(id);
        return new ResponseEntity<>(String.format("Post of id:%s is deleted successfully",String.valueOf(id)),HttpStatus.OK);
    }
}
