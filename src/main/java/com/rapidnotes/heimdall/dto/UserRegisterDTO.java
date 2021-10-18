package com.rapidnotes.heimdall.dto;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
public class UserRegisterDTO {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
}