package com.springboot.blog.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    //Email should be valid
    @NotEmpty(message = "email should not be empty")
    @Email
    private String email;

    //name should not be empty

    @NotEmpty(message = "name should not be empty or null")
    private String name;

    //body should not be empty
    //body should have at least 10 characters
    @NotEmpty(message ="body should not be empty or null")
    @Size(min = 10, message = "body should have at least 10 characters")
    private String body;
}
