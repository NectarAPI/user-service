package ke.co.nectar.user.controller;

import ke.co.nectar.user.NectarUserServiceApplication;
import ke.co.nectar.user.controllers.PermissionsController;
import ke.co.nectar.user.entity.Permissions;
import ke.co.nectar.user.service.permissions.PermissionsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NectarUserServiceApplication.class)
@AutoConfigureMockMvc
public class PermissionsControllerTest {

    @InjectMocks
    PermissionsController permissionsController;

    @MockBean
    PermissionsService permissionsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() {
        Assert.assertNotNull(permissionsController);
    }

    @Test
    public void testThatCanGetPermissions() throws Exception {
        Permissions permissions = new Permissions("permission_name", "permissions_identifier",
                                                "permissions_ref", "permissions_notes");

        ArrayList<Permissions> obtainedPermissions = new ArrayList<>();
        obtainedPermissions.add(permissions);

        when(permissionsService.getPermissions()).thenReturn(obtainedPermissions);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/permissions")
                .param("request_id", "requestid")
                .with(httpBasic("user_service", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{'status':{'code':200,'message':'Obtained permissions','requestId':'requestid'},'data':{'permissions':[{'createdAt':null,'updatedAt':null,'id':null,'name':'permission_name','identifier':'permissions_identifier','ref':'permissions_ref','notes':'permissions_notes'}]}}"));

    }
}
