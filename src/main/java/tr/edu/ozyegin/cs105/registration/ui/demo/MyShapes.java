package tr.edu.ozyegin.cs105.registration.ui.demo;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.beans.binding.When;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class MyShapes extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Create an Ellipse and set fill color
        Ellipse ellipse = new Ellipse(110, 70);

        Stop[] stops = new Stop[] {
                new Stop(0, Color.DODGERBLUE),
                new Stop(0.5, Color.LIGHTBLUE),
                new Stop(1.0, Color.LIGHTGREEN)};

        // startX=0, startY=0, endX=0, endY=1
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);

        Reflection reflection = new Reflection();
        reflection.setFraction(.8);
        reflection.setTopOffset(1.0);


        ellipse.setFill(gradient);
        ellipse.setEffect(new DropShadow(30, 10, 10, Color.GRAY));


        // Create a Text shape with font and size
        Text text = new Text("My Shapes");

        text.setFont(new Font("Times New Roman", 24));
        text.setEffect(reflection);

        text.setOnMouseClicked(
                mouseEvent ->
        {
            System.out.println(mouseEvent.getSource().getClass().toString() + " clicked.");
        }
        );

        ellipse.setOnMouseClicked(
                mouseEvent ->
                {
                    System.out.println(mouseEvent.getSource().getClass().toString() + " clicked.");
                }
        );


        StackPane stackPane = new StackPane();

        stackPane.getChildren().addAll(ellipse, text);


        // Define RotateTransition
        RotateTransition rotateTransition = new RotateTransition(
                Duration.millis(2500), stackPane);

        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);

        rotateTransition.setInterpolator(Interpolator.LINEAR);
        // configure mouse click handler
        stackPane.setOnMouseClicked(mouseEvent -> {
            if (rotateTransition.getStatus().equals(Animation.Status.RUNNING)) {
                rotateTransition.pause();
            } else {
                rotateTransition.play();
            }
        });



        Text text2 = new Text("STOPPED");

        text2.setFont(new Font("Times New Roman", 24));


        text2.rotateProperty().bind(stackPane.rotateProperty());

        text2.textProperty().bind(stackPane.rotateProperty().asString("%.1f"));

        text2.strokeProperty().bind(new When(rotateTransition.statusProperty()
                .isEqualTo(Animation.Status.RUNNING))
                .then(Color.GREEN).otherwise(Color.RED));

        VBox vbox = new VBox();
        vbox.getChildren().add(stackPane);
        vbox.getChildren().add(text2);



        Scene scene = new Scene(vbox, 350, 230, Color.LIGHTYELLOW);
        stage.setTitle("MyShapes with JavaFX");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
