package excellent.cancer.floor.repository;

public interface RepositoryDatabase<K, P> {

    RepositoryOptions<K, P> repositoryOptions(K key);

    RepositoryOptions<K, P> addRepositoryOptions(K key, String remote);

}
