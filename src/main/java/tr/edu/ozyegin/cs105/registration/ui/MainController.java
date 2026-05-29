package tr.edu.ozyegin.cs105.registration.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import tr.edu.ozyegin.cs105.registration.business.RegistrationService;

import java.io.IOException;
import java.util.function.Consumer;

public class MainController {

    @FXML private StackPane contentPane;

    private RegistrationService service;

    public void setService(RegistrationService service) {
        this.service = service;
        showStudents();
    }

    @FXML
    public void showStudents() {
        loadView("StudentsView.fxml",
                (StudentsViewController c) -> c.setService(service));
    }

    @FXML
    public void showCourses() {
        loadView("CoursesView.fxml",
                (CoursesViewController c) -> c.setService(service));
    }

    private <C> void loadView(String fxml, Consumer<C> wireController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent view = loader.load();
            wireController.accept(loader.getController());
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + fxml, e);
        }
    }
}
