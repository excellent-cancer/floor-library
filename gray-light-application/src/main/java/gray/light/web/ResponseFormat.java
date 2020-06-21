package gray.light.web;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

/**
 * 定义服务器回复格式
 *
 * @author XyParaCrim
 */
@Data
@Builder(access = AccessLevel.PACKAGE)
public class ResponseFormat {

    private final int code;

    private final Object data;

    @Builder.Default
    private final String msg = "";

}
