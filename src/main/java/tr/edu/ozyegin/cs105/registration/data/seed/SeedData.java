package tr.edu.ozyegin.cs105.registration.data.seed;

import tr.edu.ozyegin.cs105.registration.data.Course;
import tr.edu.ozyegin.cs105.registration.data.Professor;
import tr.edu.ozyegin.cs105.registration.data.Staff;

import java.util.List;

public final class SeedData {

    private SeedData() {}

    public static final List<String> FIRST_NAMES = List.of(
            "Ayşe", "Mehmet", "Zeynep", "Emre", "Elif", "Can", "Burak", "Selin",
            "Deniz", "Cem", "Ece", "Merve", "Ali", "Fatma", "Ahmet", "Defne",
            "Kerem", "Naz", "Berk", "İrem", "Pınar", "Buse", "Cansu", "Onur",
            "Murat", "Tolga", "Ege", "Yağmur", "Sinem", "Gizem"
    );

    public static final List<String> LAST_NAMES = List.of(
            "Yılmaz", "Kaya", "Demir", "Şahin", "Çelik", "Yıldız", "Yıldırım",
            "Öztürk", "Aydın", "Özdemir", "Arslan", "Doğan", "Koç", "Kurt",
            "Polat", "Aslan", "Çetin", "Şimşek", "Erdoğan", "Korkmaz",
            "Avcı", "Tekin", "Bulut", "Ergin", "Acar"
    );

    public static final List<Course> COURSES = List.of(
            new Course(4001, "CS101",   "Computational Thinking"),
            new Course(4002, "CS105",   "Introduction to Programming"),
            new Course(4003, "CS201",   "Data Structures and Algorithms"),
            new Course(4004, "CS302",   "Operating Systems"),
            new Course(4005, "CS308",   "Software Engineering"),
            new Course(4006, "MATH101", "Calculus I"),
            new Course(4007, "MATH102", "Calculus II"),
            new Course(4008, "MATH211", "Discrete Mathematics"),
            new Course(4009, "NS101",   "Physics I"),
            new Course(4010, "HUM101",  "Critical Reading and Writing")
    );

    public static final List<Professor> PROFESSORS = List.of(
            new Professor("Mehmet", "Demir",    "E001", "Prof. Dr."),
            new Professor("Ayşe",   "Yıldız",   "E002", "Doç. Dr."),
            new Professor("Can",    "Aydın",    "E003", "Dr. Öğr. Üyesi"),
            new Professor("Selin",  "Kaya",     "E004", "Prof. Dr."),
            new Professor("Emre",   "Şahin",    "E005", "Doç. Dr."),
            new Professor("Zeynep", "Çelik",    "E006", "Dr. Öğr. Üyesi"),
            new Professor("Ahmet",  "Özdemir",  "E007", "Prof. Dr.")
    );

    public static final List<Staff> STAFF = List.of(
            new Staff("Burak", "Arslan", "S001"),
            new Staff("Defne", "Doğan",  "S002"),
            new Staff("Kerem", "Koç",    "S003")
    );

    /** courseId -> employeeNumber of the assigned instructor */
    public static final List<TeachingAssignmentSeed> TEACHING_ASSIGNMENTS = List.of(
            new TeachingAssignmentSeed(4001, "E002"),
            new TeachingAssignmentSeed(4002, "E001"),
            new TeachingAssignmentSeed(4003, "E003"),
            new TeachingAssignmentSeed(4004, "E001"),
            new TeachingAssignmentSeed(4005, "E002"),
            new TeachingAssignmentSeed(4006, "E004"),
            new TeachingAssignmentSeed(4007, "E004"),
            new TeachingAssignmentSeed(4008, "E005"),
            new TeachingAssignmentSeed(4009, "E006"),
            new TeachingAssignmentSeed(4010, "E007")
    );

    public record TeachingAssignmentSeed(int courseId, String employeeNumber) {}

    public static final int STUDENT_COUNT = 50;
    public static final long RANDOM_SEED = 42L;
    public static final int MIN_COURSES_PER_STUDENT = 4;
    public static final int MAX_COURSES_PER_STUDENT = 6;
}
