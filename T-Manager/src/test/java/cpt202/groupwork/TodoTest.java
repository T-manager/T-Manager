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
public class TodoTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    // Implement the simulation of HTTP request
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    // POST:/todo/add
    @Test
    public void testAddTodoNormal() throws Exception {
        String requestBody = "{\"todoName\":\"this is a new name\", \"todolistId\":\"8\", \"todoDetail\":\"some detail\", \"todoDdl\":\"2021-05-23 09:28:44.755Z\", \"todoMember\":\"Sunnary\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/todo/add").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}"))).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void testAddTodoTodolistNotExist() throws Exception {
        String requestBody = "{\"todoName\":\"this is a new name\", \"todolistId\":\"0\", \"todoDetail\":\"some detail\", \"todoDdl\":\"2021-05-23 09:28:44.755Z\", \"todoMember\":\"Sunnary\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/todo/add").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string(equalTo("{\"status\":302,\"message\":\"todolist does not exist\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testAddTodoLongName() throws Exception {
        String requestBody = "{\"todoName\":\"this is a very long new name for todo\", \"todolistId\":\"8\", \"todoDetail\":\"some detail\", \"todoDdl\":\"2021-05-23 09:28:44.755Z\", \"todoMember\":\"Sunnary\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/todo/add").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo(
                        "{\"status\":341,\"message\":\"The TODO name should be between 1 and 20 characters\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testAddTodoShortName() throws Exception {
        String requestBody = "{\"todoName\":\"\", \"todolistId\":\"8\", \"todoDetail\":\"some detail\", \"todoDdl\":\"2021-05-23 09:28:44.755Z\", \"todoMember\":\"Sunnary\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/todo/add").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo(
                        "{\"status\":341,\"message\":\"The TODO name should be between 1 and 20 characters\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testAddTodoLongDetail() throws Exception {
        String requestBody = "{\"todoName\":\"new name for todo\", \"todolistId\":\"8\", \"todoDetail\":\"some detail, some detail, some detail, some detail, some detail, some detail, some detail, some detail, some detail,some detail, some detail, some detail, some detail, some detail, some detail,some detail, some detail, some detail, some detail, some detail, some detail, some detail,some detail, some detail, some detail, some detail, some detail, some detail, some detail, some detail, some detail\", \"todoDdl\":\"2021-05-23 09:28:44.755Z\", \"todoMember\":\"Sunnary\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/todo/add").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo(
                        "{\"status\":343,\"message\":\"The detail should be less than 100 characters\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testAddTodoShortDetail() throws Exception {
        String requestBody = "{\"todoName\":\"new name for todo\", \"todolistId\":\"8\", \"todoDetail\":\"\", \"todoDdl\":\"2021-05-23 09:28:44.755Z\", \"todoMember\":\"Sunnary\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/todo/add").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo(
                        "{\"status\":343,\"message\":\"The detail should be less than 100 characters\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    // Delete:/todo/delete/{todoId}
    @Test
    public void testDeleteTodoNormal() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/todo/delete/{todoId}", 10).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}"))).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void testDeleteTodoNotExist() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/todo/delete/{todoId}", 10000).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":301,\"message\":\"todo does not exist\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    // PUT: /todo/edit
    @Test
    public void testModifyTodoNormal() throws Exception {
        String requestBody = "{\"todoId\":\"10\", \"todolistId\":\"8\", \"todoName\":\"modify todoName\", \"todoDetail\":\"some detail\", \"todoDdl\":\"2021-05-23 09:28:44.755Z\", \"todoCheck\":\"false\", \"todoMember\":\"Sunnary\",\"todoMemberAvatar\":\"20210508155726.jpg\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.put("/todo/edit").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}"))).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void testModifyTodoNotExist() throws Exception {
        String requestBody = "{\"todoId\":\"499\", \"todolistId\":\"8\", \"todoName\":\"modify todoName\", \"todoDetail\":\"some detail\", \"todoDdl\":\"2021-05-23 09:28:44.755Z\", \"todoCheck\":\"false\", \"todoMember\":\"Sunnary\",\"todoMemberAvatar\":\"20210508155726.jpg\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.put("/todo/edit").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":301,\"message\":\"todo does not exist\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testModiftTodoLongName() throws Exception {
        String requestBody = "{\"todoId\":\"10\", \"todolistId\":\"8\", \"todoName\":\"Give todo a very lone new todoName\", \"todoDetail\":\"some detail\", \"todoDdl\":\"2021-05-23 09:28:44.755Z\", \"todoCheck\":\"false\", \"todoMember\":\"Sunnary\",\"todoMemberAvatar\":\"20210508155726.jpg\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.put("/todo/edit").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo(
                        "{\"status\":341,\"message\":\"The TODO name should be between 1 and 20 characters\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testModiftTodoShortName() throws Exception {
        String requestBody = "{\"todoId\":\"10\", \"todolistId\":\"8\", \"todoName\":\"\", \"todoDetail\":\"some detail\", \"todoDdl\":\"2021-05-23 09:28:44.755Z\", \"todoCheck\":\"false\", \"todoMember\":\"Sunnary\",\"todoMemberAvatar\":\"20210508155726.jpg\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.put("/todo/edit").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo(
                        "{\"status\":341,\"message\":\"The TODO name should be between 1 and 20 characters\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testModiftTodoLongDetail() throws Exception {
        String requestBody = "{\"todoId\":\"10\", \"todolistId\":\"8\", \"todoName\":\"new todoName\", \"todoDetail\":\"some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,some detail,\", \"todoDdl\":\"2021-05-23 09:28:44.755Z\", \"todoCheck\":\"false\", \"todoMember\":\"Sunnary\",\"todoMemberAvatar\":\"20210508155726.jpg\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.put("/todo/edit").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo(
                        "{\"status\":343,\"message\":\"The detail should be less than 100 characters\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testModiftTodoShortDetail() throws Exception {
        String requestBody = "{\"todoId\":\"10\", \"todolistId\":\"8\", \"todoName\":\"new todoName\", \"todoDetail\":\"\", \"todoDdl\":\"2021-05-23 09:28:44.755Z\", \"todoCheck\":\"false\", \"todoMember\":\"Sunnary\",\"todoMemberAvatar\":\"20210508155726.jpg\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.put("/todo/edit").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo(
                        "{\"status\":343,\"message\":\"The detail should be less than 100 characters\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testModifyUserNotExist() throws Exception {
        String requestBody = "{\"todoId\":\"10\", \"todolistId\":\"8\", \"todoName\":\"modify todoName\", \"todoDetail\":\"some detail\", \"todoDdl\":\"2021-05-23 09:28:44.755Z\", \"todoCheck\":\"false\", \"todoMember\":\"SomeOneNotExist\",\"todoMemberAvatar\":\"20210508155726.jpg\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.put("/todo/edit").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":307,\"message\":\"user does not exist\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    // PUT: /todo/check/{todoId}
    @Test
    public void testCheckTodoNormal() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/todo/check/{todoId}", 10).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}"))).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void testCheckTodoNotExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/todo/check/{todoId}", 499).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":301,\"message\":\"todo does not exist\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    // GET: /todo/get/{todoId}
    @Test
    public void testGetTodoNormal() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/todo/get/{todoId}", 10).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo(
                        "{\"status\":0,\"message\":\"Success\",\"data\":{\"todoId\":10,\"todolistId\":8,\"todoName\":\"1\",\"todoDetail\":\"1\",\"todoDdl\":\"2021-05-07 16:01:00\",\"todoMember\":2,\"todoCheck\":false}}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testGetTodoNotExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/todo/get/{todoId}", 499).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":301,\"message\":\"todo does not exist\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    // GET: /todo/get/member/{todoMember}
    @Test
    public void testGetTodoForMemberNormal() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/todo/get/member/{todoMember}", "Sunnary2").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo(
                        "{\"status\":0,\"message\":\"Success\",\"data\":[{\"todoId\":27,\"todoName\":\"REPORT\",\"projectId\":74,\"projectName\":\"new project\",\"todolistId\":13,\"todolistName\":\"CPT202\",\"todoDetail\":\"MEETING MINUTES\",\"todoDdl\":\"2021-05-25 16:00:00\",\"todoCheck\":false}]}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testGetTodoForMemberNotExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/todo/get/member/{todoMember}", "SomeoneNotExist").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string(equalTo("{\"status\":301,\"message\":\"executor does not exist\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    // GET /todo/get/{projectId}/search/{todoName}

    @Test
    public void testSearchTodoNormal() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/todo/get/{projectId}/search/{todoName}", 3, 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[{\"todolistId\":8,\"projectId\":3,\"projectName\":\"11\",\"todolistName\":\"123\",\"todolistTotalNum\":-5,\"todolistCompleteNum\":0,\"todoViewDTO\":[{\"todoId\":10,\"todolistId\":8,\"todoName\":\"1\",\"todoDetail\":\"1\",\"todoDdl\":\"2021-05-07 16:01:00\",\"todoCheck\":false,\"todoMember\":\"Sunnary\",\"todoMemberAvatar\":\"20210523122557.jpg\"}]}]}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testSearchTodoProjectNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/todo/get/{projectId}/search/{todoName}", 300, 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string(equalTo("{\"status\":302,\"message\":\"project does not exist\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

}
