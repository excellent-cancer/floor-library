package gray.light.document.service;

import gray.light.definition.entity.Scope;
import gray.light.document.customizer.WorksDocumentCustomizer;
import gray.light.document.entity.WorksDocument;
import gray.light.document.repository.WorksDocumentRepository;
import gray.light.owner.entity.OwnerProject;
import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.entity.ProjectStatus;
import gray.light.owner.service.ProjectDetailsService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.transaction.annotation.Transactional;
import perishing.constraint.jdbc.Page;

import java.util.List;

/**
 * 提供对于文档之间的关系功能，例如：文件树、查询、删除等等
 *
 * @author XyParaCrim
 */
//@Service
@CommonsLog
public class DocumentRelationService {

    private final WorksDocumentRepository worksDocumentRepository;

    private final ProjectDetailsService projectDetailsService;

    public DocumentRelationService(WorksDocumentRepository worksDocumentRepository, ProjectDetailsService projectDetailsService) {
        this.worksDocumentRepository = worksDocumentRepository;
        this.projectDetailsService = projectDetailsService;
    }


    /**
     * 为works添加新文档
     *
     * @param documentOwnerProject 添加的新文档项目
     * @param projectId            要添加的项目Id
     * @param uncommited           未提交的项目详细
     * @return 是否创建并保存成功
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean addDocumentToWorks(Long projectId, OwnerProject documentOwnerProject, ProjectDetails uncommited) {
        if (projectDetailsService.addBookProjectDetailsSafely(documentOwnerProject, Scope.DOCUMENT, uncommited)) {

            WorksDocument document = WorksDocumentCustomizer.generate(documentOwnerProject, projectId);
            if (worksDocumentRepository.save(document)) {
                return true;
            }
            throw new RuntimeException("Failed to apply relation between works and document: " + documentOwnerProject);
        }

        return false;
    }

    /**
     * 获取指定项目状态的详细
     *
     * @param status 项目状态
     * @param page   分页
     * @return 获取指定项目状态的详细
     */
    @Transactional(readOnly = true)
    public List<ProjectDetails> findProjectDetailsByStatus(ProjectStatus status, Page page) {
        return worksDocumentRepository.findProjectDetailsByStatus(status, page.nullable());
    }

    /**
     * 查询works的所有文档
     *
     * @param worksId works-id
     * @param page 分页
     * @return 文档项目
     */
    public List<OwnerProject> findDocumentByWorks(Long worksId, Page page) {
        return worksDocumentRepository.findOwnerProjectByWorksId(worksId, page.nullable());
    }

}
