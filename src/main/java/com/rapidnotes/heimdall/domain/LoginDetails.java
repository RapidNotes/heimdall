package com.rapidnotes.heimdall.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDetails {

    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotBlank(message = "Password is mandatory")
    private String password;
}
