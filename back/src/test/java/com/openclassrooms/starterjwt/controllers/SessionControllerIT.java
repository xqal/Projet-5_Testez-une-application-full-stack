package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.TestPropertySource;
import org.springframework.security.test.context.support.WithMockUser;




@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class SessionControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    // GET SESSION BY ID
    @Test
    @WithMockUser
    public void findById_ReturnSession() throws Exception {

        // ARRANGE    
        Teacher teacher = new Teacher();
        teacher.setFirstName("Teacher FirstName");
        teacher.setLastName("Teacher LastName");
        teacher = teacherRepository.save(teacher);

        Session session = new Session();
        session.setName("Session Name");
        session.setDate(new Date());
        session.setDescription("Session Description");
        session.setTeacher(teacher);
        session = sessionRepository.save(session);

        // ACT
        mockMvc.perform(get("/api/session/" + session.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(session.getId()))
            .andExpect(jsonPath("$.name").value("Session Name"))
            .andExpect(jsonPath("$.teacher_id").value(teacher.getId().intValue()));
    }

    @Test
    @WithMockUser
    public void findById_SessionNotFound() throws Exception {
        mockMvc.perform(get("/api/session/100"))
            .andExpect(status().isNotFound());
    }

    // GET ALL SESSIONS
    @Test
    @WithMockUser
    public void findAll_ReturnAllSessions() throws Exception {
        Session session1 = new Session();
        session1.setName("Session 1 Name");
        session1.setDate(new Date());
        session1.setDescription("Session 1 Description");
        session1 = sessionRepository.save(session1);


        Session session2 = new Session();
        session2.setName("Session 2 Name");
        session2.setDate(new Date());
        session2.setDescription("Session 2 Description");
        session2 = sessionRepository.save(session2);

        mockMvc.perform(get("/api/session/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name").value("Session 1 Name"))
            .andExpect(jsonPath("$[1].name").value("Session 2 Name"));
    }

    // POST CREATE SESSION
    @Test
    @WithMockUser
    public void create_CreateSession() throws Exception {

        // ARRANGE
        Teacher teacher = new Teacher();
        teacher.setFirstName("Margot");
        teacher.setLastName("DELAHAYE");
        teacher = teacherRepository.save(teacher);

        String jsonSession = "{\"name\": \"Session Name\", \"date\": \"2022-01-01\", \"description\": \"Session Description\", \"teacher_id\": " + teacher.getId() + "}";

        // ACT
        mockMvc.perform(post("/api/session/")
            .contentType("application/json")
            .content(jsonSession))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Session Name"))
            .andExpect(jsonPath("$.teacher_id").value(teacher.getId()));
    }

    // UPDATE SESSION
    @Test
    @WithMockUser
    public void update_UpdateSession() throws Exception {
        
        // ARRANGE
        Teacher teacher = new Teacher();
        teacher.setFirstName("Margot");
        teacher.setLastName("DELAHAYE");
        teacher = teacherRepository.save(teacher);

        Session session = new Session();
        session.setName("Old session");
        session.setDate(new Date());
        session.setDescription("Old description");
        session.setTeacher(teacher);
        session = sessionRepository.save(session);

        String jsonSessionUpdated = "{\"name\": \"New session\", \"date\": \"2022-01-01\", \"description\": \"New description\", \"teacher_id\": " + teacher.getId() + "}";
    
        // ACT
        mockMvc.perform(put("/api/session/" + session.getId())
            .contentType("application/json")
            .content(jsonSessionUpdated))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("New session"))
            .andExpect(jsonPath("$.description").value("New description"));
    }

    // DELETE SESSION
    @Test
    @WithMockUser
    public void delete_DeleteSession() throws Exception {
        //ARRANGE
        Session session = new Session();
        session.setName("Old session");
        session.setDate(new Date());
        session.setDescription("Old description");
        session = sessionRepository.save(session);
        
        //ACT
        mockMvc.perform(delete("/api/session/" + session.getId()))
            .andExpect(status().isOk());

        assertThat(sessionRepository.findById(session.getId())).isEmpty();
    }
    
    @Test
    @WithMockUser
    public void delete_SessionNotFound() throws Exception {
        mockMvc.perform(delete("/api/session/100"))
            .andExpect(status().isNotFound());
    }

    // POST ADD PARTICIPANT
    @Test
    @WithMockUser
    public void participate_AddParticipant() throws Exception {

        Session session = new Session();
        session.setName("Session Name");
        session.setDate(new Date());
        session.setDescription("Session Description");
        session.setUsers(new ArrayList<>());
        session = sessionRepository.save(session);

        User user = new User();
        user.setEmail("user@user.com");
        user.setFirstName("First_User");
        user.setLastName("Last_User");
        user.setPassword("password");
        user.setAdmin(false);
        user = userRepository.save(user);

        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/" + user.getId()))
            .andExpect(status().isOk());

        Session sessionUpdated = sessionRepository.findById(session.getId()).get();
        assertThat(sessionUpdated.getUsers()).hasSize(1);
        assertEquals(sessionUpdated.getUsers().get(0), user);
        
    }

    // DELETE PARTICIPANT
    @Test
    @WithMockUser
    public void noLongerParticipate_DeleteParticipant() throws Exception {
        
        User user = new User();
        user.setEmail("user@user.com");
        user.setFirstName("First_User");
        user.setLastName("Last_User");
        user.setPassword("password");
        user.setAdmin(false);
        user = userRepository.save(user);

        Session session = new Session();
        session.setName("Session Name");
        session.setDate(new Date());
        session.setDescription("Session Description");
        session.setUsers(new ArrayList<>());
        session = sessionRepository.save(session);

        session.getUsers().add(user);
 
        mockMvc.perform(delete("/api/session/" + session.getId() + "/participate/" + user.getId()))
            .andExpect(status().isOk());

        Session sessionUpdated = sessionRepository.findById(session.getId()).get();
        assertThat(sessionUpdated.getUsers()).isEmpty();
    }
}
