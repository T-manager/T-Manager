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
public class TodoListTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    // Implement the simulation of HTTP request
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    // POST:/todolist/add
    @Test
    public void testAddTodolistNormal() throws Exception {
        String requestBody = "{\"todolistName\":\"this is a new name\", \"projectId\":\"1\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/todolist/add").content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}"))).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void testAddTodolistProjectNotExist() throws Exception {
        String requestBody = "{\"todolistName\":\"this is a new name\", \"projectId\":\"0\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .post("/todolist/add").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string(equalTo("{\"status\":302,\"message\":\"project does not exist\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testAddTodolistLongName() throws Exception {
        String requestBody = "{\"todolistName\":\"this is a very long new name\", \"projectId\":\"1\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .post("/todolist/add").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo(
                        "{\"status\":341,\"message\":\"The TODOLIST name should be between 1 and 20 characters\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testAddTodolistShortName() throws Exception {
        String requestBody = "{\"todolistName\":\"\", \"projectId\":\"1\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .post("/todolist/add").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo(
                        "{\"status\":341,\"message\":\"The TODOLIST name should be between 1 and 20 characters\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    // Delete:/todolist/delete/{todolistId}
    @Test
    public void testDeleteTodolistNormal() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/todolist/delete/{todolistId}", 10).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}"))).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void testDeleteTodolistNotExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/todolist/delete/{todolistId}", 10000).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string(equalTo("{\"status\":301,\"message\":\"todolist does not exist\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    // PUT: /todolist/modify
    @Test
    public void testModifyTodolistNormal() throws Exception {
        String requestBody = "{\"todolistId\":\"10\", \"projectId\":\"3\", \"todolistName\":\"string\", \"todolistTotalNum\":\"0\", \"todolistCompleteNum\":\"0\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/todolist/modify").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":\"modify success\"}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testModifyTodolistNotExist() throws Exception {
        String requestBody = "{\"todolistId\":\"84\", \"projectId\":\"3\", \"todolistName\":\"string\", \"todolistTotalNum\":\"0\", \"todolistCompleteNum\":\"0\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/todolist/modify").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string(equalTo("{\"status\":301,\"message\":\"todolist does not exist\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testModiftTodolistLongName() throws Exception {
        String requestBody = "{\"todolistId\":\"10\", \"projectId\":\"3\", \"todolistName\":\"This is a very long name for todolist\", \"todolistTotalNum\":\"0\", \"todolistCompleteNum\":\"0\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/todolist/modify").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo(
                        "{\"status\":341,\"message\":\"The TODOLIST name should be between 1 and 20 characters\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testModiftTodolistShortName() throws Exception {
        String requestBody = "{\"todolistId\":\"10\", \"projectId\":\"3\", \"todolistName\":\"\", \"todolistTotalNum\":\"0\", \"todolistCompleteNum\":\"0\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/todolist/modify").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo(
                        "{\"status\":341,\"message\":\"The TODOLIST name should be between 1 and 20 characters\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    // Get /todolist/get/{projectId}
    @Test
    public void testGetTodolistNormal() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/todolist/get/{projectId}", 3).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[{\"todolistId\":8,\"projectId\":3,\"projectName\":\"11\",\"todolistName\":\"123\",\"todolistTotalNum\":-5,\"todolistCompleteNum\":0,\"todoViewDTO\":[{\"todoId\":10,\"todolistId\":8,\"todoName\":\"1\",\"todoDetail\":\"1\",\"todoDdl\":\"2021-05-07 16:01:00\",\"todoCheck\":false,\"todoMember\":\"Sunnary\",\"todoMemberAvatar\":\"20210523122557.jpg\"}]}]}"))).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void testGetTodolistNotExist() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/todolist/get/{projectId}", 10000).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":301,\"message\":\"project not exist\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

}
