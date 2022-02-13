package com.rapidnotes.heimdall.dto;

import com.rapidnotes.heimdall.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserRegisterDTOMapperTest {

    @Mock
    PasswordEncoder passwordEncoder;

    UserRegisterDTOMapper userRegisterDTOMapper;

    AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encoded_password");
        userRegisterDTOMapper = Mappers.getMapper(UserRegisterDTOMapper.class);
        userRegisterDTOMapper.setPasswordEncoder(passwordEncoder);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("map UserRegisterDTO object to User object")
    public void shouldConvertUserRegisterDTOToUser() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(
                "test_user",
                "first_name",
                "last_name",
                "password",
                "test_email"
        );
        User user = userRegisterDTOMapper.userRegisterDTOToUser(userRegisterDTO);
        Assertions.assertEquals(user.getUsername(), userRegisterDTO.getUsername());
        Assertions.assertEquals(user.getFirstname(), userRegisterDTO.getFirstname());
        Assertions.assertEquals(user.getLastname(), userRegisterDTO.getLastname());
        Assertions.assertEquals(user.getEmail(), userRegisterDTO.getEmail());
        Assertions.assertEquals(user.getPassword(), "encoded_password");
    }

}