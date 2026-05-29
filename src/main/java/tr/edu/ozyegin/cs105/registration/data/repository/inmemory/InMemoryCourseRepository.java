package tr.edu.ozyegin.cs105.registration.data.repository.inmemory;

import tr.edu.ozyegin.cs105.registration.data.Course;
import tr.edu.ozyegin.cs105.registration.data.repository.CourseRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public class InMemoryCourseRepository implements CourseRepository {

    private final LinkedHashMap<Integer, Course> byId = new LinkedHashMap<>();

    @Override
    public List<Course> findAll() {
        return new ArrayList<>(byId.values());
    }

    @Override
    public Optional<Course> findById(Integer courseId) {
        return Optional.ofNullable(byId.get(courseId));
    }

    @Override
    public Course save(Course course) {
        byId.put(course.getCourseId(), course);
        return course;
    }

    @Override
    public boolean delete(Integer courseId) {
        return byId.remove(courseId) != null;
    }

    @Override
    public int count() {
        return byId.size();
    }
}
