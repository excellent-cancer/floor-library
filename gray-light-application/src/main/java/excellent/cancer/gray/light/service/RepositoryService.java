package excellent.cancer.gray.light.service;

import excellent.cancer.gray.light.jdbc.repositories.DocumentCatalogRepository;
import excellent.cancer.gray.light.jdbc.repositories.DocumentRepository;
import excellent.cancer.gray.light.jdbc.repositories.OwnerProjectRepository;
import excellent.cancer.gray.light.jdbc.repositories.OwnerRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 集中储存所有repositories，避免Idea无法找到mybatis的mapper bean
 *
 * @author XyParaCrim
 */
@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class RepositoryService {

    @NonNull
    private final OwnerRepository ownerRepository;

    @NonNull
    private final OwnerProjectRepository ownerProjectRepository;

    @NonNull
    private final DocumentCatalogRepository documentCatalogRepository;

    @NonNull
    private final DocumentRepository documentRepository;

    @Autowired
    public RepositoryService(@NonNull OwnerRepository ownerRepository,
                             @NonNull OwnerProjectRepository ownerProjectRepository,
                             @NonNull DocumentCatalogRepository documentCatalogRepository,
                             @NonNull DocumentRepository documentRepository) {
        this.ownerRepository = ownerRepository;
        this.ownerProjectRepository = ownerProjectRepository;
        this.documentCatalogRepository = documentCatalogRepository;
        this.documentRepository = documentRepository;
    }

    public OwnerRepository ofOwner() {
        return ownerRepository;
    }

    public OwnerProjectRepository ofOwnerProject() {
        return ownerProjectRepository;
    }

    public DocumentRepository ofDocument() {
        return documentRepository;
    }

    public DocumentCatalogRepository ofDocumentCatalog() {
        return documentCatalogRepository;
    }
}
