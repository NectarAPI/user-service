package ke.co.nectar.user.controllers;

import ke.co.nectar.user.annotation.Notify;
import ke.co.nectar.user.constant.StringConstants;
import ke.co.nectar.user.entity.Credentials;
import ke.co.nectar.user.response.ApiResponse;
import ke.co.nectar.user.service.credentials.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class CredentialsController {

    @Autowired
    private CredentialsService credentialsService;

    @GetMapping(value = "/credentials", params = "key")
    public ApiResponse getCredentialsByKey(@RequestParam(value = "request_id") @NotNull String requestId,
                                            @RequestParam(value = "key") @NotNull String key) {
        ApiResponse apiResponse;
        try {
            if (key != null && !key.isBlank()) {
                Credentials credentials = credentialsService.getCredentialsByKey(key);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("credentials", credentials);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.SUCCESS_MSG_CREDENTIAL_BY_KEY,
                                                requestId,
                                                output);
            } else {
                apiResponse = new ApiResponse(StringConstants.BAD_REQUEST,
                                                StringConstants.EMPTY_REF_VALUE,
                                                requestId);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                                            e.toString(),
                                            requestId);
        }
        return apiResponse;
    }

    @GetMapping(value = "/credentials", params = "ref")
    public ApiResponse getCredentials(@RequestParam(value = "request_id") @NotNull String requestId,
                                      @RequestParam(value = "ref") @NotNull String ref) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                Credentials credentials = credentialsService.getCredentials(ref);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("credentials", credentials);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.SUCCESS_MSG_CREDENTIAL_BY_REF,
                        requestId,
                        output);
            } else {
                apiResponse = new ApiResponse(StringConstants.BAD_REQUEST,
                        StringConstants.EMPTY_REF_VALUE,
                        requestId);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.toString(),
                    requestId);
        }
        return apiResponse;
    }

    @GetMapping(value = "/credentials", params = "user_ref")
    public ApiResponse getCredentialsByUserRef(@RequestParam(value = "request_id") @NotNull String requestId,
                                                @RequestParam(value = "user_ref") String userRef) {

        ApiResponse apiResponse;
        try {
            if (userRef != null && !userRef.isBlank()) {
                List<Credentials> config = credentialsService.getCredentialsByUserRef(userRef);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("data", config);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.SUCCESS_MSG_CREDENTIALS_BY_USER_REF,
                        requestId,
                        output);
            } else {
                apiResponse = new ApiResponse(StringConstants.BAD_REQUEST,
                        StringConstants.EMPTY_REF_VALUE,
                        requestId);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.toString(),
                    requestId);
        }
        return apiResponse;
    }

    @PostMapping("/credentials")
    @Notify(category = "ADD_CREDENTIAL",
            description = "Added credentials [Request-ID: {requestId}]")
    public ApiResponse addCredential(@RequestParam(value = "request_id") @NotNull String requestId,
                                       @RequestParam(value = "user_ref") @NotNull String userRef,
                                       @NotNull @RequestBody List<Long> permissionsIds) {
        ApiResponse apiResponse;
        try {
            if (userRef != null && !userRef.isBlank()) {
                Credentials credentials = credentialsService.addCredentials(permissionsIds, userRef);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("credential", credentials);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.CREATED_CREDENTIAL,
                                                requestId,
                                                output);

            } else {
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                            StringConstants.EMPTY_USER_REF_VALUE,
                                            requestId);
            }

        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
        return apiResponse;
    }

    @PutMapping(value = "/credentials", params = "ref")
    @Notify(category = "ACTIVATE_CREDENTIAL",
            description = "Activate credential {ref} [Request-ID: {requestId}]")
    public ApiResponse activateCredentials(@RequestParam(value = "request_id") @NotNull String requestId,
                                           @RequestParam(value = "user_ref") @NotNull String userRef,
                                           @RequestParam(value = "ref") String ref) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                boolean activated = credentialsService.activateCredentials(ref, userRef);

                if (activated) {
                    apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                            StringConstants.ACTIVATED_MSG_CREDENTIAL_BY_REF,
                            requestId);
                } else {
                    apiResponse = new ApiResponse(StringConstants.INVALID_REQUEST,
                            StringConstants.INVALID_MSG_CREDENTIAL_BY_REF,
                            requestId);
                }

            } else {
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.EMPTY_REF_VALUE,
                        requestId);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
        return apiResponse;
    }

    @DeleteMapping(value = "/credentials", params = "ref")
    @Notify(category = "DEACTIVATE_CREDENTIAL",
            description = "Deactivate credential {ref} [Request-ID: {requestId}]")
    public ApiResponse deactivateCredentials(@RequestParam(value = "request_id") @NotNull String requestId,
                                             @RequestParam(value = "user_ref") @NotNull String userRef,
                                             @RequestParam(value = "ref") @NotNull String ref) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                boolean deactivated = credentialsService.deactivateCredentials(ref, userRef);

                if (deactivated) {
                    apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                            StringConstants.DELETED_MSG_CREDENTIAL_BY_REF,
                            requestId);
                } else {
                    apiResponse = new ApiResponse(StringConstants.INVALID_REQUEST,
                            StringConstants.INVALID_MSG_CREDENTIAL_BY_REF,
                            requestId);
                }

            } else {
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.EMPTY_REF_VALUE,
                        requestId);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
        return apiResponse;
    }
}
