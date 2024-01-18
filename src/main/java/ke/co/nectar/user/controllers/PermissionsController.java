package ke.co.nectar.user.controllers;

import ke.co.nectar.user.constant.StringConstants;
import ke.co.nectar.user.entity.Permissions;
import ke.co.nectar.user.response.ApiResponse;
import ke.co.nectar.user.service.permissions.PermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class PermissionsController {

    @Autowired
    private PermissionsService permissionsService;

    @GetMapping(value = "/permissions")
    public ApiResponse getCredentials(@RequestParam(value = "request_id") @NotNull String requestId) {
        ApiResponse apiResponse;
        try {
            List<Permissions> permissions = permissionsService.getPermissions();
            Map<String, Object> output = new LinkedHashMap<>();
            output.put("permissions", permissions);
            apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                    StringConstants.SUCCESS_MSG_PERMISSIONS,
                    requestId,
                    output);

        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.toString(),
                    requestId);
        }
        return apiResponse;
    }

}
