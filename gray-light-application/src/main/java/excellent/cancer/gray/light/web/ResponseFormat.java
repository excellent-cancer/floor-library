package excellent.cancer.gray.light.web;

import lombok.AccessLevel;
import lombok.Builder;

/**
 * 定义服务器回复格式
 *
 * @author XyParaCrim
 */
@Builder(access = AccessLevel.PACKAGE)
public class ResponseFormat {

    private final int code;

    private final Object data;

    @Builder.Default
    private final String msg = "";

}