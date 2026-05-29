package tr.edu.ozyegin.cs105.registration.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tr.edu.ozyegin.cs105.registration.business.RegistrationService;
import tr.edu.ozyegin.cs105.registration.data.Course;
import tr.edu.ozyegin.cs105.registration.data.Student;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;

public class StudentsViewController {

    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, String> numberCol;
    @FXML private TableColumn<Student, String> firstNameCol;
    @FXML private TableColumn<Student, String> lastNameCol;
    @FXML private TextField searchField;

    @FXML private Label detailHeader;
    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, String> courseCodeCol;
    @FXML private TableColumn<Course, String> courseTitleCol;

    private RegistrationService service;

    @FXML
    public void initialize() {
        numberCol.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        courseCodeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        studentsTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldS, newS) -> showDetail(newS));
    }

    public void setService(RegistrationService service) {
        this.service = service;

        FilteredList<Student> filteredStudents =
                new FilteredList<>(service.allStudents(), student -> true);

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            String query = newText == null ? "" : newText.trim().toLowerCase();

            filteredStudents.setPredicate(student -> {
                if (query.isEmpty()) {
                    return true;
                }

                return student.getFirstName().toLowerCase().contains(query)
                        || student.getLastName().toLowerCase().contains(query)
                        || student.getStudentNumber().toLowerCase().contains(query);
            });
        });

        studentsTable.setItems(filteredStudents);

        if (!studentsTable.getItems().isEmpty()) {
            studentsTable.getSelectionModel().selectFirst();
        }
    }

    private void showDetail(Student student) {
        if (student == null) {
            detailHeader.setText("Select a student");
            coursesTable.getItems().clear();
            return;
        }
        detailHeader.setText(student.getFirstName() + " " + student.getLastName()
                + "  (" + student.getStudentNumber() + ")");
        coursesTable.setItems(service.coursesFor(student.getStudentNumber()));
    }
}
