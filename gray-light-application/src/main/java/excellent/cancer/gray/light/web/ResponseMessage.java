package excellent.cancer.gray.light.web;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ResponseMessage {

    private final int code;

    @Builder.Default
    private final String msg = "";

    private final Object data;

    public enum ResponseCode {
        SUCCESS(0),
        ERROR(-1);

        private final int codeValue;

        ResponseCode(int codeValue) {
            this.codeValue = codeValue;
        }
    }

    public static ResponseMessage success() {
        return prepare(ResponseCode.SUCCESS).
                build();
    }

    public static ResponseMessage success(Object data) {
        return prepare(ResponseCode.SUCCESS).
                data(data).
                build();
    }

    public static ResponseMessage success(String message) {
        return prepare(ResponseCode.SUCCESS).
                msg(message).
                build();
    }

    public static ResponseMessage success(Object data, String message) {
        return prepare(ResponseCode.SUCCESS).
                msg(message).
                data(data).
                build();
    }

    public static ResponseMessageBuilder error() {
        return prepare(ResponseCode.ERROR);
    }

    public static ResponseMessageBuilder error(String message) {
        return prepare(ResponseCode.ERROR).msg(message);
    }

    private static ResponseMessageBuilder prepare(@NonNull ResponseCode code) {
        return builder().code(code.codeValue);
    }
}
