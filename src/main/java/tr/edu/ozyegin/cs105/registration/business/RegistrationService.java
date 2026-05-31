package tr.edu.ozyegin.cs105.registration.business;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tr.edu.ozyegin.cs105.registration.data.Course;
import tr.edu.ozyegin.cs105.registration.data.Professor;
import tr.edu.ozyegin.cs105.registration.data.Student;
import tr.edu.ozyegin.cs105.registration.data.seed.MockDatabase;

import java.util.HashSet;
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

    public boolean enroll(String studentNumber, int courseId) {
        if (db.students().findById(studentNumber).isEmpty()) {
            return false;
        }
        if (db.courses().findById(courseId).isEmpty()) {
            return false;
        }

        int currentEnrolledCount = db.enrollments().findStudentsIn(courseId).size();

        if (currentEnrolledCount >= db.courses().findById(courseId).get().getCapacity()) {
            return false;
        }
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
        return db.enrollments().enroll(studentNumber, courseId);
    }

    public boolean drop(String studentNumber, int courseId) {
        return db.enrollments().drop(studentNumber, courseId);
    }
}
