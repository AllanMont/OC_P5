package com.openclassrooms.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;

import java.util.List;
import java.util.Optional;

public class TeacherServiceTest {
    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    @BeforeEach
    public void setUp() {
      MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllTeachers() {
        List<Teacher> mockTeachears = new ArrayList<>();
        mockTeachears.add(new Teacher(1L, "Montagne", "Allan", LocalDateTime.now(), LocalDateTime.now()));
        mockTeachears.add(new Teacher(1L, "Dupont", "Jean", LocalDateTime.now(), LocalDateTime.now()));

        when(teacherRepository.findAll()).thenReturn(mockTeachears);

        List<Teacher> findTeachers = teacherService.findAll();

        assertNotNull(findTeachers);
        assertEquals(2, findTeachers.size());

        assertEquals(mockTeachears, findTeachers);
    }

    @Test
    public void testFindTeacherById() {
        Long id = 1L;
        Teacher teacher = new Teacher(1L, "Montagne", "Allan", LocalDateTime.now(), LocalDateTime.now());

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        Teacher findTeacher = teacherService.findById(id);

        assertNotNull(findTeacher);

        assertEquals(teacher, findTeacher);
    }

    @Test
    public void testFindTeacherById_NotFound() {
        Long id = 1L;

        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        Teacher result = teacherService.findById(id);

        assertNull(result);
    }

}
