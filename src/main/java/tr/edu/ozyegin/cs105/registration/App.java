package tr.edu.ozyegin.cs105.registration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tr.edu.ozyegin.cs105.registration.business.RegistrationService;
import tr.edu.ozyegin.cs105.registration.data.persistance.DatabaseStateStore;
import tr.edu.ozyegin.cs105.registration.data.seed.MockDatabase;
import tr.edu.ozyegin.cs105.registration.ui.MainController;

import java.io.IOException;
import java.nio.file.Path;

public class App extends Application {

    private static final Path STATE_FILE = Path.of("state.json");

    private final DatabaseStateStore store = new DatabaseStateStore(STATE_FILE);
    private MockDatabase db;

    @Override
    public void start(Stage stage) throws Exception {
        db = loadOrSeed();
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

    @Override
    public void stop() throws Exception {
        if (db != null) {
            store.save(db);
        }
    }

    /** Loads the saved state if present, otherwise seeds a fresh database. */
    private MockDatabase loadOrSeed() {
        if (store.exists()) {
            try {
                return store.load();
            } catch (IOException e) {
                System.err.println("Could not load " + STATE_FILE + ", falling back to seed data: " + e.getMessage());
            }
        }
        MockDatabase fresh = new MockDatabase();
        fresh.seed();
        return fresh;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
