package org.excellent.cancer.experimental.application.service;

import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.excellent.cancer.experimental.application.error.UniqueOwnerException;
import org.excellent.cancer.experimental.application.jdbc.entities.Owner;
import org.excellent.cancer.experimental.application.jdbc.repositories.OwnerRepository;
import org.excellent.cancer.experimental.application.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public OwnerService(OwnerRepository ownerRepository) {
        // 初始化阶段：设置唯一所属用户
        this.ownerRepository = ownerRepository;
        this.owner = UniqueOwnerException.extractOwnerRequireUniqueOwner(CollectionUtils.asList(ownerRepository.findAll()));
    }
}
