package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:datas.sql")
public class SessionControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  ObjectMapper ObjMapper = new ObjectMapper();

  @Test
  public void testFindSessionById_SessionExists_ReturnsSessionDto() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/session/1")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
        .andExpect(status().isOk())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(200);
  }

  @Test
  public void testFindAllSessions() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
        .andExpect(status().isOk())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(200);

  }

  @Test
  public void testFindSessionById_SessionNotFound_ReturnsNotFound() throws Exception {
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.get("/api/session/999")
            .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
        .andExpect(status().isNotFound())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(404);
  }

  @Test
  public void testCreateSession_ValidData_ReturnsSessionDto() throws Exception {
    ObjectNode requestBody = ObjMapper.createObjectNode();
    requestBody.put("name", "Session Name");
    requestBody.put("description", "description");
    requestBody.put("date", "2023-10-15");
    requestBody.put("teacher_id", "1");

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com"))
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjMapper.writeValueAsString(requestBody)))
        .andExpect(status().isOk())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(200);
  }

  @Test
  public void testCreateSession_InvalidData_ReturnsBadRequest() throws Exception {
    ObjectNode requestBody = ObjMapper.createObjectNode();
    requestBody.put("description", "description");
    requestBody.put("date", "InvalidDate");

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com"))
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjMapper.writeValueAsString(requestBody)))
        .andExpect(status().isBadRequest())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(400);
  }

  @Test
  public void testUpdateSession_ValidInput() throws Exception {
    ObjectNode requestBody = ObjMapper.createObjectNode();
    requestBody.put("name", "Upddated Name");
    requestBody.put("description", "Updated description");
    requestBody.put("date", "2023-10-16");
    requestBody.put("teacher_id", "1");

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/session/1")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com"))
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjMapper.writeValueAsString(requestBody)))
        .andExpect(status().isOk())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(200);
  }

  @Test
  public void testUpdateSession_InvalidInput_ReturnsBadRequest() throws Exception {
    ObjectNode requestBody = ObjMapper.createObjectNode();
    requestBody.put("description", "description");
    requestBody.put("date", "InvalidDate");

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/session/1")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com"))
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjMapper.writeValueAsString(requestBody)))
        .andExpect(status().isBadRequest())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(400);
  }

  @Test
  public void testDeleteSession_ExistingSession() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/1")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
        .andExpect(status().isOk())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(200);
  }

  @Test
  public void testDeleteSession_NonExistingSession_ReturnsNotFound() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/999")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
        .andExpect(status().isNotFound())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(404);
  }

  @Test
  public void testParticipateInSession_ValidInput() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/session/1/participate/2")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
        .andExpect(status().isOk())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(200);
  }

  @Test
  public void testParticipateInSession_InvalidInput_ReturnsBadRequest() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/session/string/participate/2")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
        .andExpect(status().isBadRequest())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(400);
  }

  @Test
  public void testNoLongerParticipate_ValidInput() throws Exception {
    MvcResult participateResult = mockMvc
        .perform(MockMvcRequestBuilders.post("/api/session/1/participate/2")
            .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
        .andExpect(status().isOk())
        .andReturn();
    assertThat(participateResult.getResponse().getStatus()).isEqualTo(200);

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/1/participate/2")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
        .andExpect(status().isOk())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(200);
  }

  @Test
  public void testNoLongerParticipate_InvalidInput_ReturnsBadRequest() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/string/participate/2")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
        .andExpect(status().isBadRequest())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(400);
  }
}