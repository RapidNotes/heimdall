package com.rapidnotes.heimdall.dto;

import com.rapidnotes.heimdall.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public abstract class UserRegisterDTOMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public abstract UserRegisterDTO useToUserRegisterDTO(User user);

    @Mapping(source = "password", target = "encodedPassword", qualifiedBy = EncodedMapping.class)
    public abstract User userRegisterDTOToUser(UserRegisterDTO userRegisterDTO);

}
