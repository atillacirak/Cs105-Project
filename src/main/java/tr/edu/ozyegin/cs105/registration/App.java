package tr.edu.ozyegin.cs105.registration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tr.edu.ozyegin.cs105.registration.business.RegistrationService;
import tr.edu.ozyegin.cs105.registration.data.seed.MockDatabase;
import tr.edu.ozyegin.cs105.registration.ui.MainController;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        MockDatabase db = new MockDatabase();
        db.seed();
        RegistrationService service = new RegistrationService(db);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/Main.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.setService(service);

        Scene scene = new Scene(root, 1000, 650);
        stage.setTitle("Course Registration");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
