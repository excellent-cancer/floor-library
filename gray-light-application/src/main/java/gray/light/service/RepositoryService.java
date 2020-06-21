package gray.light.service;

import gray.light.document.repository.DocumentCatalogsRepository;
import gray.light.document.repository.DocumentChapterRepository;
import gray.light.document.repository.DocumentRepository;
import gray.light.owner.repository.OwnerProjectRepository;
import gray.light.owner.repository.OwnerRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 集中储存所有repositories，避免Idea无法找到mybatis的mapper bean
 *
 * @author XyParaCrim
 */
@Service
public class RepositoryService {

    @NonNull
    private final OwnerRepository ownerRepository;

    @NonNull
    private final OwnerProjectRepository ownerProjectRepository;

    @NonNull
    private final DocumentRepository documentRepository;

    @NonNull
    private final DocumentCatalogsRepository catalogsRepository;

    @NonNull
    private final DocumentChapterRepository chapterRepository;

    @Autowired
    public RepositoryService(@NonNull OwnerRepository ownerRepository,
                             @NonNull OwnerProjectRepository ownerProjectRepository,
                             @NonNull DocumentRepository documentRepository,
                             @NonNull DocumentCatalogsRepository catalogsRepository,
                             @NonNull DocumentChapterRepository chapterRepository) {
        this.ownerRepository = ownerRepository;
        this.ownerProjectRepository = ownerProjectRepository;
        this.documentRepository = documentRepository;
        this.catalogsRepository = catalogsRepository;
        this.chapterRepository = chapterRepository;
    }


    public OwnerRepository owner() {
        return ownerRepository;
    }

    public OwnerProjectRepository ownerProject() {
        return ownerProjectRepository;
    }

    public DocumentRepository document() {
        return documentRepository;
    }

    public DocumentCatalogsRepository documentCatalogs() {
        return catalogsRepository;
    }

    public DocumentChapterRepository documentChapter() {
        return chapterRepository;
    }
}
