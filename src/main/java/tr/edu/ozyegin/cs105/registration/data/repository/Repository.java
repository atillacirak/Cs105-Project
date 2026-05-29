package tr.edu.ozyegin.cs105.registration.data.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {

    List<T> findAll();

    Optional<T> findById(ID id);

    T save(T entity);

    boolean delete(ID id);

    int count();
}
