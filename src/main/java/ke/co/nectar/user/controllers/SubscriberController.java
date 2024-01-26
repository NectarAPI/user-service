package ke.co.nectar.user.controllers;

import ke.co.nectar.user.annotation.Notify;
import ke.co.nectar.user.constant.StringConstants;
import ke.co.nectar.user.entity.Subscriber;
import ke.co.nectar.user.response.ApiResponse;
import ke.co.nectar.user.service.subscriber.SubscriberService;
import ke.co.nectar.user.service.subscriber.impl.SubscriberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @GetMapping(path = "/subscriber", params = "ref")
    public ApiResponse findSubscriberByRef(@RequestParam(value = "request_id") @NotNull String requestId,
                                           @RequestParam(value = "ref") @NotNull String ref) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                Subscriber subscriber = subscriberService.findByRef(ref);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("subscriber", subscriber);
                apiResponse =  new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.SUCCESS_SUBSCRIBER_DETAILS,
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

    @PostMapping(path = "/subscriber")
    @Notify(category = "ADD_SUBSCRIBER",
            description = "Added new subscriber [Request-ID: {requestId}]")
    public ApiResponse addSubscriber(@RequestParam(value = "request_id") @NotNull String requestId,
                                     @RequestParam(value = "user_ref") @NotNull String userRef,
                                     @NotNull @RequestBody SubscriberServiceImpl.SubscriberBundle subscriberBundle) {
        try {
            Subscriber addSubscriberRef = subscriberService.add(subscriberBundle, userRef);
            Map<String, Object> output = new LinkedHashMap<>();
            output.put("subscriber", addSubscriberRef);
            return new ApiResponse(StringConstants.SUCCESS_CODE,
                                    StringConstants.SUCCESS_SUBSCRIBER_ADDED,
                                    requestId,
                                    output);
        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
    }

    @PutMapping(path = "/subscriber", consumes = "application/json")
    @Notify(category = "UPDATE_SUBSCRIBER",
            description = "Updated subscriber {subscriberRef} [Request-ID: {requestId}]")
    public ApiResponse updateSubscriber(@RequestParam(value = "request_id") @NotNull String requestId,
                                        @RequestParam(value = "subscriber_ref") @NotNull String subscriberRef,
                                        @RequestParam(value = "user_ref") @NotNull String userRef,
                                        @Valid @RequestBody @NotNull SubscriberServiceImpl.SubscriberBundle subscriberBundle) {
        try {
            Subscriber subscriber = subscriberService.update(subscriberBundle, subscriberRef, userRef);
            Map<String, Object> output = new LinkedHashMap<>();
            output.put("subscriber", subscriber);
            return new ApiResponse(StringConstants.SUCCESS_CODE,
                                    StringConstants.SUCCESS_SUBSCRIBER_UPDATED,
                                    requestId,
                                    output);
        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
    }

    @PutMapping(value = "/subscriber/{subscriber_ref}", params = "user_ref")
    @Notify(category = "ACTIVATE_SUBSCRIBER",
            description = "Activate subscriber {subscriberRef} [Request-ID: {requestId}]")
    public ApiResponse activateSubscriber(@RequestParam(value = "request_id") @NotNull String requestId,
                                       @RequestParam(value = "user_ref") @NotNull String userRef,
                                       @PathVariable(value = "subscriber_ref") @NotNull String subscriberRef) {
        ApiResponse apiResponse;
        try {
            if (subscriberRef != null && !subscriberRef.isBlank()) {
                if (subscriberService.activateSubscriber(subscriberRef)) {
                    apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                    StringConstants.ACTIVATED_MSG_SUBSCRIBER_BY_REF,
                                                    requestId);
                } else {
                    apiResponse = new ApiResponse(StringConstants .INVALID_REQUEST,
                                                    StringConstants.INVALID_SUBSCRIBER_REF,
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

    @DeleteMapping(value = "/subscriber/{subscriber_ref}", params = "user_ref")
    @Notify(category = "DEACTIVATE_SUBSCRIBER",
            description = "Deactivate subscriber {subscriberRef} [Request-ID: {requestId}]")
    public ApiResponse deactivateSubscriber(@RequestParam(value = "request_id") @NotNull String requestId,
                                         @RequestParam(value = "user_ref") @NotNull String userRef,
                                         @PathVariable(value = "subscriber_ref") @NotNull String subscriberRef) {
        ApiResponse apiResponse;
        try {
            if (subscriberRef != null && !subscriberRef.isBlank()) {
                if (subscriberService.deactivateSubscriber(subscriberRef)) {
                    apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                    StringConstants.DEACTIVATED_MSG_SUBSCRIBER_BY_REF,
                                                    requestId);
                } else {
                    apiResponse = new ApiResponse(StringConstants .INVALID_REQUEST,
                                                    StringConstants.INVALID_SUBSCRIBER_REF,
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
