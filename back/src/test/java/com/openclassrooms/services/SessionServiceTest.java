package com.openclassrooms.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

public class SessionServiceTest {
    @InjectMocks
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;
    
    @BeforeEach
    public void setUp() {
      MockitoAnnotations.openMocks(this);
    }

    @Test
    public void newSessionTest() {
        Session newSession = new Session();
        newSession.setId(1L);
        newSession.setName("Java");
        newSession.setDescription("Java is a programming language");
        newSession.setCreatedAt((LocalDateTime.now()));
        newSession.setDate(new Date());
        newSession.setUsers(null);

        when(sessionRepository.save(newSession)).thenReturn(newSession);

        Session result = sessionService.create(newSession);

        assertEquals(newSession, result);
    }

    @Test
    public void deleteSessionTest() {
        Long id = 1L;

        sessionService.delete(id);

        verify(sessionRepository, times(1)).deleteById(id);
    }

    @Test
    public void findAllSessionTest(){
        List<Session> mockSessions = new ArrayList<>();
        mockSessions.add(new Session());
        mockSessions.add(new Session());

        when(sessionRepository.findAll()).thenReturn(mockSessions);

        List<Session> findSessions = sessionService.findAll();

        assertEquals(mockSessions, findSessions);
    }

    @Test
    public void getSessionByIdTest() {
        Long id = 1L;
        Session session = new Session();
        session.setId(id);

        when(sessionRepository.findById(id)).thenReturn(java.util.Optional.of(session));

        Session findSession = sessionService.getById(id);

        assertEquals(session, findSession);
    }
    

    @Test
    public void updateSessionTest() {
        Long id = 1L;
        Session session = new Session();
        session.setId(id);
        session.setName("Java");
        session.setDescription("Java is a programming language");
        session.setCreatedAt((LocalDateTime.now()));
        session.setDate(new Date());
        session.setUsers(null);

        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.update(id, session);

        assertEquals(session, result);
    }

    @Test
    public void participateSessionTest() {
    Long sessionId = 1L;
    Long userId = 2L;

    Session session = new Session();
    session.setId(sessionId);
    session.setUsers(new ArrayList<>());

    User user = new User();
    user.setId(userId);

    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    sessionService.participate(sessionId, userId);

    assertThat(session.getUsers()).contains(user);

    verify(sessionRepository).save(session);
    }

    @Test
    public void notFoundParticipateSessionTest() {
    Long sessionId = 1L;
    Long userId = 2L;

    when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    public void alreadyParticipateSessionTest() {
    Long sessionId = 1L;
    Long userId = 2L;

    Session session = new Session();
    session.setId(sessionId);
    session.setUsers(new ArrayList<>());

    User user = new User();
    user.setId(userId);

    session.getUsers().add(user);

    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

    assertThrows(Exception.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    public void noLongerParticipateSessionTest() {
    Long sessionId = 1L;
    Long userId = 2L;

    Session session = new Session();
    session.setId(sessionId);
    session.setUsers(new ArrayList<>());

    User user = new User();
    user.setId(userId);

    session.getUsers().add(user);

    when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

    sessionService.noLongerParticipate(sessionId, userId);

    assertThat(session.getUsers()).doesNotContain(user);
    }
}
