package ke.co.nectar.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.json.JSONObject;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private Status status;
    private Map<String,Object> data;

    public ApiResponse(){}

    public ApiResponse(int code, String message, String requestId,
                       Map<String, Object> data) {
        setStatus(createStatus(code, message, requestId));
        setData(data);
    }

    public ApiResponse(int code, String message, String requestId) {
        this(code, message, requestId, null);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("Status: %s, data: %s",
                status, new JSONObject(data).toString());
    }

    private Status createStatus(int code, String message, String requestId) {
        return new Status(code, message, requestId);
    }

    static public class Status {
        private int code;
        private String message;
        private String requestId;

        Status(int code, String message, String requestId) {
            setCode(code);
            setMessage(message);
            setRequestId(requestId);
        }

        @Override
        public String toString() {
            return String.format("Code: %d, Message: %s, RequestID: %s",
                    code, message, requestId);
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }
    }
}
