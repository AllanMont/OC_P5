package com.openclassrooms.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
      MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deleteUserTest() {
        Long id = 1L;

        userService.delete(id);

        verify(userRepository, times(1)).deleteById(id);
    }
    
    @Test
    public void findUserByIdTest() {
        Long id = 1L;
        
        User newUser = new User();
        newUser.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(newUser));

        User findUser = userService.findById(id);

        assertNotNull(findUser);
        assertEquals(newUser, findUser);
    }
}
