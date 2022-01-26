package com.rapidnotes.heimdall.dto;

import com.rapidnotes.heimdall.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserRegisterDTOMapper {

    PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Mapping(source = "password", target = "encodedPassword", qualifiedByName = "passwordToEncodedPassword")
    public abstract User userRegisterDTOToUser(UserRegisterDTO userRegisterDTO);

    @Named("passwordToEncodedPassword")
    public String passwordToEncodedPassword(String password) {
       return passwordEncoder.encode(password);
    }

}
