package com.openclassrooms.starterjwt.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;



@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    public void findAll_ShouldReturnAllTeachers() {
        // ARRANGE
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("Teacher Name");
        teacher1.setLastName("Teacher Lastname");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Teacher Name 2");
        teacher2.setLastName("Teacher Lastname 2");
        
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        when(teacherRepository.findAll()).thenReturn(teachers);

        // ACT
        List<Teacher> result = teacherService.findAll();

        // ASSERT
        assertThat(result).containsExactlyInAnyOrder(teacher1, teacher2);
    }

    @Test
    public void findById_ShouldReturnTeacher() {
        // ARRANGE
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Teacher Name");
        teacher.setLastName("Teacher Lastname");

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // ACT
        Teacher result = teacherService.findById(1L);

        // ASSERT
        assertEquals(teacher, result);
    }
}
