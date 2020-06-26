package gray.light.book.handler;

import gray.light.book.business.CatalogsTreeWalker;
import gray.light.book.business.ContainsCatalogCatalogBo;
import gray.light.book.entity.BookCatalog;
import gray.light.book.entity.BookChapter;
import gray.light.book.service.BookService;
import gray.light.owner.handler.OwnerHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

import static gray.light.support.web.ResponseToClient.allRightFromValue;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BookHandler {

    private final OwnerHandler OwnerHandler;

    private final BookService bookService;

    /**
     * 查询文档仓库的结构树
     *
     * @param request 服务请求
     * @return Response of Publisher
     */
    public Mono<ServerResponse> queryBookTree(ServerRequest request) {
        return OwnerHandler.extractId(request, projectId -> {
            Tuple2<List<BookCatalog>, List<BookChapter>> queryResult = bookService.catalogAndChapter(projectId);
            ContainsCatalogCatalogBo rootBo = CatalogsTreeWalker.walk(queryResult.getT1(), queryResult.getT2());

            return allRightFromValue(rootBo.getCatalogs());
        });
    }

}
