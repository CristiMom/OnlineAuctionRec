package com.sda.onlineAuction.validator;

import com.sda.onlineAuction.dto.UserDto;
import com.sda.onlineAuction.model.User;
import com.sda.onlineAuction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Optional;

@Service
public class UserDtoValidator {

    @Autowired
    private UserRepository userRepository;

    public void validate(UserDto userDto, BindingResult bindingResult){
        String password = userDto.getPassword();
        if (password.length() < 6){
            FieldError fieldError = new FieldError("userDto", "password", "The password is too short");
            bindingResult.addError(fieldError);
        }
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()){
            FieldError fieldError = new FieldError("userDto", "email", "Email already in use!");
            bindingResult.addError(fieldError);
        }
    }
}
