package ke.co.nectar.user.controllers;

import ke.co.nectar.user.annotation.Notify;
import ke.co.nectar.user.constant.StringConstants;
import ke.co.nectar.user.entity.Meter;
import ke.co.nectar.user.entity.MeterType;
import ke.co.nectar.user.response.ApiResponse;
import ke.co.nectar.user.service.meters.MetersService;
import ke.co.nectar.user.service.meters.impl.MetersServiceImpl.CompositeMeterBundle;
import ke.co.nectar.user.service.meters.impl.MetersServiceImpl.MeterBundle;
import ke.co.nectar.user.service.subscriber.SubscriberService;
import ke.co.nectar.user.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class MetersController {

    @Autowired
    private MetersService metersService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriberService subscriberService;

    @GetMapping(path = "/meters", params = "ref")
    public ApiResponse getMeter(@RequestParam(value = "request_id") @NotNull String requestId,
                                @RequestParam(value = "ref") @NotNull String ref) {
        ApiResponse apiResponse;
        try {
            if (ref != null && !ref.isBlank()) {
                Meter meter = metersService.findByRef(ref);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("meter", meter);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.SUCCESS_METER_DETAILS,
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

    @GetMapping(path = "/meters/types")
    public ApiResponse getMeterTypes(@RequestParam(value = "request_id") @NotNull String requestId) {
        ApiResponse apiResponse;
        try {
            List<MeterType> meterTypes = metersService.getMeterTypes();
            Map<String, Object> output = new LinkedHashMap<>();
            output.put("meter_types", meterTypes);
            apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                        StringConstants.SUCCESS_METER_TYPE_DETAILS,
                                        requestId,
                                        output);
        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
        return apiResponse;
    }

    @PostMapping("/meters")
    @Notify(category = "ADD_METER",
            description = "Added new meter [Request-ID: {requestId}]")
    public ApiResponse addMeter(@RequestParam(value = "request_id") @NotNull String requestId,
                                @RequestParam(value = "user_ref") @NotNull String userRef,
                                @NotNull @RequestBody MeterBundle meter) {
        try {
            CompositeMeterBundle addedMeter = metersService.add(meter, userRef);
            Map<String, Object> output = new LinkedHashMap<>();
            output.put("meter", addedMeter);
            return new ApiResponse(StringConstants.SUCCESS_CODE,
                                    StringConstants.SUCCESS_METER_ADDED,
                                    requestId,
                                    output);
        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
    }

    @PutMapping(value = "/meters", params = "meter_ref")
    @Notify(category = "UPDATE_METER",
            description = "Updated meter {meterRef} [Request-ID: {requestId}]")
    public ApiResponse updateMeter(@RequestParam(value = "request_id") @NotNull String requestId,
                                   @RequestParam(value = "meter_ref") @NotNull String meterRef,
                                   @RequestParam(value = "user_ref") @NotNull String userRef,
                                   @Valid @RequestBody @NotNull MeterBundle meterBundle) {
        try {
            CompositeMeterBundle addedMeter = metersService.update(meterBundle, meterRef, userRef);
            Map<String, Object> output = new LinkedHashMap<>();
            output.put("meter", addedMeter);
            return new ApiResponse(StringConstants.SUCCESS_CODE,
                                    StringConstants.SUCCESS_METER_UPDATED,
                                    requestId,
                                    output);

        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
    }

    @PutMapping(value = "/meters/{ref}", params = "user_ref")
    @Notify(category = "ACTIVATE_METER",
            description = "Activate meter {meterRef} [Request-ID: {requestId}]")
    public ApiResponse activateMeter(@RequestParam(value = "request_id") @NotNull String requestId,
                                       @RequestParam(value = "user_ref") @NotNull String userRef,
                                       @PathVariable(value = "ref") @NotNull String meterRef) {
        ApiResponse apiResponse;
        try {
            if (meterRef != null && !meterRef.isBlank()) {
                if (metersService.activateMeter(meterRef, userRef)) {
                    apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                    StringConstants.ACTIVATED_MSG_METER_BY_REF,
                                                    requestId);
                } else {
                    apiResponse = new ApiResponse(StringConstants .INVALID_REQUEST,
                                                    StringConstants.INVALID_METER_REF,
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

    @DeleteMapping(value = "/meters/{ref}", params = "user_ref")
    @Notify(category = "DEACTIVATE_METER",
            description = "Deactivate meter {meterRef} [Request-ID: {requestId}]")
    public ApiResponse deactivateMeter(@RequestParam(value = "request_id") @NotNull String requestId,
                                         @RequestParam(value = "user_ref") @NotNull String userRef,
                                         @PathVariable(value = "ref") @NotNull String meterRef) {
        ApiResponse apiResponse;
        try {
            if (meterRef != null && !meterRef.isBlank()) {
                if (metersService.deactivateMeter(meterRef, userRef)) {
                    apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                    StringConstants.DEACTIVATED_MSG_METER_BY_REF,
                                                    requestId);
                } else {
                    apiResponse = new ApiResponse(StringConstants .INVALID_REQUEST,
                                                    StringConstants.INVALID_METER_REF,
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
