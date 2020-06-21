package gray.light.web;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import perishing.constraint.web.ResponseCode;
import reactor.core.publisher.Mono;

/**
 * 封装回复客户端常用和需要的功能
 *
 * @author XyParaCrim
 */
public final class ResponseToClient {

    // 快捷方式获取handle需要的ServerResponse

    /**
     * 将指定对象实例以json形式写入到body中
     *
     * @param value 返回对象实例
     * @return webflux的ServerResponse
     */
    public static Mono<ServerResponse> allRightFromValue(Object value) {
        return assemblyResponse(ServerResponse.ok(), success(value));
    }

    public static Mono<ServerResponse> failWithMessage(String message) {
        return assemblyResponse(ServerResponse.ok(), error(message));
    }

    private static Mono<ServerResponse> assemblyResponse(ServerResponse.BodyBuilder body, ResponseFormat response) {
        return body.body(BodyInserters.fromValue(response));
    }

    // 快捷方式获取回复对象

    /**
     * 返回成功回复：返回码
     *
     * @return 回复对象
     */
    public static ResponseFormat success() {
        return prepare(ResponseCode.CommonResponseCode.SUCCESS)
                .build();
    }

    /**
     * 返回成功回复：返回码、数据对象
     *
     * @param data 数据对象
     * @return 回复对象
     */
    public static ResponseFormat success(Object data) {
        return prepare(ResponseCode.CommonResponseCode.SUCCESS)
                .data(data)
                .build();
    }

    /**
     * 返回成功回复：返回码、数据对象、回复消息
     *
     * @param data 数据对象
     * @param msg  回复消息
     * @return 回复对象
     */
    public static ResponseFormat success(Object data, String msg) {
        return prepare(ResponseCode.CommonResponseCode.SUCCESS)
                .data(data)
                .msg(msg)
                .build();
    }

    /**
     * 返回错误回复：返回码
     *
     * @return 回复对象
     */
    public static ResponseFormat error() {
        return prepare(ResponseCode.CommonResponseCode.ERROR)
                .build();
    }

    /**
     * 返回错误回复：返回码、回复消息
     *
     * @param msg 回复消息
     * @return 回复对象
     */
    public static ResponseFormat error(String msg) {
        return prepare(ResponseCode.CommonResponseCode.ERROR).
                msg(msg).
                build();
    }

    private static ResponseFormat.ResponseFormatBuilder prepare(ResponseCode responseCode) {
        return ResponseFormat.
                builder().
                code(responseCode.value());
    }

    // 参数检查

    public static UnsatisfiedBodyExtractorChain unsatisfiedChain() {
        return new UnsatisfiedBodyExtractorChain();
    }

}
