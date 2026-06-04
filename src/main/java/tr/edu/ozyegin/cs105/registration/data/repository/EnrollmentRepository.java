package tr.edu.ozyegin.cs105.registration.data.repository;

import tr.edu.ozyegin.cs105.registration.business.EnrollmentResult;
import tr.edu.ozyegin.cs105.registration.data.Course;
import tr.edu.ozyegin.cs105.registration.data.Student;

import java.util.List;

public interface EnrollmentRepository {

    EnrollmentResult enroll(String studentNumber, Integer courseId);

    boolean drop(String studentNumber, Integer courseId);

    boolean isEnrolled(String studentNumber, Integer courseId);

    List<Course> findCoursesOf(String studentNumber);

    List<Student> findStudentsIn(Integer courseId);

    int count();
}
