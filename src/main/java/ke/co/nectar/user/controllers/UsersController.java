package ke.co.nectar.user.controllers;

import ke.co.nectar.user.annotation.Notify;
import ke.co.nectar.user.constant.StringConstants;
import ke.co.nectar.user.controllers.utils.ActivityLog;
import ke.co.nectar.user.entity.User;
import ke.co.nectar.user.entity.UserActivityLog;
import ke.co.nectar.user.entity.Utility;
import ke.co.nectar.user.response.ApiResponse;
import ke.co.nectar.user.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/user", params = "ref")
    public ApiResponse findUserByRef(@RequestParam(value = "request_id") @NotNull String requestId,
                                     @RequestParam(value = "ref") @NotNull String ref) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                User user = userService.findByRef(ref);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("user", user);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                StringConstants.SUCCESS_USER_DETAILS,
                                requestId,
                                output);
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

    @GetMapping(path = "/user", params = "email")
    public ApiResponse findUserByEmail(@RequestParam(value = "request_id") @NotNull String requestId,
                                        @RequestParam(value = "email") @NotNull String email) {
        ApiResponse apiResponse;
        try {
            if (email != null && !email.isBlank()) {
                User user = userService.findByEmail(email);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("user", user);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.SUCCESS_USER_DETAILS,
                        requestId,
                        output);
            } else {
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.EMPTY_USER_NAME_VALUE,
                        requestId);
            }

        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
        return apiResponse;
    }

    @GetMapping(value = "/user", params = "username")
    public ApiResponse findUser(@RequestParam(value = "request_id") @NotNull String requestId,
                                @RequestParam(value = "username") String username) {
        ApiResponse apiResponse;
        try {
            if (username != null && !username.isBlank()) {
                User user = userService.find(username);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("user", user);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                    StringConstants.SUCCESS_USER_DETAILS,
                                    requestId,
                                    output);
            } else {
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.EMPTY_USER_NAME_VALUE,
                        requestId);
            }

        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
        return apiResponse;
    }

    @GetMapping(value = "/user", params = { "ref", "remember_token" })
    public ApiResponse findUser(@RequestParam(value = "request_id") @NotNull String requestId,
                                @RequestParam(value = "ref") @NotNull String ref,
                                @RequestParam(value = "remember_token") @NotNull String rememberToken) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank() &&
                    rememberToken != null && !rememberToken.isBlank()) {
                User user = userService.findByRefAndRememberToken(ref, rememberToken);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("user", user);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.SUCCESS_USER_DETAILS,
                        requestId,
                        output);
            } else {
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.EMPTY_USER_NAME_VALUE,
                        requestId);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
        return apiResponse;
    }

    @GetMapping(path = "/user/{ref}/activity")
    public ApiResponse getUserActivityLogs(@RequestParam(value = "request_id") @NotNull String requestId,
                                           @PathVariable(value = "ref") @NotNull String ref) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                List<UserActivityLog> userActivityLogs = userService.getUserActivityLogs(ref);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("user_activity_logs", userActivityLogs);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.SUCCESS_USER_DETAILS,
                                                requestId,
                                                output);
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

    @GetMapping(path = "/user/{user_ref}/utility")
    public ApiResponse getUtilitiesForUser(@RequestParam(value = "request_id") @NotNull String requestId,
                                           @PathVariable(value = "user_ref") @NotNull String userRef) {

        ApiResponse apiResponse;
        try {
            if (userRef != null && !userRef.isBlank()) {
                List<Utility> userUtilities = userService.getUserUtilities(userRef);
                Map<String, Object>  output = new LinkedHashMap<>();
                output.put("utilities", userUtilities);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.SUCCESS_UTILITIES_DETAILS,
                        requestId,
                        output);
            } else {
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.INVALID_USER_REF,
                                                requestId);
            }
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
        return apiResponse;
    }

    @PostMapping("/user")
    @Notify(category = "ADD_USER",
            description = "Added new user [Request-ID: {requestId}]")
    public ApiResponse addUser(@RequestParam(value = "request_id") @NotNull String requestId,
                               @RequestParam(value = "user_ref") @NotNull String userRef,
                               @NotNull @RequestBody User user) {
        try {
            String addedUserRef = userService.add(user, userRef);
            Map<String, Object> output = new LinkedHashMap<>();
            output.put("user_ref", addedUserRef);
            return new ApiResponse(StringConstants.SUCCESS_CODE,
                    StringConstants.SUCCESS_USER_SAVED,
                    requestId,
                    output);
        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
    }

    @PostMapping(value = "/user/{ref}/activity", consumes = "application/json")
    public ApiResponse addUserActivityLog(@RequestParam(value = "request_id") @NotNull String requestId,
                                          @PathVariable(value = "ref") @NotNull String userRef,
                                          @NotNull @Valid @RequestBody ActivityLog activityLog) {
        try {
            if (userRef != null && !userRef.isBlank()) {
                String userActivityLogRef = userService.setUserActivityLog(activityLog, userRef);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("user_activity_log_ref", userActivityLogRef);
                return new ApiResponse(StringConstants.SUCCESS_CODE,
                                        StringConstants.SUCCESS_USER_ACTIVITY_LOG_SAVED,
                                        requestId,
                                        output);
            } else {
                return new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.EMPTY_REF_VALUE,
                        requestId);
            }
        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                                    e.getMessage(),
                                    requestId);
        }
    }

    @PutMapping(value = "/user", params = "ref")
    @Notify(category = "UPDATE_USER",
            description = "Updated user {userRef} [Request-ID: {requestId}]")
    public ApiResponse updateUser(@RequestParam(value = "request_id") @NotNull String requestId,
                                  @RequestParam(value = "ref") @NotNull String userRef,
                                  @Valid @RequestBody @NotNull User user) {
        try {
            User obtainedUser = userService.findByRef(userRef);
            if (obtainedUser != null) {
                obtainedUser.setFirstName(user.getFirstName() != null ? user.getFirstName() : obtainedUser.getFirstName());
                obtainedUser.setLastName(user.getLastName() != null ? user.getLastName() : obtainedUser.getLastName());
                obtainedUser.setUsername(user.getUsername() != null ? user.getUsername() : obtainedUser.getUsername());
                obtainedUser.setPhoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber() : obtainedUser.getPhoneNumber());
                obtainedUser.setImageURL(user.getImageURL() != null ? user.getImageURL() : obtainedUser.getImageURL());
                obtainedUser.setRef(user.getRef() != null ? user.getRef() : obtainedUser.getRef());
                obtainedUser.setRememberToken(user.getRememberToken() != null ? user.getRememberToken() : obtainedUser.getRememberToken());
                obtainedUser.setEmail(user.getEmail() != null ? user.getEmail() : obtainedUser.getEmail());
                obtainedUser.password = (user.getPassword() != null ? user.getPassword() : obtainedUser.getPassword());
                obtainedUser.setEmailVerifiedAt(user.getEmailVerifiedAt() != null ? user.getEmailVerifiedAt() : obtainedUser.getEmailVerifiedAt());
                obtainedUser.setActivated(user.getActivated() != null ? user.getActivated() : obtainedUser.getActivated());
                obtainedUser.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt() : obtainedUser.getCreatedAt());
                obtainedUser.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt() : obtainedUser.getUpdatedAt());

                userService.update(obtainedUser, userRef);

                return new ApiResponse(StringConstants.SUCCESS_CODE,
                                        StringConstants.SUCCESS_USER_UPDATE,
                                        requestId);
            }

            return new ApiResponse(StringConstants.INVALID_REQUEST,
                                    StringConstants.INVALID_USER_REF,
                                    requestId);

        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
    }

    @PutMapping(value = "/user", params = { "ref", "password" })
    @Notify(category = "UPDATE_USER_PASSWORD",
            description = "Updated password for user {userRef} [Request-ID: {requestId}]")
    public ApiResponse updateUserPassword(@RequestParam(value = "ref") @NotNull String userRef,
                                          @RequestParam(value = "password") @NotNull String password,
                                          @RequestParam(value = "request_id") @NotNull String requestId) {
        try {
            if (userService.updateUserPassword(userRef, password)) {
                return new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.SUCCESS_USER_PASSWORD_UPDATE,
                        requestId);
            } else {
                return new ApiResponse(StringConstants.INVALID_REQUEST,
                        StringConstants.INVALID_USER_REF,
                        requestId);
            }
        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
    }

    @PutMapping(value = "/user", params = { "ref", "remember_token" })
    @Notify(category = "UPDATE_USER_REMEMBER_TOKEN",
            description = "Updated remember token for user {userRef} [Request-ID: {requestId}]")
    public ApiResponse updateUserRememberToken(@RequestParam(value = "request_id") @NotNull String requestId,
                                               @RequestParam(value = "ref") @NotNull String userRef,
                                               @RequestParam(value = "remember_token") @NotNull String rememberToken) {
        try {
            if (userService.updateUserRememberToken(rememberToken, userRef)) {
                return new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.SUCCESS_USER_REMEMBER_TOKEN_UPDATE,
                        requestId);
            } else {
                return new ApiResponse(StringConstants.INVALID_REQUEST,
                        StringConstants.INVALID_USER_REF,
                        requestId);
            }
        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
    }

    @PutMapping(value = "/user", params = { "ref", "email_verified_at" })
    @Notify(category = "UPDATE_EMAIL_VERIFIED_AT",
            description = "Update email verified at [Request-ID: {requestId}]")
    public ApiResponse updateEmailVerifiedAt(@RequestParam(value = "request_id") @NotNull String requestId,
                                               @RequestParam(value = "ref") @NotNull String userRef,
                                               @RequestParam(value = "email_verified_at") @NotNull long emailVerifiedAt) {
        try {
            Instant emailVerifiedAtTime = Instant.ofEpochMilli(emailVerifiedAt);
            if (userService.updateEmailVerifiedAt(emailVerifiedAtTime, userRef)) {
                return new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.SUCCESS_EMAIL_VERIFIED_AT_UPDATE,
                        requestId);
            } else {
                return new ApiResponse(StringConstants.INVALID_REQUEST,
                        StringConstants.INVALID_USER_REF,
                        requestId);
            }
        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
    }

    @DeleteMapping(value = "/user", params = "ref")
    @Notify(category = "DEACTIVATE_USER",
            description = "Deactivate user {userRef} [Request-ID: {requestId}]")
    public ApiResponse deactivateUser(HttpServletRequest request,
                                  @RequestParam(value = "request_id") @NotNull String requestId,
                                  @RequestParam(value = "ref") @NotNull String userRef) {
        ApiResponse apiResponse;
        try {
            if (userRef != null && !userRef.isBlank()) {
                if (userService.deactivateUser(userRef)) {
                    apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                            StringConstants.DEACTIVATED_MSG_USER_BY_REF,
                            requestId);
                } else {
                    apiResponse = new ApiResponse(StringConstants.INVALID_REQUEST,
                            StringConstants.ERROR_DEACTIVATING_USER,
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
