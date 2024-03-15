package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:datas.sql")
public class UserControllerIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void FindById_userFound_returnUserDto() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
        .andExpect(status().is(200))
        .andReturn();

    String responseContent = result.getResponse().getContentAsString();

    User resultUser = objectMapper.readValue(responseContent, User.class);
    assertThat(resultUser.getFirstName()).isEqualTo("Admin");
  }

  @Test
  public void testFindById_userNotFound_returnNotFound() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/999")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
        .andExpect(status().isNotFound())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(404);
  }

  @Test
  public void testFindById_invalidId_returnBadRequest() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/invalidId")
        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
        .andExpect(status().isBadRequest())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(400);
  }

  @Test
  public void testDeleteUserById_userFound_return200() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/2")
        .with(SecurityMockMvcRequestPostProcessors.user("plop@plop.com")))
        .andExpect(status().is(200))
        .andReturn();

    assertThat(result.getResponse().getStatus()).isEqualTo(200);
  }

  @Test
  public void testDeleteUserById_userNotFound_returnNotFound() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/999")
        .with(SecurityMockMvcRequestPostProcessors.user("plop@plop.com")))
        .andExpect(status().isNotFound())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(404);
  }

  @Test
  public void testDeleteUserById_unauthorizedUser_returnUnauthorized() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/2")
        .with(SecurityMockMvcRequestPostProcessors.user("newuser@example.com")))
        .andExpect(status().isUnauthorized())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(401);
  }

  @Test
  public void testDeleteUserById_invalidId_returnBadRequest() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/invalidId")
        .with(SecurityMockMvcRequestPostProcessors.user("plop@plop.com")))
        .andExpect(status().isBadRequest())
        .andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(400);
  }

}