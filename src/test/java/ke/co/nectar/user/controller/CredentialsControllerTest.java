package ke.co.nectar.user.controller;

import ke.co.nectar.user.NectarUserServiceApplication;
import ke.co.nectar.user.annotation.NotificationProcessor;
import ke.co.nectar.user.controllers.CredentialsController;
import ke.co.nectar.user.entity.Credentials;
import ke.co.nectar.user.entity.Permissions;
import ke.co.nectar.user.entity.User;
import ke.co.nectar.user.service.credentials.CredentialsService;
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
@SpringBootTest(classes =NectarUserServiceApplication.class)
@AutoConfigureMockMvc
public class CredentialsControllerTest {

    @InjectMocks
    private CredentialsController credentialsController;

    @MockBean
    private CredentialsService credentialsService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationProcessor notificationProcessor;

    @Test
    public void contextLoads() {
        Assert.assertNotNull(credentialsController);
    }

    @Test
    public void testThatCredentialsByRefAreReturned() throws Exception {
        final long EPOCH_TIME = 1606754076302l;

        Permissions permissions = new Permissions("name", "identifier", "ref", "notes");
        permissions.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        permissions.setUpdatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        List<Permissions> permissionList = new ArrayList<>();
        permissionList.add(permissions);

        Credentials credentials = new Credentials("key", "secret", "ref", true, permissionList, Instant.now(), Instant.now());
        credentials.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        credentials.setUpdatedAt(Instant.ofEpochMilli(EPOCH_TIME));

        when(credentialsService.getCredentials(any())).thenReturn(credentials);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/credentials?ref=ref")
                .param("request_id", "requestid")
                .with(httpBasic("user_service","password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Obtained permissions','requestId':'requestid'},'data':{'credentials':{'createdAt':'2020-11-30T16:34:36.302Z','updatedAt':'2020-11-30T16:34:36.302Z','key':'key','secret':'secret','ref':'ref','activated':true,'user':null,'permissions':[{'createdAt':'2020-11-30T16:34:36.302Z','updatedAt':'2020-11-30T16:34:36.302Z','id':null,'name':'name','identifier':'identifier','ref':'ref','notes':'notes'}]}}}"));
    }

    @Test
    public void testThatCredentialsByKeyAreReturned() throws Exception {
        final long EPOCH_TIME = 1606754076302l;

        Permissions permissions = new Permissions("name", "identifier", "ref", "notes");
        permissions.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        permissions.setUpdatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        List<Permissions> permissionList = new ArrayList<>();
        permissionList.add(permissions);

        Credentials credentials = new Credentials("key", "secret", "ref", true,
                permissionList, Instant.now(), Instant.now());
        credentials.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        credentials.setUpdatedAt(Instant.ofEpochMilli(EPOCH_TIME));

        when(credentialsService.getCredentialsByKey(any())).thenReturn(credentials);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/credentials?key=key")
                .param("request_id", "requestid")
                .with(httpBasic("user_service","password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Obtained credentials by key','requestId':'requestid'},'data':{'credentials':{'createdAt':'2020-11-30T16:34:36.302Z','updatedAt':'2020-11-30T16:34:36.302Z','key':'key','secret':'secret','ref':'ref','activated':true,'user':null,'permissions':[{'createdAt':'2020-11-30T16:34:36.302Z','updatedAt':'2020-11-30T16:34:36.302Z','id':null,'name':'name','identifier':'identifier','ref':'ref','notes':'notes'}]}}}"));
    }

    @Test
    public void testThatCredentialsByUserRefAreReturned() throws Exception {
        final long EPOCH_TIME = 1606754076302l;

        Permissions permissions = new Permissions("name", "identifier", "ref", "notes");
        permissions.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        permissions.setUpdatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        List<Permissions> permissionList = new ArrayList<>();
        permissionList.add(permissions);

        User user = new User("firstName", "lastName", "test_username",
                "323232", "imageURL", "user_ref",
                "remember_token", "email@aa.com", "2121212", true);

        Credentials credentials = new Credentials("key", "secret", "ref", true, permissionList,
                                                    Instant.now(), Instant.now());
        credentials.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        credentials.setUpdatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        credentials.setUser(user);

        ArrayList<Credentials> returnedCredentials = new ArrayList<>();
        returnedCredentials.add(credentials);

        when(credentialsService.getCredentialsByUserRef(any())).thenReturn(returnedCredentials);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/credentials?user_ref=user_ref")
                .param("request_id", "requestid")
                .with(httpBasic("user_service","password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully obtained user credentials','requestId':'requestid'},'data':{'data':[{'createdAt':'2020-11-30T16:34:36.302Z','updatedAt':'2020-11-30T16:34:36.302Z','key':'key','secret':'secret','ref':'ref','activated':true,'user':{'id':null,'username':'test_username','ref':'user_ref','email':'email@aa.com','activated':true,'created_at':null,'updated_at':null,'first_name':'firstName','last_name':'lastName','phone_no':'323232','image_url':'imageURL','remember_token':'remember_token','email_verified_at':null},'permissions':[{'createdAt':'2020-11-30T16:34:36.302Z','updatedAt':'2020-11-30T16:34:36.302Z','id':null,'name':'name','identifier':'identifier','ref':'ref','notes':'notes'}]}]}}"));
    }

    @Test
    public void testThatCredentialsAreCreated() throws Throwable {
        final long EPOCH_TIME = 1606754076302l;

        Permissions permissions = new Permissions("name", "identifier", "ref", "notes");
        permissions.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        permissions.setUpdatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        List<Permissions> permissionList = new ArrayList<>();
        permissionList.add(permissions);

        User user = new User("firstName", "lastName", "test_username",
                "323232", "imageURL", "user_ref",
                "remember_token", "email@aa.com", "2121212", true);

        Credentials credentials = new Credentials("key", "secret", "ref", user,true, permissionList);
        credentials.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        credentials.setUpdatedAt(Instant.ofEpochMilli(EPOCH_TIME));

        when(notificationProcessor.process(any())).thenReturn(null);
        when(credentialsService.addCredentials(any(),anyString())).thenReturn(credentials);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/credentials")
                .param("user_ref", "user_ref")
                .param("request_id", "requestid")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1,2,3]")
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Created credentials','requestId':'requestid'},'data':{'credential':{'createdAt':'2020-11-30T16:34:36.302Z','updatedAt':'2020-11-30T16:34:36.302Z','key':'key','secret':'secret','ref':'ref','activated':true,'user':{'id':null,'username':'test_username','ref':'user_ref','email':'email@aa.com','activated':true,'created_at':null,'updated_at':null,'first_name':'firstName','last_name':'lastName','phone_no':'323232','image_url':'imageURL','remember_token':'remember_token','email_verified_at':null},'permissions':[{'createdAt':'2020-11-30T16:34:36.302Z','updatedAt':'2020-11-30T16:34:36.302Z','id':null,'name':'name','identifier':'identifier','ref':'ref','notes':'notes'}]}}}"))
                .andReturn();
    }

    @Test
    public void testThatCredentialsAreActivated() throws Throwable {
        when(notificationProcessor.process(any())).thenReturn(null);
        when(credentialsService.activateCredentials(any(), any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/credentials?ref=ref")
                .param("request_id", "requestid")
                .param("user_ref", "userref")
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully activated credentials','requestId':'requestid'}}"));
    }

    @Test
    public void testThatCredentialsAreDeactivated() throws Throwable {
        when(notificationProcessor.process(any())).thenReturn(null);
        when(credentialsService.deactivateCredentials(any(), anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/credentials?ref=ref")
                .param("request_id", "requestid")
                .param("user_ref", "userref")
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Successfully deactivated credentials','requestId':'requestid'}}"));
    }
}
