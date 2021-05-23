package cpt202.groupwork.service;
import cpt202.groupwork.controller.ProjectController;
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
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest

public class RelationServiceTest {

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
    public void getUserProject() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/relation/getproject/{username}","HangmingYe").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[{\"projectId\":88,\"projectMemberId\":120,\"projectName\":\"eSt123\",\"projectDetail\":\"abcd\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"team\"},{\"projectId\":87,\"projectMemberId\":119,\"projectName\":\"TEST2\",\"projectDetail\":\"222\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"team\"},{\"projectId\":86,\"projectMemberId\":118,\"projectName\":\"Test1\",\"projectDetail\":\"1\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"personal\"}]}")))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/relation/getproject/{username}","Hangming").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":301,\"message\":\"user not found\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void getProjectUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/relation/getuser/{projectId}","88").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[{\"projectMemberId\":120,\"projectId\":88,\"memberId\":57,\"memberName\":\"HangmingYe\",\"memberAvatar\":\"default.jpg\",\"memberRole\":\"owner\"},{\"projectMemberId\":121,\"projectId\":88,\"memberId\":20,\"memberName\":\"user3\",\"memberAvatar\":\"default.jpg\",\"memberRole\":\"member\"}]}")))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/relation/getuser/{projectId}","0").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":301,\"message\":\"Project not found\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }
}