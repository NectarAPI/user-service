package ke.co.nectar.user.controller;

import ke.co.nectar.user.NectarUserServiceApplication;
import ke.co.nectar.user.controllers.UsersController;
import ke.co.nectar.user.entity.User;
import ke.co.nectar.user.entity.UserActivityCategory;
import ke.co.nectar.user.entity.UserActivityLog;
import ke.co.nectar.user.service.user.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NectarUserServiceApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @InjectMocks
    private UsersController usersController;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() {
        Assert.assertNotNull(usersController);
    }

    @Test
    public void testThatCanGetUserUsingRef() throws Exception {
        final long EPOCH_TIME = 1606754076302l;

        User user = new User("firstName", "lastName", "username", "0710100100",
                "https://img.url", "ref", "remember_token", "name@email.com",
                "password", true);
        user.setId(1l);
        user.setEmailVerifiedAt(Instant.ofEpochMilli(EPOCH_TIME));
        user.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        user.setUpdatedAt(Instant.ofEpochMilli(EPOCH_TIME));

        when(userService.findByRef(any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/user?ref=ref")
                .param("request_id", "requestid")
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully obtained user details','requestId':'requestid'},'data':{'user':{'id':1,'username':'username','ref':'ref','email':'name@email.com','activated':true,'created_at':'2020-11-30T16:34:36.302Z','updated_at':'2020-11-30T16:34:36.302Z','first_name':'firstName','last_name':'lastName','phone_no':'0710100100','image_url':'https://img.url','remember_token':'remember_token','email_verified_at':'2020-11-30T16:34:36.302Z'}}}"));
    }

    @Test
    public void testThatCanGetUserUsingUsername() throws Exception {
        final long EPOCH_TIME = 1606754076302l;

        User user = new User("firstName", "lastName", "username", "0710100100",
                                "https://img.url", "ref", "remember_token", "name@email.com",
                                "password", true);
        user.setId(1l);
        user.setEmailVerifiedAt(Instant.ofEpochMilli(EPOCH_TIME));
        user.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        user.setUpdatedAt(Instant.ofEpochMilli(EPOCH_TIME));

        when(userService.find(any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/user?username=username_does_not_matter")
                .param("request_id", "requestid")
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully obtained user details','requestId':'requestid'},'data':{'user':{'id':1,'username':'username','ref':'ref','email':'name@email.com','activated':true,'created_at':'2020-11-30T16:34:36.302Z','updated_at':'2020-11-30T16:34:36.302Z','first_name':'firstName','last_name':'lastName','phone_no':'0710100100','image_url':'https://img.url','remember_token':'remember_token','email_verified_at':'2020-11-30T16:34:36.302Z'}}}"));
    }

    @Test
    public void testThatCanGetUserByRefAndRememberToken() throws Exception {
        final long EPOCH_TIME = 1606754076302l;

        User user = new User("firstName", "lastName", "username", "0710100100",
                "https://img.url", "ref", "remember_token", "name@email.com",
                "password", true);
        user.setId(1l);
        user.setEmailVerifiedAt(Instant.ofEpochMilli(EPOCH_TIME));
        user.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        user.setUpdatedAt(Instant.ofEpochMilli(EPOCH_TIME));

        when(userService.findByRefAndRememberToken(anyString(), anyString())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/user?ref=ref&remember_token=remember_token")
                .param("request_id", "requestid")
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully obtained user details','requestId':'requestid'},'data':{'user':{'id':1,'username':'username','ref':'ref','email':'name@email.com','activated':true,'created_at':'2020-11-30T16:34:36.302Z','updated_at':'2020-11-30T16:34:36.302Z','first_name':'firstName','last_name':'lastName','phone_no':'0710100100','image_url':'https://img.url','remember_token':'remember_token','email_verified_at':'2020-11-30T16:34:36.302Z'}}}"));
    }

    @Test
    public void testThatCanCreateNewUser() throws Exception {
        String userJson = "{\"first_name\": \"racheal\",\"last_name\":\"muriuki\",\"username\":\"rachael\",\"phone_no\":\"0703100100\",\"image_url\":\"https://rachael.image\",\"email\":\"rach@email.com\",\"activated\":\"true\"}";

        when(userService.add(any(), anyString())).thenReturn("ref");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/user")
                .param("user_ref", "ref")
                .param("request_id", "requestid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully created user','requestId':'requestid'},'data':{'user_ref':'ref'}}"))
                .andReturn();
    }

    @Test
    public void testThatCanUpdateUser() throws Exception {
        User user = new User("firstName", "lastName", "username", "0710100100",
                "https://img.url", "ref", "remember_token", "name@email.com",
                "password", true);

        String updateUserJson = "{\"first_name\": \"racheal\",\"last_name\":\"muriuki\",\"username\":\"rachael\",\"phone_no\":\"0703100100\",\"image_url\":\"https://rachael.image\",\"email\":\"rach@email.com\",\"activated\":\"true\"}";

        when(userService.findByRef(any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/user")
                .param("request_id", "requestid")
                .param("ref", "ref")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateUserJson)
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully updated user details','requestId':'requestid'}}"));
    }

    @Test
    public void testThatCanUpdateUserRememberToken() throws Exception {
        String userJson = "{\"first_name\": \"racheal\",\"last_name\":\"muriuki\",\"username\":\"rachael\",\"phone_no\":\"0703100100\",\"image_url\":\"https://rachael.image\",\"email\":\"rach@email.com\",\"activated\":\"true\"}";

        when(userService.updateUserRememberToken(any(), any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/user?ref=ref&remember_token=remember_token")
                .param("request_id", "requestid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully updated user remember token','requestId':'requestid'}}"));
    }

    @Test
    public void testThatCanUpdateEmailVerifiedAt() throws Exception {
        String userJson = "{\"first_name\": \"racheal\",\"last_name\":\"muriuki\",\"username\":\"rachael\",\"phone_no\":\"0703100100\",\"image_url\":\"https://rachael.image\",\"email\":\"rach@email.com\",\"activated\":\"true\"}";

        when(userService.updateEmailVerifiedAt(any(), any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/user?ref=ref&email_verified_at=1609576229")
                .param("request_id", "requestid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Updated email verified date','requestId':'requestid'}}"));
    }

    @Test
    public void testThatCanUpdateUserPassword() throws Exception {
        String userJson = "{\"first_name\": \"racheal\",\"last_name\":\"muriuki\",\"username\":\"rachael\",\"phone_no\":\"0703100100\",\"image_url\":\"https://rachael.image\",\"email\":\"rach@email.com\",\"activated\":\"true\"}";

        when(userService.updateUserPassword(any(), any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/user?ref=ref&password=new_password")
                .param("request_id", "requestid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Updated user password','requestId':'requestid'}}"));
    }

    @Test
    public void testThatCanDeleteUser() throws Exception {

        when(userService.deactivateUser(any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/user?ref=ref")
                .param("request_id", "requestid")
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully deactivated user','requestId':'requestid'}}"));

    }

    @Test
    public void testThatUserActivityLogCanBeReturned() throws Exception {
        final long EPOCH_TIME = 1606754076302l;

        User user = new User("anotherFirstName", "anotherLastName", "anotherUsername",
                "0323232", "https://anotherimage.com", "remember_token",
                "another-email@aa.com", "2121212", true);

        UserActivityCategory userActivityCategory = new UserActivityCategory("LOGIN",
                                                                            "9e0f044e-7263-47fa-8b23-a4904f050eb0",
                                                                            "User Login Activity",
                                                                            Instant.ofEpochMilli(EPOCH_TIME),
                                                                            Instant.ofEpochMilli(EPOCH_TIME));

        UserActivityLog userActivityLog = new UserActivityLog(user, "2c4eb746-675a-45a5-bd50-6dc224ecc27d",
                                                        userActivityCategory, "Added new configuration",
                                                        Instant.ofEpochMilli(EPOCH_TIME), Instant.ofEpochMilli(EPOCH_TIME));

        List<UserActivityLog> logs = new ArrayList<>();
        logs.add(userActivityLog);

        when(userService.getUserActivityLogs(anyString())).thenReturn(logs);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/user/ref/activity")
                .param("request_id", "requestid")
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully obtained user details','requestId':'requestid'},'data':{'user_activity_logs':[{'ref':'2c4eb746-675a-45a5-bd50-6dc224ecc27d','created_at':'2020-11-30T16:34:36.302Z','updated_at':'2020-11-30T16:34:36.302Z','category': {'created_at':'2020-11-30T16:34:36.302Z','updated_at':'2020-11-30T16:34:36.302Z','name':'LOGIN','ref':'9e0f044e-7263-47fa-8b23-a4904f050eb0','notes':'User Login Activity'}}]}}"));
    }

    @Test
    public void testThatUserActivityLogIsAdded() throws Exception {

        final String USER_ACTIVITY_LOG = "{\"category\":\"Configuration\",\"description\":\"Added Configuration\"}";

        when(userService.setUserActivityLog(any(), anyString())).thenReturn("ref");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/user/ref/activity")
                .param("ref", "ref")
                .param("request_id", "requestid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_ACTIVITY_LOG)
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully created user activity log','requestId':'requestid'},'data':{'user_activity_log_ref':'ref'}}"))
                .andReturn();
    }
}
