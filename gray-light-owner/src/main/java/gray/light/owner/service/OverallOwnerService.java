package gray.light.owner.service;

import gray.light.definition.entity.Scope;
import gray.light.owner.entity.Owner;
import gray.light.owner.entity.OwnerProject;
import gray.light.owner.repository.OwnerProjectRepository;
import gray.light.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import perishing.constraint.jdbc.Page;

import java.util.List;
import java.util.Optional;

/**
 * 定义对于所属者的相关服务，例如：项目，介绍等等
 *
 * @author XyParaCrim
 */
@Slf4j
@RequiredArgsConstructor
public class OverallOwnerService {

    private final OwnerRepository ownerRepository;

    private final OwnerProjectRepository projectRepository;

    /**
     *  查询指定id的所属者
     *
     * @param ownerId 指定所属者id
     * @return 返回查询结果
     */
    public Optional<Owner> findOwner(Long ownerId) {
        return ownerRepository.findById(ownerId);
    }

    /**
     * 查询指定id的所属者项目
     *
     * @param projectId 指定id的所属者项目
     * @return 返回查询结果
     */
    public Optional<OwnerProject> findProject(Long projectId) {
        return projectRepository.findById(projectId);
    }

    /**
     * 是否存在该项目
     *
     * @param projectId 项目ID
     * @return 是否存在该项目
     */
    public boolean existsProject(Long projectId) {
        return projectRepository.existsById(projectId);
    }

    /**
     * 返回指定所属者的所有项目
     *
     * @param ownerId 指定所属者id
     * @param page 分页
     * @return 返回指定所属者的所有项目
     */
    public List<OwnerProject> projects(Long ownerId, Page page) {
        return projectRepository.findByOwnerIdAndScope(ownerId, null, page.nullable());
    }

    /**
     * 返回指定所属者的范围项目
     *
     * @param ownerId 指定所属者id
     * @param scope 范围
     * @param page 分页
     * @return 返回指定所属者的所有项目
     */
    public List<OwnerProject> projects(Long ownerId, Scope scope, Page page) {
        return projectRepository.findByOwnerIdAndScope(ownerId, scope.getName(), page.nullable());
    }

    /**
     * 添加指定所属者项目
     *
     * @param project 指定所属者项目
     * @return 是否添加成功
     */
    public boolean addProject(OwnerProject project) {
        return projectRepository.save(project);
    }

}
