package com.rapidnotes.heimdall.services;

import com.rapidnotes.heimdall.dao.UserRepo;
import com.rapidnotes.heimdall.dto.UserRegisterDTO;
import com.rapidnotes.heimdall.domain.User;
import com.rapidnotes.heimdall.dto.UserRegisterDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRegisterDTOMapper userRegisterDTOMapper;
    private final UserRepo userRepo;
    public User create(UserRegisterDTO userRegisterDTO) {
        if (userRepo.findById(userRegisterDTO.getUsername()).isPresent()) {
            throw new ValidationException(format("Username %s already exists!", userRegisterDTO.getUsername()));
        }

        User user = userRegisterDTOMapper.userRegisterDTOToUser(userRegisterDTO);
        userRepo.save(user);
        return user;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
