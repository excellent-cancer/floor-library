package org.excellent.cancer.experimental.application.service;

import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.excellent.cancer.experimental.application.error.UniqueOwnerException;
import org.excellent.cancer.experimental.application.jdbc.entities.Owner;
import org.excellent.cancer.experimental.application.jdbc.entities.OwnerProject;
import org.excellent.cancer.experimental.application.jdbc.repositories.OwnerRepository;
import org.excellent.cancer.experimental.application.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 定义关于所属者的相关服务，例如：项目，介绍等等
 *
 * @author XyParaCrim
 */
@Service
@CommonsLog
public final class OwnerService {

    @Getter
    private final Owner owner;

    private final OwnerRepository ownerRepository;

    /**
     * 不仅在构造函数中设置必要的参数，而且会查询出唯一的owner，并且将其设置到全局变量中
     *
     * @throws UniqueOwnerException 如果不能够成功初始化唯一owner
     * @param ownerRepository jdbc repository
     */
    @Autowired
    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
        this.owner = UniqueOwnerException.extractOwnerRequireUniqueOwner(CollectionUtils.asList(ownerRepository.findAll()));
        GlobalFinalVariables.set(Owner.class, this.owner);
    }

    /**
     * 返回owner的所有项目
     *
     * @return 返回一个包含项目列表的flux
     */
    public Mono<List<OwnerProject>> projects() {
        return Mono.fromFuture(ownerRepository.findProjectsByOwnerId(owner.getId()));
    }

    /**
     * 查询指定Id的项目
     *
     * @param projectId 项目Id
     * @return 返回包含项目操作的flux
     */
    public Mono<Optional<OwnerProject>> project(Long projectId) {
        return Mono.fromFuture(ownerRepository.findProjectByOwnerIdAndProjectId(owner.getId(), projectId));
    }
}
