package tr.edu.ozyegin.cs105.registration.data.seed;

import tr.edu.ozyegin.cs105.registration.data.Course;
import tr.edu.ozyegin.cs105.registration.data.Professor;
import tr.edu.ozyegin.cs105.registration.data.Staff;
import tr.edu.ozyegin.cs105.registration.data.Student;
import tr.edu.ozyegin.cs105.registration.data.repository.CourseRepository;
import tr.edu.ozyegin.cs105.registration.data.repository.EmployeeRepository;
import tr.edu.ozyegin.cs105.registration.data.repository.EnrollmentRepository;
import tr.edu.ozyegin.cs105.registration.data.repository.StudentRepository;
import tr.edu.ozyegin.cs105.registration.data.repository.TeachingAssignmentRepository;
import tr.edu.ozyegin.cs105.registration.data.repository.inmemory.InMemoryCourseRepository;
import tr.edu.ozyegin.cs105.registration.data.repository.inmemory.InMemoryEmployeeRepository;
import tr.edu.ozyegin.cs105.registration.data.repository.inmemory.InMemoryEnrollmentRepository;
import tr.edu.ozyegin.cs105.registration.data.repository.inmemory.InMemoryStudentRepository;
import tr.edu.ozyegin.cs105.registration.data.repository.inmemory.InMemoryTeachingAssignmentRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MockDatabase {

    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final TeachingAssignmentRepository teachingAssignmentRepository;

    public MockDatabase() {
        this.studentRepository = new InMemoryStudentRepository();
        this.employeeRepository = new InMemoryEmployeeRepository();
        this.courseRepository = new InMemoryCourseRepository();
        this.enrollmentRepository = new InMemoryEnrollmentRepository(studentRepository, courseRepository);
        this.teachingAssignmentRepository = new InMemoryTeachingAssignmentRepository(employeeRepository, courseRepository);
    }

    public StudentRepository students() {
        return studentRepository;
    }

    public EmployeeRepository employees() {
        return employeeRepository;
    }

    public CourseRepository courses() {
        return courseRepository;
    }

    public EnrollmentRepository enrollments() {
        return enrollmentRepository;
    }

    public TeachingAssignmentRepository teachingAssignments() {
        return teachingAssignmentRepository;
    }

    public void seed() {
        Random random = new Random(SeedData.RANDOM_SEED);

        for (Course course : SeedData.COURSES) {
            courseRepository.save(course);
        }
        for (Professor professor : SeedData.PROFESSORS) {
            employeeRepository.save(professor);
        }
        for (Staff staff : SeedData.STAFF) {
            employeeRepository.save(staff);
        }
        for (SeedData.TeachingAssignmentSeed seed : SeedData.TEACHING_ASSIGNMENTS) {
            teachingAssignmentRepository.assign(seed.employeeNumber(), seed.courseId());
        }

        List<Student> generated = generateStudents(random);
        for (Student student : generated) {
            studentRepository.save(student);
        }
        enrollStudents(random, generated);
    }

    private List<Student> generateStudents(Random random) {
        List<Student> students = new ArrayList<>(SeedData.STUDENT_COUNT);
        Set<String> usedNumbers = new HashSet<>();
        int counter = 1;
        while (students.size() < SeedData.STUDENT_COUNT) {
            String firstName = SeedData.FIRST_NAMES.get(random.nextInt(SeedData.FIRST_NAMES.size()));
            String lastName  = SeedData.LAST_NAMES.get(random.nextInt(SeedData.LAST_NAMES.size()));
            int admissionYear = 2021 + random.nextInt(4);
            String studentNumber = String.format("%d%06d", admissionYear, counter++);
            if (!usedNumbers.add(studentNumber)) {
                continue;
            }
            students.add(new Student(firstName, lastName, studentNumber));
        }
        return students;
    }

    private void enrollStudents(Random random, List<Student> students) {
        List<Course> allCourses = new ArrayList<>(SeedData.COURSES);
        int span = SeedData.MAX_COURSES_PER_STUDENT - SeedData.MIN_COURSES_PER_STUDENT + 1;
        for (Student student : students) {
            int load = SeedData.MIN_COURSES_PER_STUDENT + random.nextInt(span);
            List<Course> shuffled = new ArrayList<>(allCourses);
            java.util.Collections.shuffle(shuffled, random);
            for (int i = 0; i < load && i < shuffled.size(); i++) {
                enrollmentRepository.enroll(student.getStudentNumber(), shuffled.get(i).getCourseId());
            }
        }
    }
}
