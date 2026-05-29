package tr.edu.ozyegin.cs105.registration.data.repository.inmemory;

import tr.edu.ozyegin.cs105.registration.data.Student;
import tr.edu.ozyegin.cs105.registration.data.repository.StudentRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public class InMemoryStudentRepository implements StudentRepository {

    private final LinkedHashMap<String, Student> byId = new LinkedHashMap<>();

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(byId.values());
    }

    @Override
    public Optional<Student> findById(String studentNumber) {
        return Optional.ofNullable(byId.get(studentNumber));
    }

    @Override
    public Student save(Student student) {
        byId.put(student.getStudentNumber(), student);
        return student;
    }

    @Override
    public boolean delete(String studentNumber) {
        return byId.remove(studentNumber) != null;
    }

    @Override
    public int count() {
        return byId.size();
    }
}
