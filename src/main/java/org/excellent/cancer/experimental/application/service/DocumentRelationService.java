package org.excellent.cancer.experimental.application.service;

import lombok.extern.apachecommons.CommonsLog;
import org.excellent.cancer.experimental.application.jdbc.repositories.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;

/**
 * 提供关于文档之间的关系功能，例如：文件树、查询、删除等等
 *
 * @author XyParaCrim
 */
@Service
@CommonsLog
@ConditionalOnBean(NamedParameterJdbcOperations.class)
public class DocumentRelationService {

    private final OwnerRepository ownerRepository;

    @Autowired
    public DocumentRelationService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }


}
