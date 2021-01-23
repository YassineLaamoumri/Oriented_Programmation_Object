package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class DKButton extends Button{
	private final String FONT_PATH = "src/model/resources/kenvector_future.ttf";
	private final String BUTTON_PRESSED_STYLE = "-fx-background-image: url('model/resources/red_button_pressed'); -fx-background-color: transparent;";
	private final String BUTTON_STYLE = "-fx-background-image: url('model/resources/red_button.png');-fx-background-color: transparent;";

	public  DKButton(String text,int size_font) {
		setText(text);
		setButtonFont(size_font);
		setPrefWidth(190);
		setPrefHeight(49);
		setStyle(BUTTON_STYLE);
		initializeButtonListeners();
	}
	private void setButtonFont(int size_font) {
		try {
			setFont(Font.loadFont(new FileInputStream(FONT_PATH),23));
		} catch (FileNotFoundException e) {
			setFont(Font.font("Verdana",size_font));
		}

	}
	private void setButtonPressedStyle() {
		setStyle(BUTTON_PRESSED_STYLE);
		setPrefHeight(45);
		setLayoutY(getLayoutY() +4);
	}

	private void setButtonStyle() {
		setStyle(BUTTON_STYLE);
		setPrefHeight(49);
		setLayoutY(getLayoutY() -4);
	}

	private void initializeButtonListeners() {
		setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if(event.getButton().equals(MouseButton.PRIMARY)) {
					setButtonPressedStyle();
				}
			}
		});
		setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if(event.getButton().equals(MouseButton.PRIMARY)) {
					setButtonStyle();
				}
			}
		});
		
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				setEffect(new DropShadow());
			}
		});
		
		setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				setEffect(null);
			}
		});
	}
	

}
