package cpt202.groupwork;
import cpt202.groupwork.controller.ProjectController;
import cpt202.groupwork.service.ProjectService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class RelationTest {

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectController projectController;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void createRelationTest() throws Exception{
        String requestBody;
        requestBody = "{\"memberName\": \"user1\",\"projectId\": \"6\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/relation/add").content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.projectId").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.memberId").value(15))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.memberRole").value("member"))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"memberName\": \"user1\",\"projectId\": \"6\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/relation/add").content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":321,\"message\":\"user already exist in this project\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"memberName\": \"\",\"projectId\": \"6\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/relation/add").content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":341,\"message\":\"member name don't meet requirement\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"memberName\": \"user31415926\",\"projectId\": \"6\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/relation/add").content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":301,\"message\":\"user not found\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"memberName\": \"user1\",\"projectId\": \"-1\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/relation/add").content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":302,\"message\":\"project not found\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void getUserProject() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/relation/getproject/{username}","HangmingYe").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[{\"projectId\":4,\"projectMemberId\":3,\"projectName\":\"Test1\",\"projectDetail\":\"detail\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"personal\"},{\"projectId\":5,\"projectMemberId\":4,\"projectName\":\"TEST2\",\"projectDetail\":\"\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"team\"},{\"projectId\":6,\"projectMemberId\":5,\"projectName\":\"eSt123\",\"projectDetail\":\"detail123\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"team\"},{\"projectId\":53,\"projectMemberId\":58,\"projectName\":\"Delete\",\"projectDetail\":\"A project used for delete test\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"team\"}]}")))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/relation/getproject/{username}","Hangming").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":301,\"message\":\"user not found\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteRelationTest() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/relation/delete/{projectMemberId}", 58).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}"))).andReturn()
                .getResponse().getContentAsString();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/relation/delete/{projectMemberId}", -1).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":301,\"message\":\"relation not found\",\"data\":null}"))).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void getProjectUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/relation/getuser/{projectId}","6").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[{\"projectMemberId\":5,\"projectId\":6,\"memberId\":57,\"memberName\":\"HangmingYe\",\"memberAvatar\":\"default.jpg\",\"memberRole\":\"owner\"},{\"projectMemberId\":102,\"projectId\":6,\"memberId\":16,\"memberName\":\"user2\",\"memberAvatar\":\"default.jpg\",\"memberRole\":\"member\"}]}")))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/relation/getuser/{projectId}","0").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":301,\"message\":\"Project not found\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }
}