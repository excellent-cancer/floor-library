package gray.light.note.handler;

import gray.light.book.handler.BookHandler;
import gray.light.definition.entity.Scope;
import gray.light.note.business.NoteBo;
import gray.light.note.business.NoteFo;
import gray.light.note.service.NoteService;
import gray.light.owner.customizer.OwnerProjectCustomizer;
import gray.light.owner.customizer.ProjectDetailsCustomizer;
import gray.light.owner.entity.OwnerProject;
import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.service.OverallOwnerService;
import gray.light.support.error.NormalizingFormException;
import gray.light.support.web.RequestSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import perishing.constraint.jdbc.Page;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static gray.light.support.web.ResponseToClient.allRightFromValue;
import static gray.light.support.web.ResponseToClient.failWithMessage;

/**
 * @author XyParaCrim
 */
@Slf4j
@RequiredArgsConstructor
public class NoteHandler {

    private final NoteService noteService;

    private final BookHandler bookHandler;

    private final OverallOwnerService overallOwnerService;

    /**
     * 查询指定所属者的note
     *
     * @param request 服务请求
     * @return 回复
     */
    public Mono<ServerResponse> queryNote(ServerRequest request) {
        return RequestSupport.extractOwnerId(request, ownerId -> {
            Page page = RequestSupport.extractPage(request);
            return allRightFromValue(noteService.noteProject(ownerId, page));
        });
    }

    /**
     * 为所属者添加一个笔记
     *
     * @param request 服务请求
     * @return Response of Publisher
     */
    public Mono<ServerResponse> createNote(ServerRequest request) {
        return request.
                bodyToMono(NoteFo.class).
                flatMap(noteFo -> {
                    try {
                        noteFo.normalize();
                    } catch (NormalizingFormException e) {
                        log.error(e.getMessage());
                        return failWithMessage(e.getMessage());
                    }

                    OwnerProject noteProject = OwnerProjectCustomizer.fromForm(noteFo.getNote(), Scope.NOTE);
                    ProjectDetails uncommited = ProjectDetailsCustomizer.uncommitProjectDetails(noteFo.getSource());

                    if (noteService.createNote(noteProject, uncommited)) {
                        Optional<OwnerProject> savedProject = overallOwnerService.findProject(noteProject.getId());
                        if (savedProject.isPresent()) {
                            return allRightFromValue(NoteBo.of(savedProject.get()));
                        }
                    }

                    return failWithMessage("Failed to create note");

                });
    }


    /**
     * 查询笔记仓库的结构树
     *
     * @param request 服务请求
     * @return Response of Publisher
     */
    public Mono<ServerResponse> queryNoteTree(ServerRequest request) {
        // TODO 验证
        return bookHandler.queryBookTree(request);
    }

}
