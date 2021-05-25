package cpt202.groupwork;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class MissionTest {
  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  // Implement the simulation of HTTP request
  @Before
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  // POST:/mission/add
  @Test
  public void testAddMissionNormal() throws Exception {
    String requestBody = "{\"missionId\": 2,\n"
        + "    \"missionName\": \"string\",\n"
        + "    \"ganttId\": 10,\n"
        + "    \"missionStart\": \"2021-05-23 06:32:11\",\n"
        + "    \"missionDuration\": 0,\n"
        + "    \"missionProgress\": 0,\n"
        + "    \"missionParent\": 0}";
    mockMvc.perform(
        MockMvcRequestBuilders.post("/mission/add").content(requestBody).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}"))).andReturn()
        .getResponse().getContentAsString();
  }

  @Test
  public void testAddMissionGanttNotExist() throws Exception {
    String requestBody =  "{\"missionId\": 2,\n"
        + "    \"missionName\": \"string\",\n"
        + "    \"ganttId\": 1000,\n"
        + "    \"missionStart\": \"2021-05-23 06:32:11\",\n"
        + "    \"missionDuration\": 0,\n"
        + "    \"missionProgress\": 0,\n"
        + "    \"missionParent\": 0}";
    mockMvc.perform(
        MockMvcRequestBuilders.post("/mission/add").content(requestBody).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content()
            .string(equalTo("{\"status\":301,\"message\":\"gantt does not exist\",\"data\":null}")))
        .andReturn().getResponse().getContentAsString();
  }

  @Test
  public void testAddMissionLongName() throws Exception {
    String requestBody =  "{ \"missionId\": 2,\n"
        + "    \"missionName\": \"this is a new very long mission name\",\n"
        + "    \"ganttId\": 10,\n"
        + "    \"missionStart\": \"2021-05-23 06:32:11\",\n"
        + "    \"missionDuration\": 0,\n"
        + "    \"missionProgress\": 0,\n"
        + "    \"missionParent\": 0}";
    mockMvc.perform(
        MockMvcRequestBuilders.post("/mission/add").content(requestBody).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo(
            "{\"status\":342,\"message\":\"mission name should be between 1 and 20 characters\",\"data\":null}")))
        .andReturn().getResponse().getContentAsString();
  }

  @Test
  public void testAddMissionShortName() throws Exception {
    String requestBody = "{\"missionId\": 2,\n"
        + "    \"missionName\": \"\",\n"
        + "    \"ganttId\": 10,\n"
        + "    \"missionStart\": \"2021-05-23 06:32:11\",\n"
        + "    \"missionDuration\": 0,\n"
        + "    \"missionProgress\": 0,\n"
        + "    \"missionParent\": 0}";
    mockMvc.perform(
        MockMvcRequestBuilders.post("/mission/add").content(requestBody).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo(
            "{\"status\":342,\"message\":\"mission name should be between 1 and 20 characters\",\"data\":null}")))
        .andReturn().getResponse().getContentAsString();
  }

  // Delete:/mission/delete/{todoId}
  @Test
  public void testDeleteMissionNormal() throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.delete("/mission/delete/{missionId}", 2).accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}"))).andReturn()
        .getResponse().getContentAsString();
  }

  @Test
  public void testDeleteMissionNotExist() throws Exception {

    mockMvc.perform(
        MockMvcRequestBuilders.delete("/mission/delete/{missionId}", 10000).accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            content().string(equalTo("{\"status\":301,\"message\":\"mission does not exist\",\"data\":null}")))
        .andReturn().getResponse().getContentAsString();
  }

  // PUT: /todo/edit
  @Test
  public void testModifyMissionNormal() throws Exception {
    String requestBody = "{\"missionId\": 2,\n"
        + "    \"missionName\": \"string\",\n"
        + "    \"ganttId\": 10,\n"
        + "    \"missionStart\": \"2021-05-23 06:32:11\",\n"
        + "    \"missionDuration\": 0,\n"
        + "    \"missionProgress\": 0,\n"
        + "    \"missionParent\": 0}";
    mockMvc.perform(
        MockMvcRequestBuilders.put("/mission/edit").content(requestBody).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}"))).andReturn()
        .getResponse().getContentAsString();
  }

  @Test
  public void testModifyMissionNotExist() throws Exception {
    String requestBody = "{\"missionId\": 10000,\n"
        + "    \"missionName\": \"mission name\",\n"
        + "    \"ganttId\": 10,\n"
        + "    \"missionStart\": \"2021-05-23 06:32:11\",\n"
        + "    \"missionDuration\": 0,\n"
        + "    \"missionProgress\": 0,\n"
        + "    \"missionParent\": 0}";
    mockMvc.perform(
        MockMvcRequestBuilders.put("/mission/edit").content(requestBody).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            content().string(equalTo("{\"status\":301,\"message\":\"mission does not exist\",\"data\":null}")))
        .andReturn().getResponse().getContentAsString();
  }

  @Test
  public void testModifyMissionLongName() throws Exception {
    String requestBody = "{\"missionId\": 2,\n"
        + "    \"missionName\": \"this is a very long mission name\",\n"
        + "    \"ganttId\": 10,\n"
        + "    \"missionStart\": \"2021-05-23 06:32:11\",\n"
        + "    \"missionDuration\": 0,\n"
        + "    \"missionProgress\": 0,\n"
        + "    \"missionParent\": 0}";
    mockMvc.perform(
        MockMvcRequestBuilders.put("/mission/edit").content(requestBody).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo(
            "{\"status\":342,\"message\":\"mission name should be between 1 and 20 characters\",\"data\":null}")))
        .andReturn().getResponse().getContentAsString();
  }

  @Test
  public void testModifyMissionShortName() throws Exception {
    String requestBody ="{\"missionId\": 2,\n"
        + "    \"missionName\": \"\",\n"
        + "    \"ganttId\": 10,\n"
        + "    \"missionStart\": \"2021-05-23 06:32:11\",\n"
        + "    \"missionDuration\": 0,\n"
        + "    \"missionProgress\": 0,\n"
        + "    \"missionParent\": 0}";
    mockMvc.perform(
        MockMvcRequestBuilders.put("/mission/edit").content(requestBody).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo(
            "{\"status\":342,\"message\":\"mission name should be between 1 and 20 characters\",\"data\":null}")))
        .andReturn().getResponse().getContentAsString();
  }


  // GET: /mission/get/{todoId}
  @Test
  public void testGetMissionNormal() throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.get("/mission/get/{todoId}", 2).accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo(
            "{\"status\":0,\"message\":\"Success\",\"data\":{\"missionId\":2,\"ganttId\":10,\"missionName\":\"string\",\"missionStart\":\"2021-05-23 06:32:11\",\"missionDuration\":0,\"missionProgress\":0.0,\"missionParent\":0}}")))
        .andReturn().getResponse().getContentAsString();
  }

  @Test
  public void testGetMissionNotExist() throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.get("/mission/get/{todoId}", 499).accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            content().string(equalTo("{\"status\":301,\"message\":\"mission does not exist\",\"data\":null}")))
        .andReturn().getResponse().getContentAsString();
  }



}
