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
public class GanttTest {
  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  // Implement the simulation of HTTP request
  @Before
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  // POST:/gantt/add
  @Test
  public void testAddGanttNormal() throws Exception {
    String requestBody = "{\"ganttName\":\"this is a new name\", \"projectId\":\"3\"}";
    mockMvc.perform(MockMvcRequestBuilders.post("/gantt/add").content(requestBody)
        .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}"))).andReturn()
        .getResponse().getContentAsString();
  }

  @Test
  public void testAddGanttProjectNotExist() throws Exception {
    String requestBody = "{\"ganttName\":\"this is a new name\", \"projectId\":\"0\"}";
    mockMvc.perform(MockMvcRequestBuilders
        .post("/gantt/add").content(requestBody).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content()
            .string(equalTo("{\"status\":302,\"message\":\"project does not exist\",\"data\":null}")))
        .andReturn().getResponse().getContentAsString();
  }

  // Delete:/gantt/delete/{ganttId}
  @Test
  public void testDeleteGanttNormal() throws Exception {

    mockMvc.perform(
        MockMvcRequestBuilders.delete("/gantt/delete/{ganttId}", 6).accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}"))).andReturn()
        .getResponse().getContentAsString();
  }

  @Test
  public void testDeleteGanttNotExist() throws Exception {

    mockMvc.perform(MockMvcRequestBuilders
        .delete("/gantt/delete/{ganttId}", 5).accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content()
            .string(equalTo("{\"status\":301,\"message\":\"gantt chart does not exist\",\"data\":null}")))
        .andReturn().getResponse().getContentAsString();
  }

  // PUT: /gantt/modify
  @Test
  public void testModifyGanttNormal() throws Exception {
    String requestBody = "{\"ganttId\":\"6\", \"projectId\":\"30\", \"ganttName\":\"string\",\"ganttDetail\":\"some detail\", \"ganttTotalNum\":\"0\"}";
    mockMvc.perform(MockMvcRequestBuilders
        .put("/gantt/modify").content(requestBody).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":\"modify success\"}")))
        .andReturn().getResponse().getContentAsString();
  }

  @Test
  public void testModifyGanttNotExist() throws Exception {
    String requestBody = "{\"ganttId\":\"5\", \"projectId\":\"30\", \"ganttName\":\"string\", \"ganttTotalNum\":\"0\", \"ganttCompleteNum\":\"0\"}";
    mockMvc.perform(MockMvcRequestBuilders
        .put("/gantt/modify").content(requestBody).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content()
            .string(equalTo("{\"status\":301,\"message\":\"gantt does not exist\",\"data\":null}")))
        .andReturn().getResponse().getContentAsString();
  }



  // Get /gantt/get/{projectId}
  @Test
  public void testGetGanttNormal() throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.get("/gantt/get/{projectId}", 31).accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[{\"ganttId\":39,\"projectId\":31,\"projectName\":\"project 1\",\"ganttName\":\"totolist 1\",\"ganttTotalNum\":2,\"ganttCompleteNum\":1,\"todoViewDTO\":[{\"todoId\":20,\"ganttId\":39,\"todoName\":\"123\",\"todoDetail\":\"\",\"todoDdl\":\"2021-05-07 15:55:00\",\"todoCheck\":true,\"todoMember\":\"all\",\"todoMemberAvatar\":\"20210506080440.jpg\"},{\"todoId\":21,\"ganttId\":39,\"todoName\":\"lunar\",\"todoDetail\":\"123\",\"todoDdl\":\"2021-05-21 14:51:00\",\"todoCheck\":false,\"todoMember\":\"all\",\"todoMemberAvatar\":\"20210506080440.jpg\"}]},{\"ganttId\":40,\"projectId\":31,\"projectName\":\"project 1\",\"ganttName\":\"\",\"ganttTotalNum\":0,\"ganttCompleteNum\":0,\"todoViewDTO\":[]},{\"ganttId\":48,\"projectId\":31,\"projectName\":\"project 1\",\"ganttName\":\"\",\"ganttTotalNum\":0,\"ganttCompleteNum\":0,\"todoViewDTO\":[]}]}"))).andReturn()
        .getResponse().getContentAsString();
  }

  @Test
  public void testGetGanttNotExist() throws Exception {

    mockMvc.perform(
        MockMvcRequestBuilders.get("/gantt/get/{projectId}", 10000).accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            content().string(equalTo("{\"status\":301,\"message\":\"project not exist\",\"data\":null}")))
        .andReturn().getResponse().getContentAsString();
  }

}
