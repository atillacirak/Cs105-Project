package tr.edu.ozyegin.cs105.registration.data.persistance;

import tr.edu.ozyegin.cs105.registration.data.Course;
import tr.edu.ozyegin.cs105.registration.data.Employee;
import tr.edu.ozyegin.cs105.registration.data.Student;

import java.util.List;

public record DatabaseState(
        List<Student> students,
        List<Course> courses,
        List<Employee> employees,
        List<EnrollmentEntry> enrollments,
        List<AssignmentEntry> assignments
) {

    /** A single student-in-a-course row. */
    public record EnrollmentEntry(String studentNumber, int courseId) {}

    /** A single professor-teaches-a-course row. */
    public record AssignmentEntry(String employeeNumber, int courseId) {}
}
