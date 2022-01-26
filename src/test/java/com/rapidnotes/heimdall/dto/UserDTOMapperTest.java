package com.rapidnotes.heimdall.dto;

import com.rapidnotes.heimdall.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class UserDTOMapperTest {

    UserDTOMapper userDTOMapper;

    @BeforeEach
    void setUp() {
        userDTOMapper = Mappers.getMapper(UserDTOMapper.class);
    }

    @Test
    @DisplayName("map User object to UserDTO object")
    void shouldConvertUserToUserDTO() {
        User user = new User("test_user",
                "first_name",
                "last_name",
                "test_email",
                "password");
        UserDTO userDTO = userDTOMapper.userToUserDTO(user);
        assertThat(userDTO.getUsername(), is(equalTo(user.getUsername())));
        assertThat(userDTO.getFirstname(), is(equalTo(user.getFirstname())));
        assertThat(userDTO.getLastname(), is(equalTo(user.getLastname())));
        assertThat(userDTO.getEmail(), is(equalTo(user.getEmail())));
    }

    @Test
    @DisplayName("map UserDTO object to User object")
    void userDTOToUser() {
        UserDTO userDTO = new UserDTO("test_user",
                "first_name",
                "last_name",
                "test_email");
        User user = userDTOMapper.userDTOToUser(userDTO);
        assertThat(user.getUsername(), is(equalTo(userDTO.getUsername())));
        assertThat(user.getFirstname(), is(equalTo(userDTO.getFirstname())));
        assertThat(user.getLastname(), is(equalTo(userDTO.getLastname())));
        assertThat(user.getEmail(), is(equalTo(userDTO.getEmail())));
    }
}