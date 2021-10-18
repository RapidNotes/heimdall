package com.rapidnotes.heimdall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
}
