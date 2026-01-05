package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.TestPropertySource;
import org.springframework.security.test.context.support.WithMockUser;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;



@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class TeacherControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    
    // -- GET BY ID
    @Test
    @WithMockUser(username = "teacher@get.com")
    public void findById_ReturnTeacher() throws Exception {

        // ARRANGE    
        Teacher teacher = new Teacher();
        teacher.setFirstName("Teacher FirstName");
        teacher.setLastName("Teacher LastName");
        teacher = teacherRepository.save(teacher);

        // ACT
        mockMvc.perform(get("/api/teacher/" + teacher.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Teacher FirstName"))
            .andExpect(jsonPath("$.lastName").value("Teacher LastName"));
    }
    
    @Test
    @WithMockUser
    public void findById_TeacherNotFound() throws Exception {
        // ASSERT
        mockMvc.perform(get("/api/teacher/100"))
            .andExpect(status().isNotFound());
    }
    
    // -- GET ALL
    @Test
    @WithMockUser
    public void findAll_ReturnAllTeachers() throws Exception {

        // ARRANGE
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("Teacher1 First");
        teacher1.setLastName("Teacher1 Last");
        teacher1 = teacherRepository.save(teacher1);
        
        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Teacher2 First");
        teacher2.setLastName("Teacher2 Last");
        teacher2 = teacherRepository.save(teacher2);
        
        // ACT
        mockMvc.perform(get("/api/teacher/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].lastName").value("Teacher1 Last"))
            .andExpect(jsonPath("$[1].lastName").value("Teacher2 Last"));
    }
}
