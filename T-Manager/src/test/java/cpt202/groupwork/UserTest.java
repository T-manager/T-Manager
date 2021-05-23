package cpt202.groupwork;

/**
 * @description:
 * @author: cpt202group2
 * @create:
 **/

import com.jayway.jsonpath.JsonPath;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @Before
  public void setup(){
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }
  @Test
  public void testUserController() throws Exception {
    //GET auth/check
    mockMvc.perform(
        MockMvcRequestBuilders.get("/auth/check")
            .param("username", "0")
    ).andExpect(MockMvcResultMatchers.status().isOk())

        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Fail"));
//        .andExpect(content().string(
//            equalTo("{\"status\":200,\"message\":\"Fail\",\"data\":2002}")
//        ));
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/auth/check")
                .param("username", "test")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            content()
                .string(
                    equalTo(
                        "{\"status\":200,\"message\":\"Success\",\"data\":2000}")));
    String content =
        "{\"userName\":\"test\", \"userEmail\" : \"xianghao.niu18@student.xjtlu.edu.cn\"}";

    //POST auth check
    String result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/check")
        .content(content).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(
            equalTo("{\"status\":200,\"message\":\"Success\",\"data\":2000}")))
        .andReturn().getResponse().getContentAsString();
    System.out.println(result);

//     POST:/auth/register
    result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/auth/register")
                    .content(
                        "{\n"
                            + "    \"userName\":\"email\",\n"
                            + "    \"userPassword\": \"admin\",\n"
                            + "    \"userEmail\":\"kaiwen.li18@student.xjtlu.edu.cn\"\n"
                            + "   \n"
                            + "}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                content().string(equalTo("{\"status\":200,\"message\":\"Success\",\"data\":1000}")))
            .andReturn()
            .getResponse()
            .getContentAsString();
    System.out.println(result);

    //     POST:/auth/login
    result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/auth/login")
                    .content(
                        "{\n"
                            + "    \"userName\":\"test\",\n"
                            + "    \"userPassword\": \"admin\",\n"
                            + "    \"userEmail\":\"kaiwen.li18@student.xjtlu.edu.cn\"\n"
                            + "   \n"
                            + "}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                content().string(equalTo("{\"status\":200,\"message\":\"Fail\",\"data\":2001}")))
            .andReturn()
            .getResponse()
            .getContentAsString();
    System.out.println(result);

    //     POST:/auth/login
    result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/auth/login")
                    .content(
                        "{\n"
                            + "    \"userName\":\"email\",\n"
                            + "    \"userPassword\": \"admin\",\n"
                            + "    \"userEmail\":\"kaiwen.li18@student.xjtlu.edu.cn\"\n"
                            + "   \n"
                            + "}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andExpect(
//                content().string(equalTo("{\"status\":200,\"message\":\"Success\",\"data\":1000}")))
            .andReturn()
            .getResponse()
            .getContentAsString();
    System.out.println(result);
    String token = JsonPath.parse(result).read("$.data");
    System.out.println(token);

    //get token test
    result=mockMvc.perform(
        MockMvcRequestBuilders.get("/user/get/email")
            .param("token", token)
    ).andExpect(MockMvcResultMatchers.status().isOk())

        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Success"))
        .andReturn()
        .getResponse()
        .getContentAsString();
    System.out.println(result);

    // put
    result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.put("/user/edit/email")
                    .param("token", token)
                    .content(
                        "{\n"
                            + "    \"userName\":\"email\",\n"
                            + "    \"userPassword\": \"admin2\",\n"
                            + "    \"userEmail\":\"kaiwen.li18@student.xjtlu.edu.cn\"\n"
                            + "   \n"
                            + "}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Success"))
            .andReturn()
            .getResponse()
            .getContentAsString();
    System.out.println(result);

    //delete
    result=mockMvc.perform(
        MockMvcRequestBuilders.delete("/user/delete/email")
            .param("token", token)
    ).andExpect(MockMvcResultMatchers.status().isOk())

        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Success"))
        .andReturn()
        .getResponse()
        .getContentAsString();
    System.out.println(result);
  }
}