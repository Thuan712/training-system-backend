package com.thinkvitals.service;

import com.thinkvitals.exceptions.ValidationException;
import com.thinkvitals.model.UserEntity;
import com.thinkvitals.repository.UserRepository;
import com.thinkvitals.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    //#region Validation
    public void checkValidPassword(String password) {
        if (password.length() < 8) {
            throw new ValidationException("Password must be greater than 8 characters !");
        }

        if (!StringUtils.isValidPassword(password)) {
            throw new ValidationException("Password must contain at least a special character !");
        }
    }

    public void checkValidUsername(String username) {
        if (username.length() < 8) {
            throw new ValidationException("Username must be greater than 8 characters !");
        }

        if (!StringUtils.isValidUsername(username)) {
            throw new ValidationException("Username must not contain any special characters !");
        }
    }

    public void checkValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new ValidationException("Email is required !");
        }

        if (!StringUtils.isValidEmail(email)) {
            throw new ValidationException("Invalid email address !");
        }
    }

    public void checkExistUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new ValidationException("Username is required !");
        }

        boolean exists = userRepository.existsByUsername(username);
        if (exists) {
            throw new ValidationException("Username has been used !");
        }
    }

    public boolean checkUsernameValidity(String username) {
        if (username == null || username.isEmpty()) {
            return true;
        }

        UserEntity userEntity = userRepository.findByUsername(username);
        return userEntity != null;
    }
    public void checkExistEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new ValidationException("Email is required !");
        }

        boolean exists = userRepository.existsByEmail(email);
        if (exists) {
            throw new ValidationException("Email has been used !");
        }
    }

    public void checkValidDateOfBirth(String dateOfBirth) {
        if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);

            try {
                Date date = sdf.parse(dateOfBirth);
                Date now = new Date();
                if (!date.before(now)) {
                    throw new ValidationException("Date of Birth is greater than current year !");
                }
            } catch (ParseException e) {
                throw new ValidationException("Invalid Date of Birth !");
            }
        }
    }
    //#endregion
}
