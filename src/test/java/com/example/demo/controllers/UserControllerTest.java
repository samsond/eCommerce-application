package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    private UserController userController;

    private final UserRepository userRepo = mock(UserRepository.class);

    private final CartRepository cartRepo = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @BeforeEach
    public void setup() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() {
        when(encoder.encode("12345678")).thenReturn("thisIsHashed");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("12345678");
        userRequest.setConfirmPassword("12345678");

        final ResponseEntity<User> response = userController.createUser(userRequest);


        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);


    }

    @Test
    public void find_by_username_happy_path() {
//        ResponseEntity<User> userResponseEntity = userController.findByUserName("test");
//        assertEquals(404, userResponseEntity.getStatusCode().value());
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("12345678");
        when(userRepo.findByUsername("test")).thenReturn(user);
        ResponseEntity<User> userResponseEntity = userController.findByUserName("test");
        assertNotNull(userResponseEntity.getBody());
        assertEquals("test", userResponseEntity.getBody().getUsername());

    }

    @Test
    public void find_by_id_happy_path(){
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("12345678");
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        ResponseEntity<User> userResponseEntity = userController.findById(1L);
        assertNotNull(userResponseEntity.getBody());
        assertEquals(1L, userResponseEntity.getBody().getId());
    }
}
