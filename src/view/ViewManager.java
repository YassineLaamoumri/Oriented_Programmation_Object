package view;

import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.DKButton;

public class ViewManager {
	private static final int HEIGHT = 600;
	private static final int WIDTH = 600;
	private AnchorPane mainPane;
	private Scene mainScene;
	private Stage mainStage;

	public ViewManager() throws FileNotFoundException {
		mainPane = new AnchorPane();
		mainScene = new Scene(mainPane,WIDTH,HEIGHT);
		mainStage = new Stage();
		mainStage.setScene(mainScene);
		mainStage.setTitle("Dukes Of Realm");
		createButtons();
		createBackground();
		createLogo();
	}
	public Stage getMainStage() {
		return mainStage;
	}

	private DKButton createButtons( ) {
		DKButton startButton = new DKButton("PLAY",23);
		
		mainPane.getChildren().add(startButton);
		
		startButton.setLayoutX(200);
		startButton.setLayoutY(450);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				GameViewManager gameManager = new GameViewManager();
				gameManager.createNewGame(mainStage);
				
			}
			
		});
		return startButton;
	}
	private void createBackground() throws FileNotFoundException {

		BackgroundFill myBF = new BackgroundFill(Color.web("540000"), new CornerRadii(1),
		         null);
		
		mainPane.setBackground(new Background(myBF));
				
	}
	private void createLogo() {
		ImageView logo = new ImageView("view/resources/logo.png");
		logo.setLayoutX(150);
		logo.setLayoutY(200);
		
		logo.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				logo.setEffect(new DropShadow());
				
			}
	});
		logo.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				logo.setEffect(null);
			}
		});
		mainPane.getChildren().add(logo);
	}

}
