package tr.edu.ozyegin.cs105.registration.data.repository;

import tr.edu.ozyegin.cs105.registration.data.Course;
import tr.edu.ozyegin.cs105.registration.data.Professor;

import java.util.List;

public interface TeachingAssignmentRepository {

    boolean assign(String employeeNumber, Integer courseId);

    boolean unassign(String employeeNumber, Integer courseId);

    boolean isAssigned(String employeeNumber, Integer courseId);

    List<Course> findCoursesTaughtBy(String employeeNumber);

    List<Professor> findInstructorsOf(Integer courseId);

    int count();
}
