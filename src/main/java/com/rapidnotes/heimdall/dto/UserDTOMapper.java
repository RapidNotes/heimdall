package com.rapidnotes.heimdall.dto;
//
//import com.rapidnotes.heimdall.domain.User;
//import com.rapidnotes.heimdall.domain.UserView;
//import org.springframework.stereotype.Component;
//
//import java.util.function.Function;
//
//@Component
//public class UserViewMapper implements Function<User, UserView> {
//    @Override
//    public UserView apply(User user) {
//       return new UserView(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail());
//    }
//}
//

import com.rapidnotes.heimdall.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserDTOMapper {

    public abstract UserDTO userToUserDTO(User user);

    public abstract User userDTOToUser(UserDTO userDTO);

}