package com.openclassrooms.starterjwt.controllers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:datas.sql")
public class AuthControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  ObjectMapper ObjMapper = new ObjectMapper();

  @Test
  public void testAuthenticateUser() throws Exception {
    ObjectNode requestBody = ObjMapper.createObjectNode();
    requestBody.put("email", "yoga@studio.com");
    requestBody.put("password", "test!1234");

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjMapper.writeValueAsString(requestBody)))
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(200);
  }

  @Test
  public void testRegisterUser() throws Exception {
    ObjectNode requestBody = ObjMapper.createObjectNode();
    requestBody.put("email", "test@example.com");
    requestBody.put("password", "password");
    requestBody.put("firstName", "Test");
    requestBody.put("lastName", "Exemple");

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjMapper.writeValueAsString(requestBody)))
        .andExpect(status().isOk())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(200);
  }

  @Test
  public void testRegisterUserEmailAlreadyTaken() throws Exception {
    ObjectNode requestBody = ObjMapper.createObjectNode();
    requestBody.put("email", "yoga@studio.com");
    requestBody.put("password", "password");
    requestBody.put("firstName", "Test");
    requestBody.put("lastName", "Exemple");

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjMapper.writeValueAsString(requestBody)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Expecting a bad request
        .andReturn();

    String responseContent = result.getResponse().getContentAsString();
    assertThat(responseContent).contains("Error: Email is already taken!");
  }

}