package tr.edu.ozyegin.cs105.registration.data.repository.inmemory;

import tr.edu.ozyegin.cs105.registration.business.EnrollmentResult;
import tr.edu.ozyegin.cs105.registration.data.Course;
import tr.edu.ozyegin.cs105.registration.data.Student;
import tr.edu.ozyegin.cs105.registration.data.repository.CourseRepository;
import tr.edu.ozyegin.cs105.registration.data.repository.EnrollmentRepository;
import tr.edu.ozyegin.cs105.registration.data.repository.StudentRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InMemoryEnrollmentRepository implements EnrollmentRepository {

    private record Enrollment(String studentNumber, Integer courseId) {}

    private final Set<Enrollment> rows = new HashSet<>();
    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;

    public InMemoryEnrollmentRepository(StudentRepository studentRepo, CourseRepository courseRepo) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
    }

    @Override
    public EnrollmentResult enroll(String studentNumber, Integer courseId) {
        return new EnrollmentResult(rows.add(new Enrollment(studentNumber, courseId)), "Enrollment successful");
    }

    @Override
    public boolean drop(String studentNumber, Integer courseId) {
        return rows.remove(new Enrollment(studentNumber, courseId));
    }

    @Override
    public boolean isEnrolled(String studentNumber, Integer courseId) {
        return rows.contains(new Enrollment(studentNumber, courseId));
    }

    @Override
    public List<Course> findCoursesOf(String studentNumber) {
        List<Course> result = new ArrayList<>();
        for (Enrollment e : rows) {
            if (e.studentNumber.equals(studentNumber)) {
                courseRepo.findById(e.courseId).ifPresent(result::add);
            }
        }
        return result;
    }

    @Override
    public List<Student> findStudentsIn(Integer courseId) {
        List<Student> result = new ArrayList<>();
        for (Enrollment e : rows) {
            if (e.courseId.equals(courseId)) {
                studentRepo.findById(e.studentNumber).ifPresent(result::add);
            }
        }
        return result;
    }

    @Override
    public int count() {
        return rows.size();
    }
}
