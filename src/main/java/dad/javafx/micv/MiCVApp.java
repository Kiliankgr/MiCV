package dad.javafx.micv;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MiCVApp extends Application {

	private MainController mainController;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		mainController = new MainController();
		
		Scene scene = new Scene(mainController.getRootView(), 640, 480);
		primaryStage.setTitle("MiCV - Kilian González Rodríguez");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);

	}

}
