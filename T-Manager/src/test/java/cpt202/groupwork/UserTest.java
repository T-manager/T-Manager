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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
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
  public void postControllerTest(String url,String req,String resp,String token) throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders
                .post(url)
                .param("token", token)
                .content(req)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo(resp)));
  }
  public void deleteControllerTest(String url,String req,String resp,String token) throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders
                .delete(url)
                .param("token", token)
                .content(req)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo(resp)));
  }

  public void putControllerTest(String url,String req,String resp,String token) throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders
                .put(url)
                .param("token", token)
                .content(req)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo(resp)));
  }
  public void getControllerTest(String url,String resp,String token) throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders
                .get(url)
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(equalTo(resp)));
  }
  public String getToken(String req) throws Exception {
    String result=mockMvc
        .perform(
            MockMvcRequestBuilders
                .post("/auth/login")
                .content(req)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Success"))
        .andReturn()
        .getResponse()
        .getContentAsString();
    String token = JsonPath.parse(result).read("$.data");
    return token;
  }
  @Test
  public void postCheckUser() throws Exception {
    //normal case
    String url="/auth/check";
    String req =
        "{\n"
            + "    \"userName\": \"test22\",\n"
            + "    \"userEmail\": \"kaiwen.li18@student.xjtlu.edu.cn\"\n"
            + "}";
    String resp = "{\" status\":0,\"message\":\"Success\",\"data\":[]}".replaceAll(" ","");;
    postControllerTest(url, req, resp,"");

    //not match
    url="/auth/check";
    req =
        "{\n"
            + "    \"userName\": \"test22\",\n"
            + "    \"userEmail\": \"kaiwen.li18@student.xjtlu.edu.cnn\"\n"
            + "}";
    resp =
        "{ \"status\": 31, \"message\": \"".replaceAll(" ", "")
            + "Email not match\""
            + ", \"data\": null }".replaceAll(" ", "");
    ;
    postControllerTest(url, req, resp,"");
  }

  @Test
  public void getCheckName() throws Exception {
    //normal case
    String url="/auth/checkname/test";
    String resp =
        "{ \"status\": 321, \"message\": " .replaceAll(" ", "")
            + "\"User already exists\""
            + ", \"data\": null }"
            .replaceAll(" ", "");
    ;
    getControllerTest(url, resp,"");

    //not found
    url="/auth/checkname/tes";
    resp = "{ \"status\": 0, \"message\": \"Success\", \"data\": [] }".replaceAll(" ", "");
    getControllerTest(url, resp,"");
  }

  @Test
  public void getCheckEmail() throws Exception {
    //normal case
    String url="/auth/checkemail/kaiwen.li18@student.xjtlu.edu.cn";
    String resp =
        "{ \"status\": 321, \"message\": \"".replaceAll(" ", "")
            + "User already exists"
            + "\", \"data\": null }".replaceAll(" ", "");
    ;
    getControllerTest(url, resp,"");

    //not found
    url="/auth/checkemail/test@test.test";
    resp = "{ \"status\": 0, \"message\": \"Success\", \"data\": [] }".replaceAll(" ", "");
    getControllerTest(url, resp,"");
  }

  @Test
  public void putEditPassword() throws Exception {
    //normal case
    String url="/auth/edit/test";
    String req = "{\n" + "    \"userName\": \"test\",\n" + "    \"userPassword\": \"test\"\n" + "}";
    String resp =
        "{ \"status\": 0, \"message\": \"Success\", \"data\": { \"userName\": \"test\", \"userEmail\": \"xianghao.niu18@student.xjtlu.edu.cn\", \"userRole\": \"USER\", \"userAvatar\": \"default.jpg\" }} ".replaceAll(" ", "");

    putControllerTest(url, req, resp,"");

    //user not exists
    url="/auth/edit/tes";
    req = "{\n" + "    \"userName\": \"tes\",\n" + "    \"userPassword\": \"test\"\n" + "}";
    resp =
        "{ \"status\": 301, \"message\": " .replaceAll(" ", "")
            + "\"User not found"
            + "\", \"data\": null }".replaceAll(" ", "");
    ;
    putControllerTest(url, req, resp,"");
  }
  @Test
  public void postLogin() throws Exception {
    //normal case
    String url="/auth/login";
    String req = "{\n" + "    \"userName\": \"test\",\n" + "    \"userPassword\": \"test\"\n" + "}";
    String resp ="";
    getToken(req);

    //not match
    url="/auth/login";
    req = "{\n" + "    \"userName\": \"test\",\n" + "    \"userPassword\": \"test2\"\n" + "}";
    resp =
        "{ \"status\": 31, \"message\": \"".replaceAll(" ", "")
            + "Wrong password"
            + "\", \"data\": null }".replaceAll(" ", "");
    ;
    postControllerTest(url, req, resp,"");

    //user not exists
    url="/auth/login";
    req = "{\n" + "    \"userName\": \"tes\",\n" + "    \"userPassword\": \"test\"\n" + "}";
    resp =
        "{   \"status\": 301,\"message\": " .replaceAll(" ", "")
            + "\"User not found\","
            + "    \"data\": null}".replaceAll(" ", "");
    ;
    postControllerTest(url, req, resp,"");
  }

  @Test
  public void postRegister() throws Exception {
    //normal case
    String url="/auth/register";
    String req =
        "{\n"
            + "    \"userName\": \"test2\",\n"
            + "    \"userPassword\": \"test\",\n"
            + "    \"userEmail\": \"kaiwen.li18@student.xjtlu.edu.cn\"\n"
            + "}";
    String resp = "{ \"status\": 0, \"message\": \"Success\", \"data\": [] }".replaceAll(" ", "");
    postControllerTest(url, req, resp,"");


    //user already exists
    url="/auth/register";
    req =
        "{\n"
            + "    \"userName\": \"test22\",\n"
            + "    \"userPassword\": \"test\",\n"
            + "    \"userEmail\": \"kaiwen.li18@student.xjtlu.edu.cn\"\n"
            + "}";
    resp =
        "{ \"status\": 321, \"message\": \"" .replaceAll(" ", "")
            + "User already exists"
            + "\", \"data\": null }".replaceAll(" ", "");
    ;
    postControllerTest(url, req, resp,"");
  }
  @Test
  public void getUser() throws Exception {
//    //get token
//    String req = "{\n" + "    \"userName\": \"test\",\n" + "    \"userPassword\": \"test\"\n" + "}";
//    String token=getToken(req);

    //normal case
    String url="/user/get/test";
    String resp =
        "{ \"status\": 0, \"message\": \"Success\", \"data\": { \"userName\": \"test\", \"userEmail\": \"xianghao.niu18@student.xjtlu.edu.cn\", \"userRole\": \"USER\", \"userAvatar\": \"default.jpg\" } }"
            .replaceAll(" ", "");
    ;
    getControllerTest(url, resp,"");

    //user not exists
    url="/user/get/tes";
    resp =
        "{ \"status\": 301, \"message\": \"" .replaceAll(" ", "")
            + "User not found"
            + "\", \"data\": null }".replaceAll(" ", "");
    getControllerTest(url, resp,"");
  }

  @Test
  public void putEditUser() throws Exception {
//    //get token
//    String req = "{\n" + "    \"userName\": \"test\",\n" + "    \"userPassword\": \"test\"\n" + "}";
//    String token=getToken(req);


    //normal case
    String url="/user/edit/test";
    String req =
        "{\n"
            + "    \"userName\": \"test0\",\n"
            + "    \"userEmail\": \"kaiwen.li18@student.xjtlu.edu.cn\",\n"
            + "    \"userAvatar\": \"default.jpg\"\n"
            + "}";
    String resp =
        "{ \"status\": 0, \"message\": \"Success\", \"data\": { \"userName\": \"test0\", \"userEmail\": \"kaiwen.li18@student.xjtlu.edu.cn\", \"userRole\": \"USER\", \"userAvatar\": \"default.jpg\" } }"
            .replaceAll(" ", "");

    putControllerTest(url, req, resp,"");

    //user not exists
    url="/user/edit/tes";
    req =
        "{\n"
            + "    \"userName\": \"test0\",\n"
            + "    \"userEmail\": \"kaiwen.li18@student.xjtlu.edu.cn\",\n"
            + "    \"userAvatar\": \"default.jpg\"\n"
            + "}";
    resp =
        "{ \"status\": 301, \"message\": " .replaceAll(" ", "")
            + "\"User not found"
            + "\", \"data\": null }".replaceAll(" ", "");
    ;
    putControllerTest(url, req, resp,"");
  }
  @Test
  public void deleteUser() throws Exception {
//    //get token
//    String req = "{\n" + "    \"userName\": \"test\",\n" + "    \"userPassword\": \"test\"\n" + "}";
//    String token=getToken(req);


    //normal case
    String url="/user/delete/test22";
    String req =
        "";
    String resp =
        "{ \"status\": 0, \"message\": \"Success\", \"data\": { \"userName\": \"test22\", \"userEmail\": \"kaiwen.li18@student.xjtlu.edu.cn\", \"userRole\": \"USER\", \"userAvatar\": \"default.jpg\" } }"
            .replaceAll(" ", "");

    deleteControllerTest(url, req, resp,"");

    //user not exists
    url="/user/delete/tes";
    req =
        "";
    resp =
        "{ \"status\": 301, \"message\": " .replaceAll(" ", "")
            + "\"User not found"
            + "\", \"data\": null }".replaceAll(" ", "");
    ;
    deleteControllerTest(url, req, resp,"");
  }
}