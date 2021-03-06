public interface ICrudRepository<ID, T> {
    int size();
    void save(T entity);
    void delete(ID id);
    void update(T entity);
    T findOne(ID id);
    Iterable<T> findAll();
}