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
public class ProjectTest {

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

    public void createProjectTest() throws Exception{
        String requestBody;
        requestBody = "{\"projectName\": \"Project\",\"projectDetail\": \"detail\",\"projectOwner\": \"HangmingYe\", \"projectType\": \"team\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/project/add").content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"projectName\": \"\",\"projectDetail\": \"detail\",\"projectOwner\": \"HangmingYe\", \"projectType\": \"team\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/project/add").content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":341,\"message\":\"project name don't meet the requirement\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"projectName\": \"abcdefghijklmnopqrstuvwxyz\",\"projectDetail\": \"detail\",\"projectOwner\": \"HangmingYe\", \"projectType\": \"personal\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/project/add").content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":341,\"message\":\"project name don't meet the requirement\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"projectName\": \"Project\",\"projectDetail\": \"\",\"projectOwner\": \"HangmingYe\", \"projectType\": \"personal\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/project/add").content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"projectName\": \"Project\",\"projectDetail\": \"detail\",\"projectOwner\": \"\", \"projectType\": \"team\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/project/add").content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":343,\"message\":\"project owner don't meet the requirement\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"projectName\": \"Project\",\"projectDetail\": \"detail\",\"projectOwner\": \"Hangming\", \"projectType\": \"team\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/project/add").content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":303,\"message\":\"project owner does not exist\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"projectName\": \"Project\",\"projectDetail\": \"detail\",\"projectOwner\": \"HangmingYe\", \"projectType\": \"tttttteam\"}";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/project/add").content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":344,\"message\":\"project type don't meet the requirement\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteProjectTest() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/project/delete/{projectId}", 53).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}"))).andReturn()
                .getResponse().getContentAsString();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/project/delete/{projectId}", 0).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":300,\"message\":\"project not find\",\"data\":null}"))).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void modifyProjectTest() throws  Exception{
        String requestBody;
        requestBody = "{\"projectId\": \"5\",\"projectName\": \"New Name\",\"projectDetail\": \"detail\", \"projectType\": \"team\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/project/modify").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":{\"projectId\":5,\"projectName\":\"New Name\",\"projectDetail\":\"detail\",\"projectOwnerId\":57,\"projectType\":\"team\"}}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"projectId\": \"\",\"projectName\": \"New Name\",\"projectDetail\": \"detail\", \"projectType\": \"team\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/project/modify").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":340,\"message\":\"\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"projectId\": \"5\",\"projectName\": \"abcdefghijklmnopqrstuvwxyz\",\"projectDetail\": \"detail\", \"projectType\": \"team\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/project/modify").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":341,\"message\":\"project name don't meet the requirement\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"projectId\": \"5\",\"projectName\": \"\",\"projectDetail\": \"detail\", \"projectType\": \"team\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/project/modify").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":341,\"message\":\"project name don't meet the requirement\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"projectId\": \"5\",\"projectName\": \"New Name\",\"projectDetail\": \"detail\", \"projectType\": \"tttttteam\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/project/modify").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":344,\"message\":\"project type don't meet the requirement\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"projectId\": \"5\",\"projectName\": \"New Name\",\"projectDetail\": \"detail\", \"projectType\": \"personal\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/project/modify").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":314,\"message\":\"project type cannot be change\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();

        requestBody = "{\"projectId\": \"0\",\"projectName\": \"New Name\",\"projectDetail\": \"detail\", \"projectType\": \"team\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/project/modify").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":301,\"message\":\"project not find\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void getProjectTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/project/get/{projectId}", 53).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":{\"projectId\":53,\"projectName\":\"Delete\",\"projectDetail\":\"A project used for delete test\",\"projectOwnerId\":57,\"projectType\":\"team\"}}"))).andReturn()
                .getResponse().getContentAsString();

        mockMvc.perform(MockMvcRequestBuilders.get("/project/get/{projectId}", 0).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":300,\"message\":\"project not found\",\"data\":null}"))).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void getProjectV2Test() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/project/getV2/{projectId}", 53).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":{\"projectName\":\"Delete\",\"projectDetail\":\"A project used for delete test\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"team\"}}"))).andReturn()
                .getResponse().getContentAsString();

        mockMvc.perform(MockMvcRequestBuilders.get("/project/getV2/{projectId}", 0).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("{\"status\":300,\"message\":\"project not found\",\"data\":null}"))).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void searchUserProjectTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/project/search/{userName}/{namePattern}","HangmingYe","Test").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[{\"projectId\":4,\"projectMemberId\":3,\"projectName\":\"Test1\",\"projectDetail\":\"detail\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"personal\"},{\"projectId\":5,\"projectMemberId\":4,\"projectName\":\"TEST2\",\"projectDetail\":\"\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"team\"}]}")))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/project/search/{userName}/{namePattern}","HangmingYe","TEST").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[{\"projectId\":4,\"projectMemberId\":3,\"projectName\":\"Test1\",\"projectDetail\":\"detail\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"personal\"},{\"projectId\":5,\"projectMemberId\":4,\"projectName\":\"TEST2\",\"projectDetail\":\"\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"team\"}]}")))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/project/search/{userName}/{namePattern}","HangmingYe","1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[{\"projectId\":4,\"projectMemberId\":3,\"projectName\":\"Test1\",\"projectDetail\":\"detail\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"personal\"},{\"projectId\":6,\"projectMemberId\":5,\"projectName\":\"eSt123\",\"projectDetail\":\"detail123\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"team\"}]}")))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/project/search/{userName}/{namePattern}","HangmingYe","est").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[{\"projectId\":4,\"projectMemberId\":3,\"projectName\":\"Test1\",\"projectDetail\":\"detail\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"personal\"},{\"projectId\":5,\"projectMemberId\":4,\"projectName\":\"TEST2\",\"projectDetail\":\"\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"team\"},{\"projectId\":6,\"projectMemberId\":5,\"projectName\":\"eSt123\",\"projectDetail\":\"detail123\",\"projectOwner\":\"HangmingYe\",\"projectType\":\"team\"}]}")))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/project/search/{userName}/{namePattern}","HangmingYe","0").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":0,\"message\":\"Success\",\"data\":[]}")))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/project/search/{userName}/{namePattern}","Hangming","TEST").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        content().string(equalTo("{\"status\":301,\"message\":\"User not found\",\"data\":null}")))
                .andReturn().getResponse().getContentAsString();
    }


}