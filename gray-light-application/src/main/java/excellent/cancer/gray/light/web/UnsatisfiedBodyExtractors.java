package excellent.cancer.gray.light.web;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Optional;

/**
 * 定义常用的提取器
 *
 * @author XyParaCrim
 */
public final class UnsatisfiedBodyExtractors {

    public static final UnsatisfiedBodyExtractor EXTRACT_NAME = ((key, properties) -> {
        if (StringUtils.isEmpty(properties.get(key))) {
            return Optional.of(ServerResponse.status(HttpStatus.BAD_REQUEST).body(BodyInserters.fromValue("项目名不能为空")));
        }

        return Optional.empty();
    });


}
