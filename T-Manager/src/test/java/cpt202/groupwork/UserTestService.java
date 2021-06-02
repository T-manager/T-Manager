package cpt202.groupwork;

import cpt202.groupwork.entity.User;
import cpt202.groupwork.repository.UserRepository;
import cpt202.groupwork.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @description:
 * @author: zhong
 * @create:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTestService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserServiceImpl userService;
//  @Autowired
//  private WebApplicationContext wac;
//
//  private MockMvc mockMvc;
//
//  @Before
//  public void setup(){
//    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//  }
//  @Test
//  public void test() throws Exception {
//    User user = new User();
//    user.setUserName("testuser");
//    userRepository.save(user);
//
//    //GET auth/check
//    String result=mockMvc.perform(
//        MockMvcRequestBuilders.get("/auth/check")
//            .param("username", "testuser")
//    ).andExpect(MockMvcResultMatchers.status().isOk())
//
//        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Success"))
//        .andReturn()
//        .getResponse()
//        .getContentAsString();
//    System.out.println(result);;
//  }
  @Test
  public void testService() throws Exception{
    Response response=userService.userNameExists("test");
    Assert.assertEquals("Success", response.getMessage());
    System.out.println(response.getMessage());
  }
}