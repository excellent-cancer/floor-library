package floor.repository;

public interface RepositoryDatabase<K, P> {

    RepositoryOptions<K, P> repositoryOptions(K key) throws InvalidRepositoryException;

    RepositoryOptions<K, P> addRepositoryOptions(K key, String remote);

}
