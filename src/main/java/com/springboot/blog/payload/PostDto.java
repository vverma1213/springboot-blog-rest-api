package com.springboot.blog.payload;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class PostDto {

    private Long id;
    //description should not be empty
    //description should have at least 10 characters
    @NotEmpty
    @Size(min = 10, message = "post description should have at least 10 characters")
    private String description;

    //title should not be empty
    //title should have atleast 2 characters
    @NotEmpty
    @Size(min = 2,message="post title should have at least 2 characters")
    private String title;

    //content should not be empty
    @NotEmpty
    private String content;
    private Set<CommentDto> comments;
}
