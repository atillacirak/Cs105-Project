package tr.edu.ozyegin.cs105.registration.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tr.edu.ozyegin.cs105.registration.business.EnrollmentResult;
import tr.edu.ozyegin.cs105.registration.business.RegistrationService;
import tr.edu.ozyegin.cs105.registration.data.Course;
import tr.edu.ozyegin.cs105.registration.data.Professor;
import tr.edu.ozyegin.cs105.registration.data.Student;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class CoursesViewController {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, String> codeCol;
    @FXML private TableColumn<Course, String> titleCol;
    @FXML private TableColumn<Course, String> scheduleCol;

    @FXML private Label courseHeader;
    @FXML private Label instructorLabel;
    @FXML private Label scheduleLabel;
    @FXML private Label capacityLabel;


    @FXML private TableView<Student> rosterTable;
    @FXML private TableColumn<Student, String> rosterNumberCol;
    @FXML private TableColumn<Student, String> rosterFirstCol;
    @FXML private TableColumn<Student, String> rosterLastCol;

    @FXML private ComboBox<Student> enrollComboBox;

    private RegistrationService service;

    @FXML
    public void initialize() {
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        scheduleCol.setCellValueFactory(data -> new SimpleStringProperty(formatSchedule(data.getValue())));

        rosterNumberCol.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        rosterFirstCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        rosterLastCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        coursesTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldC, newC) -> showDetail(newC));
    }

    public void setService(RegistrationService service) {
        this.service = service;
        coursesTable.setItems(service.allCourses());
        if (!coursesTable.getItems().isEmpty()) {
            coursesTable.getSelectionModel().selectFirst();
        }
    }

    private void showDetail(Course course) {
        if (course == null) {
            courseHeader.setText("Select a course");
            instructorLabel.setText("Instructor: -");
            scheduleLabel.setText("Schedule: -");
            rosterTable.getItems().clear();
            enrollComboBox.getItems().clear();
            return;
        }
        courseHeader.setText(course.getCourseCode() + " — " + course.getTitle());
        scheduleLabel.setText("Schedule: " + formatSchedule(course));

        ObservableList<Professor> instructors = service.instructorsFor(course.getCourseId());
        if (instructors.isEmpty()) {
            instructorLabel.setText("Instructor: -");
        } else {
            StringBuilder sb = new StringBuilder("Instructor: ");
            for (int i = 0; i < instructors.size(); i++) {
                if (i > 0) sb.append(", ");
                Professor p = instructors.get(i);
                sb.append(p.getTitle()).append(" ")
                        .append(p.getFirstName()).append(" ").append(p.getLastName());
            }
            instructorLabel.setText(sb.toString());
        }

        refreshRosterAndCombo(course);
    }

    /** Formats a course's meeting time, e.g. {@code "Mon 09:00–11:00"}. */
    private static String formatSchedule(Course course) {
        if (course.getDayOfWeek() == null || course.getStartTime() == null || course.getEndTime() == null) {
            return "-";
        }
        String day = course.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        return day + " " + course.getStartTime().format(TIME_FORMAT)
                + "–" + course.getEndTime().format(TIME_FORMAT);
    }

    private void refreshRosterAndCombo(Course course) {
        rosterTable.setItems(service.rosterFor(course.getCourseId()));
        enrollComboBox.setItems(service.unenrolledIn(course.getCourseId()));
        enrollComboBox.getSelectionModel().clearSelection();
        int enrolled = rosterTable.getItems().size();
        capacityLabel.setText("Capacity: " + enrolled + " / " + course.getCapacity());
    }

    @FXML
    public void onEnroll() {
        Course course = coursesTable.getSelectionModel().getSelectedItem();
        Student student = enrollComboBox.getSelectionModel().getSelectedItem();
        if (course == null || student == null) {
            return;
        }
        EnrollmentResult result = service.enroll(student.getStudentNumber(), course.getCourseId());

        if (result.success()) {
            service.enroll(student.getStudentNumber(), course.getCourseId());
            refreshRosterAndCombo(course);
            coursesTable.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Error");
            alert.setHeaderText("Registration failed:");
            alert.setContentText(result.message());
            alert.showAndWait();
            }

    }

    @FXML
    public void onDrop() {
        Course course = coursesTable.getSelectionModel().getSelectedItem();
        Student student = rosterTable.getSelectionModel().getSelectedItem();
        if (course == null || student == null) {
            return;
        }
        service.drop(student.getStudentNumber(), course.getCourseId());
        refreshRosterAndCombo(course);
    }
}
