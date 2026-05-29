package tr.edu.ozyegin.cs105.registration.ui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tr.edu.ozyegin.cs105.registration.business.RegistrationService;
public class AddStudentController {


        @FXML private TextField studentNumberField;
        @FXML private TextField firstNameField;
        @FXML private TextField lastNameField;
        @FXML private Label messageLabel;

        private RegistrationService service;

        public void setService(RegistrationService service) {
            this.service = service;
        }

        @FXML
        private void addStudent() {
            try {
                service.addStudent(
                        studentNumberField.getText(),
                        firstNameField.getText(),
                        lastNameField.getText()
                );

                messageLabel.setText("Student added successfully.");

                studentNumberField.clear();
                firstNameField.clear();
                lastNameField.clear();

            } catch (IllegalArgumentException e) {
                messageLabel.setText(e.getMessage());
            }
        }
    }

