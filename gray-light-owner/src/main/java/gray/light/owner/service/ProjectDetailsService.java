package gray.light.owner.service;

import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.repository.ProjectDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 定义针对Git项目细节的服务，例如：同步情况等
 *
 * @author XyParaCrim
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectDetailsService {

    private final ProjectDetailsRepository projectDetailsRepository;

    public boolean addProjectDetails(ProjectDetails projectDetails) {
        return projectDetailsRepository.save(projectDetails);
    }

}
