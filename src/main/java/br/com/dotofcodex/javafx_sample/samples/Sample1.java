package br.com.dotofcodex.javafx_sample.samples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Sample1 extends Application {

	private static final Logger logger = LoggerFactory.getLogger(Sample1.class);

	public Sample1() {
		super();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("First Sample");

		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		btn.setOnAction((ActionEvent event) -> {
			logger.info("button clicked");
		});

		StackPane root = new StackPane();
		root.getChildren().add(btn);
		stage.setScene(new Scene(root, 300, 250));
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
