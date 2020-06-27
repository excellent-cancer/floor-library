package gray.light.note.router;

import gray.light.note.handler.NoteHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.Predicate;

/**
 * 个人笔记提供的http请求
 *
 * @author XyParaCrim
 */
@Configuration
@RequiredArgsConstructor
public class PersonalNoteRouter {

    private final NoteHandler noteHandler;

    @Bean
    public RouterFunction<ServerResponse> addNote() {
        return RouterFunctions.route(RequestPredicates.POST("/owner/note"),
                noteHandler::createNote);
    }


    @Bean
    public RouterFunction<ServerResponse> getWorksDocument() {
        return RouterFunctions.route(RequestPredicates.GET("/owner/note"),
                noteHandler::queryNote);
    }

    @Bean
    public RouterFunction<ServerResponse> getFavoriteDocumentRepositoryTree() {
        return RouterFunctions.route(
                RequestPredicates.GET("/owner/note/tree").
                        and(RequestPredicates.queryParam("id", Predicate.not(StringUtils::isEmpty))),
                noteHandler::queryNoteTree
        );
    }


}
