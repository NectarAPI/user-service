package ke.co.nectar.user.controllers;

import ke.co.nectar.user.annotation.Notify;
import ke.co.nectar.user.constant.StringConstants;
import ke.co.nectar.user.entity.Meter;
import ke.co.nectar.user.entity.Subscriber;
import ke.co.nectar.user.entity.Utility;
import ke.co.nectar.user.response.ApiResponse;
import ke.co.nectar.user.service.utility.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class UtilitiesController {

    @Autowired
    private UtilityService utilityService;

    @GetMapping(path = "/utility", params = "ref")
    public ApiResponse getUtility(@RequestParam(value = "request_id") @NotNull String requestId,
                                  @RequestParam(value = "ref") @NotNull String ref) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                Utility utility = utilityService.findByRef(ref);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("utility", utility);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                    StringConstants.SUCCESS_UTILITY_DETAILS,
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

    @GetMapping(path = "/utility/{utility_ref}/subscriber")
    public ApiResponse getSubscribersForUtility(@RequestParam(value = "request_id") @NotNull String requestId,
                                                @PathVariable(value = "utility_ref") @NotNull String utilityRef) {
        ApiResponse apiResponse;
        try {
            if (utilityRef != null && !utilityRef.isBlank()) {
                List<Subscriber> utilitySubscribers = utilityService.getSubscribers(utilityRef);
                Map<String, Object>  output = new LinkedHashMap<>();
                output.put("subscribers", utilitySubscribers);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.SUCCESS_SUBSCRIBERS_DETAILS,
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

    @GetMapping(path = "/utility/{utility_ref}/meter")
    public ApiResponse getMetersForUtility(@RequestParam(value = "request_id") @NotNull String requestId,
                                           @PathVariable(value = "utility_ref") @NotNull String utilityRef) {
        ApiResponse apiResponse;
        try {
            if (utilityRef != null && !utilityRef.isBlank()) {
                List<Meter> utilityMeters = utilityService.getMeters(utilityRef);
                Map<String, Object>  output = new LinkedHashMap<>();
                output.put("meters", utilityMeters);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.SUCCESS_METERS_DETAILS,
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

    @PostMapping("/utility")
    @Notify(category = "CREATE_UTILITY",
            description = "Created new utility [Request-ID: {requestId}]")
    public ApiResponse addUtility(@RequestParam(value = "request_id") @NotNull String requestId,
                                  @RequestParam(value = "user_ref") @NotNull String userRef,
                                  @NotNull @RequestBody Utility utility) {
        try {
            Utility addedUtility = utilityService.add(utility, userRef);
            Map<String, Object> output = new LinkedHashMap<>();
            output.put("utility", addedUtility);
            return new ApiResponse(StringConstants.SUCCESS_CODE,
                                    StringConstants.SUCCESS_UTILITY_CREATED,
                                    requestId,
                                    output);
        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
    }

    @PutMapping(path = "/utility", params = "ref")
    @Notify(category = "UPDATE_UTILITY",
            description = "Updated utility {utilityRef} [Request-ID: {requestId}]")
    public ApiResponse updateUtility(@RequestParam(value = "request_id") @NotNull String requestId,
                                     @RequestParam(value = "user_ref") @NotNull String userRef,
                                     @RequestParam(value = "ref") @NotNull String utilityRef,
                                     @Valid @RequestBody @NotNull Utility utility) {
        try {
            Utility obtainedUtility = utilityService.update(utility, utilityRef);
            Map<String, Object>  output = new LinkedHashMap<>();
            output.put("utility", obtainedUtility);
            return new ApiResponse(StringConstants.SUCCESS_CODE,
                                    StringConstants.SUCCESS_UTILITY_UPDATE,
                                    requestId,
                                    output);
        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
    }

    @PutMapping(value = "/utility/{ref}", params = "user_ref")
    @Notify(category = "ACTIVATE_UTILITY",
            description = "Activate utility {utilityRef} [Request-ID: {requestId}]")
    public ApiResponse activateUtility(@RequestParam(value = "request_id") @NotNull String requestId,
                                         @RequestParam(value = "user_ref") @NotNull String userRef,
                                         @PathVariable(value = "ref") @NotNull String utilityRef) {
        ApiResponse apiResponse;
        try {
            if (utilityRef != null && !utilityRef.isBlank()) {
                if (utilityService.activateUtility(utilityRef, userRef)) {
                    apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                            StringConstants.ACTIVATED_MSG_UTILITY_BY_REF,
                            requestId);
                } else {
                    apiResponse = new ApiResponse(StringConstants .INVALID_REQUEST,
                            StringConstants.INVALID_UTILITY_REF,
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

    @DeleteMapping(value = "/utility/{ref}", params = "user_ref")
    @Notify(category = "DEACTIVATE_UTILITY",
            description = "Deactivate utility {utilityRef} [Request-ID: {requestId}]")
    public ApiResponse deactivateUtility(@RequestParam(value = "request_id") @NotNull String requestId,
                                         @RequestParam(value = "user_ref") @NotNull String userRef,
                                         @PathVariable(value = "ref") @NotNull String utilityRef) {
        ApiResponse apiResponse;
        try {
            if (utilityRef != null && !utilityRef.isBlank()) {
                if (utilityService.deactivateUtility(utilityRef, userRef)) {
                    apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                            StringConstants.DEACTIVATED_MSG_UTILITY_BY_REF,
                            requestId);
                } else {
                    apiResponse = new ApiResponse(StringConstants .INVALID_REQUEST,
                            StringConstants.INVALID_UTILITY_REF,
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
