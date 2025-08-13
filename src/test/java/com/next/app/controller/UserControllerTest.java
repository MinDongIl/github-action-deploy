//package com.next.app.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.next.app.api.user.controller.UserController;
//import com.next.app.api.user.entity.User;
//import com.next.app.api.user.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(UserController.class)
//class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private User testUser;
//
//    @BeforeEach
//    void setUp() {
//        testUser = new User();
//        testUser.setId(1L);
//        testUser.setEmail("test@example.com");
//        testUser.setName("테스트 사용자");
//        testUser.setPhone_number("010-1234-5678");
//        testUser.setCreatedAt(LocalDateTime.now());
//    }
//
//    @Test
//    void getAllUsers_ShouldReturnUserList() throws Exception {
//        // Given
//        when(userService.getAllUsers()).thenReturn(Arrays.asList(testUser));
//
//        // When & Then
//        mockMvc.perform(get("/api/users"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].id").value(1))
//                .andExpect(jsonPath("$[0].email").value("test@example.com"))
//                .andExpect(jsonPath("$[0].name").value("테스트 사용자"));
//
//        verify(userService, times(1)).getAllUsers();
//    }
//
//    @Test
//    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
//        // Given
//        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));
//
//        // When & Then
//        mockMvc.perform(get("/api/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("test@example.com"))
//                .andExpect(jsonPath("$.name").value("테스트 사용자"));
//
//        verify(userService, times(1)).getUserById(1L);
//    }
//
//    @Test
//    void getUserById_WhenUserNotExists_ShouldReturn404() throws Exception {
//        // Given
//        when(userService.getUserById(999L)).thenReturn(Optional.empty());
//
//        // When & Then
//        mockMvc.perform(get("/api/users/999"))
//                .andExpect(status().isNotFound());
//
//        verify(userService, times(1)).getUserById(999L);
//    }
//
//    @Test
//    void createUser_WhenValidUser_ShouldReturnCreatedUser() throws Exception {
//        // Given
//        User newUser = new User();
//        newUser.setEmail("new@example.com");
//        newUser.setName("새 사용자");
//        newUser.setPhone_number("010-9876-5432");
//
//        User createdUser = new User();
//        createdUser.setId(2L);
//        createdUser.setEmail("new@example.com");
//        createdUser.setName("새 사용자");
//        createdUser.setPhone_number("010-9876-5432");
//        createdUser.setCreatedAt(LocalDateTime.now());
//
//        when(userService.createUser(any(User.class))).thenReturn(createdUser);
//
//        // When & Then
//        mockMvc.perform(post("/api/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newUser)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(2))
//                .andExpect(jsonPath("$.email").value("new@example.com"))
//                .andExpect(jsonPath("$.name").value("새 사용자"));
//
//        verify(userService, times(1)).createUser(any(User.class));
//    }
//
//    @Test
//    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() throws Exception {
//        // Given
//        User updateUser = new User();
//        updateUser.setName("수정된 사용자");
//        updateUser.setPhone_number("010-1111-2222");
//
//        User updatedUser = new User();
//        updatedUser.setId(1L);
//        updatedUser.setEmail("test@example.com");
//        updatedUser.setName("수정된 사용자");
//        updatedUser.setPhone_number("010-1111-2222");
//        updatedUser.setCreatedAt(LocalDateTime.now());
//
//        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);
//
//        // When & Then
//        mockMvc.perform(put("/api/users/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateUser)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("수정된 사용자"))
//                .andExpect(jsonPath("$.phone_number").value("010-1111-2222"));
//
//        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
//    }
//
//    @Test
//    void updateUser_WhenUserNotExists_ShouldReturn404() throws Exception {
//        // Given
//        User updateUser = new User();
//        updateUser.setName("수정된 사용자");
//
//        when(userService.updateUser(eq(999L), any(User.class)))
//                .thenThrow(new RuntimeException("사용자를 찾을 수 없습니다: 999"));
//
//        // When & Then
//        mockMvc.perform(put("/api/users/999")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateUser)))
//                .andExpect(status().isNotFound());
//
//        verify(userService, times(1)).updateUser(eq(999L), any(User.class));
//    }
//
//    @Test
//    void deleteUser_ShouldReturn200() throws Exception {
//        // Given
//        doNothing().when(userService).deleteUser(1L);
//
//        // When & Then
//        mockMvc.perform(delete("/api/users/1"))
//                .andExpect(status().isOk());
//
//        verify(userService, times(1)).deleteUser(1L);
//    }
//}