package br.com.dotofcodex.javafx_sample.samples;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Sample2 extends Application {

	private static final Logger logger = LoggerFactory.getLogger(Sample2.class);
	private static final String PROPERTIES_FILE_LOCATION = "/home/pedro/.javafx_sample.properties";

	private String lastUsername;

	public Sample2() {
		super();
		logger.info("Sample 2 class instance initialized");
	}

	@Override
	public void init() {
		loadUsername();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Sample 2");
		stage.setResizable(false);
		stage.getIcons().add(new Image(Sample2.class.getResourceAsStream("/icon.png")));

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Scene scene = new Scene(grid, 500, 180);
		stage.setScene(scene);

		Text status = new Text();
		status.setVisible(false);
		status.setFill(Color.RED);
		status.setText("Wrong credentials");

		Label label1 = new Label("Username: ");
		grid.add(label1, 0, 1);

		TextField username = new TextField();
		username.setPrefWidth(280);
		username.setMaxWidth(280);
		username.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					status.setVisible(false);
					username.setStyle("");
				}
			}
		});
		username.setText(lastUsername);
		grid.add(username, 1, 1);

		Label label2 = new Label("Password: ");
		grid.add(label2, 0, 2);

		PasswordField password = new PasswordField();
		password.setPrefWidth(280);
		password.setMaxWidth(280);
		password.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					status.setVisible(false);
					password.setStyle("");
				}
			}
		});
		grid.add(password, 1, 2);

		CheckBox rememberMe = new CheckBox("Remember E-mail?");
		HBox rememberBox = new HBox(10);
		rememberBox.setAlignment(Pos.BOTTOM_LEFT);
		rememberBox.getChildren().add(rememberMe);
		
		if (lastUsername != null) {
			rememberMe.setSelected(true);
		}
		
		rememberMe.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					clearUsername();
				}
			}
		});
		grid.add(rememberBox, 1, 3);

		Button login = new Button("Sign in");
		login.setDefaultButton(true);
		login.setOnAction((ActionEvent event) -> {
			boolean isEmpty = false;
			String user = username.getText();
			String pass = password.getText();

			if (user.isEmpty()) {
				isEmpty = true;
				status.setVisible(true);
				username.setStyle("-fx-text-box-border: red; -focus-color: red;");
			}

			if (pass.isEmpty()) {
				isEmpty = true;
				status.setVisible(true);
				password.setStyle("-fx-text-box-border: red; -focus-color: red;");
			}

			if (isEmpty) {
				return;
			}

			if (user.equals("pedro@gmail.com") && pass.equals("123")) {
				String message = "Logged in with success.";
				if (rememberMe.isSelected()) {
					message = message.concat("\nRemember me active.");
					saveUsername(user);
				} else {
					clearUsername();
				}

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Info Dialog");
				alert.setHeaderText("Hello, Pedro");
				alert.setContentText(message);
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Info Dialog");
				alert.setHeaderText("Hello, Dear");
				alert.setContentText("Sorry, wrong credentials");
				alert.showAndWait();
			}
		});

		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(login);
		grid.add(hbBtn, 1, 4);

		HBox message = new HBox(10);
		message.setAlignment(Pos.BOTTOM_CENTER);
		message.getChildren().add(status);
		grid.add(message, 1, 5);

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Dialog");
				alert.setHeaderText("Hello, Dear");
				alert.setContentText("Do you wan to exit?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() != ButtonType.OK) {
					logger.info("not closing application...");
					event.consume();
					return;
				}

				logger.info("closing application...");
			}
		});

		stage.show();

	}

	private void saveUsername(String username) {
		Properties properties = new Properties();
		properties.put("username", username);
		try {
			properties.store(new FileOutputStream(PROPERTIES_FILE_LOCATION), null);
			logger.info("properties file saved with success.");
		} catch (IOException e) {
			logger.error("failed to save properties file");
			e.printStackTrace();
		}
	}

	private void loadUsername() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(PROPERTIES_FILE_LOCATION));
			lastUsername = (String) props.get("username");
			logger.info("properties file loaded with success");
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof FileNotFoundException) {
				logger.info("failed to load the file - not found");
			}
		}
	}

	private void clearUsername() {
		Properties properties = new Properties();
		properties.clear();
		try {
			properties.store(new FileOutputStream(PROPERTIES_FILE_LOCATION), null);
			logger.info("properties file cleared and saved with success.");
		} catch (IOException e) {
			logger.error("failed to save properties file");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
