package com.rapidnotes.heimdall.controllers;

import com.rapidnotes.heimdall.dao.UserRepo;
import com.rapidnotes.heimdall.domain.User;
import com.rapidnotes.heimdall.dto.UserDTO;
import com.rapidnotes.heimdall.dto.UserDTOMapper;
import com.rapidnotes.heimdall.dto.UserRegisterDTO;
import com.rapidnotes.heimdall.dto.UserRegisterDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RegisterController {

    private final UserDTOMapper userDTOMapper;
    private final UserRegisterDTOMapper userRegisterDTOMapper;
    private final UserRepo userRepo;

    public RegisterController(UserDTOMapper userDTOMapper,
                              UserRegisterDTOMapper userRegisterDTOMapper,
                              UserRepo userRepo) {
        this.userDTOMapper = userDTOMapper;
        this.userRegisterDTOMapper = userRegisterDTOMapper;
        this.userRepo = userRepo;
    }

    @Operation(summary = "Register")
    @PostMapping("/api/v1/register")
    public UserDTO registerUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        User user = userRegisterDTOMapper.userRegisterDTOToUser(userRegisterDTO);
        userRepo.save(user);
        return userDTOMapper.userToUserDTO(user);
    }

    @Operation(summary = "Get user details by username", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/api/v1/register/{username}")
    public UserDTO getUser(@PathVariable String username) {
        User user = userRepo.getById(username);
        return userDTOMapper.userToUserDTO(user);
    }

    @Operation(summary = "Get all users on heimdall", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/api/v1/register")
    public List<UserDTO> getAllUsers(@RequestParam(defaultValue = "10") Integer limit,
                                     @RequestParam(defaultValue = "0") Integer page, HttpServletResponse response) {
        List<User> users = userRepo.findAll(PageRequest.of(page, limit)).getContent();
        List<UserDTO> userDTOS = new ArrayList<>();
        for ( User user: users) {
            userDTOS.add(userDTOMapper.userToUserDTO(user));
        }
        response.addIntHeader("X-Total-Count", (int)userRepo.count());
        response.addIntHeader("X-Response-Count", userDTOS.size());
        return userDTOS;
    }
}
