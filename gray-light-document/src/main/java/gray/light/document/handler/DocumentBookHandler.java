package gray.light.document.handler;

import gray.light.book.handler.BookHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DocumentBookHandler {

    private final BookHandler bookHandler;

    /**
     * 查询文档仓库的结构树
     *
     * @param request 服务请求
     * @return Response of Publisher
     */
    public Mono<ServerResponse> queryDocumentRepositoryTree(ServerRequest request) {
        // TODO 验证
        return bookHandler.queryBookTree(request);
    }

}
