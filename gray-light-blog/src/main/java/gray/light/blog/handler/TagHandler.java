package gray.light.blog.handler;

import gray.light.blog.entity.Tag;
import gray.light.blog.service.TagService;
import gray.light.support.web.RequestParam;
import gray.light.support.web.RequestParamTables;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.ServerResponse;
import perishing.constraint.jdbc.Page;
import perishing.constraint.treasure.chest.collection.FinalVariables;
import reactor.core.publisher.Mono;

import java.util.List;

import static gray.light.support.web.RequestParamTables.ownerId;
import static gray.light.support.web.RequestParamTables.page;
import static gray.light.support.web.ResponseToClient.allRightFromValue;

/**
 * 处理关于tag的http请求
 *
 * @author XyParaCrim
 */
@RequiredArgsConstructor
public class TagHandler {

    public static final RequestParam<List<Tag>> TAGS_PARAM = RequestParamTables.paramTable("tags", new TagParamExtractor());

    private final TagService tagService;

    /**
     * 处理查询所属者所有用到的标签
     *
     * @param params 请求
     * @return 回复
     */
    public Mono<ServerResponse> getOwnerAllTags(FinalVariables<String> params) {
        Page page = page().get(params);
        Long ownerId = ownerId().get(params);

        return allRightFromValue(tagService.allOwnerTags(ownerId, page));
    }
}
