package tr.edu.ozyegin.cs105.registration.data.repository.inmemory;

import tr.edu.ozyegin.cs105.registration.data.Course;
import tr.edu.ozyegin.cs105.registration.data.Employee;
import tr.edu.ozyegin.cs105.registration.data.Professor;
import tr.edu.ozyegin.cs105.registration.data.repository.CourseRepository;
import tr.edu.ozyegin.cs105.registration.data.repository.EmployeeRepository;
import tr.edu.ozyegin.cs105.registration.data.repository.TeachingAssignmentRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InMemoryTeachingAssignmentRepository implements TeachingAssignmentRepository {

    private record Assignment(String employeeNumber, Integer courseId) {}

    private final Set<Assignment> rows = new HashSet<>();
    private final EmployeeRepository employeeRepo;
    private final CourseRepository courseRepo;

    public InMemoryTeachingAssignmentRepository(EmployeeRepository employeeRepo, CourseRepository courseRepo) {
        this.employeeRepo = employeeRepo;
        this.courseRepo = courseRepo;
    }

    @Override
    public boolean assign(String employeeNumber, Integer courseId) {
        Employee employee = employeeRepo.findById(employeeNumber).orElse(null);
        if (!(employee instanceof Professor)) {
            return false;
        }
        return rows.add(new Assignment(employeeNumber, courseId));
    }

    @Override
    public boolean unassign(String employeeNumber, Integer courseId) {
        return rows.remove(new Assignment(employeeNumber, courseId));
    }

    @Override
    public boolean isAssigned(String employeeNumber, Integer courseId) {
        return rows.contains(new Assignment(employeeNumber, courseId));
    }

    @Override
    public List<Course> findCoursesTaughtBy(String employeeNumber) {
        List<Course> result = new ArrayList<>();
        for (Assignment a : rows) {
            if (a.employeeNumber.equals(employeeNumber)) {
                courseRepo.findById(a.courseId).ifPresent(result::add);
            }
        }
        return result;
    }

    @Override
    public List<Professor> findInstructorsOf(Integer courseId) {
        List<Professor> result = new ArrayList<>();
        for (Assignment a : rows) {
            if (a.courseId.equals(courseId)) {
                employeeRepo.findById(a.employeeNumber)
                        .filter(Professor.class::isInstance)
                        .map(Professor.class::cast)
                        .ifPresent(result::add);
            }
        }
        return result;
    }

    @Override
    public int count() {
        return rows.size();
    }
}
