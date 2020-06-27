package gray.light.note.service;

import gray.light.definition.entity.Scope;
import gray.light.owner.entity.OwnerProject;
import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.entity.ProjectStatus;
import gray.light.owner.service.OverallOwnerService;
import gray.light.owner.service.ProjectDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import perishing.constraint.jdbc.Page;

import java.util.List;

/**
 * 笔记服务
 *
 * @author XyParaCrim
 */
@Service
@RequiredArgsConstructor
public class NoteService {

    private final OverallOwnerService overallOwnerService;

    private final ProjectDetailsService projectDetailsService;

    /**
     * 获取指定所属者的笔记
     *
     * @param ownerId 所属者Id
     * @param page    分页
     * @return 获取指定所属者的笔记
     */
    public List<OwnerProject> noteProject(Long ownerId, Page page) {
        return overallOwnerService.projects(ownerId, Scope.NOTE, page);
    }

    /**
     * 创建一个笔记项目并追踪它
     *
     * @param noteProject 笔记项目
     * @param uncommited  项目详细
     * @return 是否创建成功
     */
    public boolean createNote(OwnerProject noteProject, ProjectDetails uncommited) {
        return projectDetailsService.addBookProjectDetailsSafely(noteProject, Scope.NOTE, uncommited);
    }

    /**
     * 查询指定范围和note的项目详细
     *
     * @param status 项目状态
     * @param page   分页
     * @return 查询指定范围和note的项目详细
     */
    public List<ProjectDetails> findProjectDetailsByStatus(ProjectStatus status, Page page) {
        return projectDetailsService.findScopeProjectDetails(status, Scope.NOTE, page);
    }
}
