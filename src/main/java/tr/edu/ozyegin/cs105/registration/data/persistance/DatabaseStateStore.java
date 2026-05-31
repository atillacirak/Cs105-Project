package tr.edu.ozyegin.cs105.registration.data.persistance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import tr.edu.ozyegin.cs105.registration.data.Course;
import tr.edu.ozyegin.cs105.registration.data.Employee;
import tr.edu.ozyegin.cs105.registration.data.Professor;
import tr.edu.ozyegin.cs105.registration.data.Staff;
import tr.edu.ozyegin.cs105.registration.data.Student;
import tr.edu.ozyegin.cs105.registration.data.persistance.DatabaseState.AssignmentEntry;
import tr.edu.ozyegin.cs105.registration.data.persistance.DatabaseState.EnrollmentEntry;
import tr.edu.ozyegin.cs105.registration.data.seed.MockDatabase;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes the entire database to a single JSON file using Gson, and
 * acts as a factory that rebuilds a fully-populated {@link MockDatabase} from
 * that file.
 *
 * <p>This is the only place that knows the persistence file exists. The business
 * and UI layers keep talking to {@link MockDatabase} and its repositories exactly
 * as before.</p>
 */
public final class DatabaseStateStore {

    private final Path file;
    private final Gson gson;

    public DatabaseStateStore(Path file) {
        this.file = file;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                // Gson would otherwise serialize an Employee using only the static
                // Employee type and lose the Professor/Staff distinction. The adapter
                // below writes/reads a "type" discriminator so the subclass survives.
                .registerTypeAdapter(Employee.class, new EmployeeAdapter())
                .create();
    }

    /** True if a saved state file already exists on disk. */
    public boolean exists() {
        return Files.isRegularFile(file);
    }

    /** Writes the current contents of {@code db} to the JSON file. */
    public void save(MockDatabase db) throws IOException {
        DatabaseState state = capture(db);
        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            gson.toJson(state, writer);
        }
    }

    /** Loads the JSON file and rebuilds a fully-populated {@link MockDatabase}. */
    public MockDatabase load() throws IOException {
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            DatabaseState state = gson.fromJson(reader, DatabaseState.class);
            if (state == null) {
                throw new IOException("State file is empty or malformed: " + file);
            }
            return build(state);
        }
    }

    /** Snapshots a database into a serializable {@link DatabaseState}. */
    private static DatabaseState capture(MockDatabase db) {
        List<Student> students = db.students().findAll();
        List<Course> courses = db.courses().findAll();
        List<Employee> employees = db.employees().findAll();

        List<EnrollmentEntry> enrollments = new ArrayList<>();
        for (Student student : students) {
            for (Course course : db.enrollments().findCoursesOf(student.getStudentNumber())) {
                enrollments.add(new EnrollmentEntry(student.getStudentNumber(), course.getCourseId()));
            }
        }

        List<AssignmentEntry> assignments = new ArrayList<>();
        for (Course course : courses) {
            for (Professor professor : db.teachingAssignments().findInstructorsOf(course.getCourseId())) {
                assignments.add(new AssignmentEntry(professor.getEmployeeNumber(), course.getCourseId()));
            }
        }

        return new DatabaseState(students, courses, employees, enrollments, assignments);
    }

    /** Rebuilds a database from a {@link DatabaseState}, in dependency order. */
    private static MockDatabase build(DatabaseState state) {
        MockDatabase db = new MockDatabase();

        // Save the entities first so enrollments/assignments can resolve their ids.
        for (Course course : nullSafe(state.courses())) {
            db.courses().save(course);
        }
        for (Employee employee : nullSafe(state.employees())) {
            db.employees().save(employee);
        }
        for (Student student : nullSafe(state.students())) {
            db.students().save(student);
        }
        for (EnrollmentEntry entry : nullSafe(state.enrollments())) {
            db.enrollments().enroll(entry.studentNumber(), entry.courseId());
        }
        for (AssignmentEntry entry : nullSafe(state.assignments())) {
            db.teachingAssignments().assign(entry.employeeNumber(), entry.courseId());
        }

        return db;
    }

    private static <T> List<T> nullSafe(List<T> list) {
        return list == null ? List.of() : list;
    }

    /**
     * Tags each serialized {@link Employee} with a {@code "type"} field and uses
     * it on the way back to pick the right subclass. Serialization delegates to
     * the concrete class's reflective adapter (no recursion: this adapter is only
     * registered for the abstract {@code Employee} type).
     */
    private static final class EmployeeAdapter
            implements JsonSerializer<Employee>, JsonDeserializer<Employee> {

        private static final String TYPE = "type";

        @Override
        public JsonElement serialize(Employee src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = context.serialize(src, src.getClass()).getAsJsonObject();
            obj.addProperty(TYPE, src.getClass().getSimpleName());
            return obj;
        }

        @Override
        public Employee deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            JsonObject obj = json.getAsJsonObject();
            JsonElement type = obj.get(TYPE);
            if (type == null) {
                throw new JsonParseException("Employee entry is missing its '" + TYPE + "' discriminator");
            }
            return switch (type.getAsString()) {
                case "Professor" -> context.deserialize(json, Professor.class);
                case "Staff" -> context.deserialize(json, Staff.class);
                default -> throw new JsonParseException("Unknown employee type: " + type.getAsString());
            };
        }
    }
}
