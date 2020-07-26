package gray.light.blog.handler;

import gray.light.blog.business.BlogBo;
import gray.light.blog.entity.Tag;
import gray.light.blog.service.ReadableBlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerResponse;
import perishing.constraint.jdbc.Page;
import perishing.constraint.treasure.chest.collection.FinalVariables;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static gray.light.support.web.RequestParamTables.*;
import static gray.light.support.web.ResponseToClient.allRightFromValue;
import static gray.light.support.web.ResponseToClient.failWithMessage;

/**
 * @author XyParaCrim
 */
@Slf4j
@RequiredArgsConstructor
public class BlogQueryHandler {

    private final ReadableBlogService readableBlogService;

    public Mono<ServerResponse> queryBlogs(FinalVariables<String> params) {
        Page page = page().get(params);
        Long ownerId = ownerId().get(params);


        if (TagHandler.TAGS_PARAM.contains(params)) {
            List<Tag> tags = TagHandler.TAGS_PARAM.get(params);

            return allRightFromValue(readableBlogService.findBlogsByTags(ownerId, tags, page));
        } else {
            return allRightFromValue(readableBlogService.findBlogs(ownerId, page));
        }
    }

    public Mono<ServerResponse> blogDetails(FinalVariables<String> params) {
        Long id = id().get(params);

        Optional<BlogBo> details = readableBlogService.findBlogDetails(id);
        if (details.isEmpty()) {
            return failWithMessage("The blog does not exist: " + id);
        }

        return allRightFromValue(details.get());
    }

}
