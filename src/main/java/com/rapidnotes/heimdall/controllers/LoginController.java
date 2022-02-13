package com.rapidnotes.heimdall.controllers;

import com.rapidnotes.heimdall.domain.LoginDetails;
import com.rapidnotes.heimdall.domain.User;
import com.rapidnotes.heimdall.dto.UserDTO;
import com.rapidnotes.heimdall.dto.UserDTOMapper;
import com.rapidnotes.heimdall.util.JWTTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JWTTokenUtil jwtTokenUtil;
    private final UserDTOMapper userDTOMapper;

    public LoginController(AuthenticationManager authenticationManager,
                           JWTTokenUtil jwtTokenUtil,
                           UserDTOMapper userDTOMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDTOMapper = userDTOMapper;
    }

    @Operation(summary = "Login")
    @PostMapping("/api/v1/login")
    public ResponseEntity<UserDTO> login(@RequestBody @Validated LoginDetails loginDetails) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginDetails.getUsername(), loginDetails.getPassword()
                            )
                    );

            User user = (User) authentication.getPrincipal();

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtTokenUtil.generateJWT(user)
                    ).body(userDTOMapper.userToUserDTO(user));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
