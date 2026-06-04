package tr.edu.ozyegin.cs105.registration.business;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tr.edu.ozyegin.cs105.registration.data.Course;
import tr.edu.ozyegin.cs105.registration.data.Professor;
import tr.edu.ozyegin.cs105.registration.data.Student;
import tr.edu.ozyegin.cs105.registration.data.seed.MockDatabase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegistrationService {

    private final MockDatabase db;

    private final ObservableList<Student> allStudents;
    private final ObservableList<Course> allCourses;

    public RegistrationService(MockDatabase db) {
        this.db = db;
        this.allStudents = FXCollections.observableArrayList(db.students().findAll());
        this.allCourses = FXCollections.observableArrayList(db.courses().findAll());
    }

    public ObservableList<Student> allStudents() {
        return allStudents;
    }

    public ObservableList<Course> allCourses() {
        return allCourses;
    }

    public ObservableList<Course> coursesFor(String studentNumber) {
        return FXCollections.observableArrayList(
                db.enrollments().findCoursesOf(studentNumber));
    }

    public ObservableList<Student> rosterFor(int courseId) {
        return FXCollections.observableArrayList(
                db.enrollments().findStudentsIn(courseId));
    }

    public ObservableList<Professor> instructorsFor(int courseId) {
        return FXCollections.observableArrayList(
                db.teachingAssignments().findInstructorsOf(courseId));
    }

    public ObservableList<Student> unenrolledIn(int courseId) {
        Set<String> enrolled = new HashSet<>();
        for (Student s : db.enrollments().findStudentsIn(courseId)) {
            enrolled.add(s.getStudentNumber());
        }
        ObservableList<Student> result = FXCollections.observableArrayList();
        for (Student s : allStudents) {
            if (!enrolled.contains(s.getStudentNumber())) {
                result.add(s);
            }
        }
        return result;
    }

    public EnrollmentResult enroll(String studentNumber, int courseId) {
        if (db.students().findById(studentNumber).isEmpty()) {
            return new EnrollmentResult(false,"Student can't found");
        }
        if (db.courses().findById(courseId).isEmpty()) {
            return new EnrollmentResult(false,"Course can't found");
        }
        int currentEnrolledCount = db.enrollments().findStudentsIn(courseId).size();

        if (currentEnrolledCount >= db.courses().findById(courseId).get().getCapacity()) {
            return new EnrollmentResult(false,"Course is full");
        }
        if (checkTimeConflict(studentNumber, courseId) != 0) {
            return new EnrollmentResult(false,("Time conflict with course " + db.courses().findById(courseId).get().getCourseCode()));
        }

        return db.enrollments().enroll(studentNumber, courseId);
    }

    public int checkTimeConflict(String studentNumber, int courseId) {
        List<Course> courses = db.enrollments().findCoursesOf(studentNumber);
        for (Course c : courses) {
            if (c.getDayOfWeek() == db.courses().findById(courseId).get().getDayOfWeek()) {
                if (c.getStartTime().isBefore(db.courses().findById(courseId).get().getEndTime()) &&
                        c.getEndTime().isAfter(db.courses().findById(courseId).get().getStartTime())) {
                    return c.getCourseId();
                }
            }
        }
        return 0;

    }

    public boolean drop(String studentNumber, int courseId) {
        return db.enrollments().drop(studentNumber, courseId);
    }
    public Student addStudent(String studentNumber,
                              String firstName,
                              String lastName) {

        if (studentNumber == null || studentNumber.isBlank()) {
            throw new IllegalArgumentException("Student number cannot be empty.");
        }

        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be empty.");
        }

        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be empty.");
        }

        studentNumber = studentNumber.trim();
        firstName = firstName.trim();
        lastName = lastName.trim();

        if (db.students().findById(studentNumber).isPresent()) {
            throw new IllegalArgumentException("Student already exists.");
        }

        Student student = new Student(firstName, lastName, studentNumber);

        db.students().save(student);
        allStudents.add(student);

        return student;
    }
}
