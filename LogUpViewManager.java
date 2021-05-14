package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class LogUpViewManager {
    private static final int HEIGHT=500;
    private static final int WIDTH=500;
    private GridPane logupPane;
    private Scene logupScene;
    private Stage logupStage;


    public LogUpViewManager(){
        logupPane = new GridPane();
        logupScene = new Scene(logupPane,WIDTH,HEIGHT);
        logupStage = new Stage();
        logupStage.setTitle("Login");
        logupStage.setScene(logupScene);
        logupPane.setPadding(new Insets(10, 10, 10, 10));
        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.web("#FF0000"));
        GridPane.setConstraints(errorLabel, 1, 0);
        Label nameLabel = new Label("Adınız:");
        GridPane.setConstraints(nameLabel, 0, 1);
        TextField nameInput = new TextField("");
        GridPane.setConstraints(nameInput, 1, 1);
        Label surlbl = new Label("Soyadınız:");
        GridPane.setConstraints(surlbl, 0, 2);
        TextField surtext = new TextField();
        GridPane.setConstraints(surtext, 1, 2);
        Label kullaniciAdiLabel = new Label("Kullanıcı Adınız:");
        GridPane.setConstraints(kullaniciAdiLabel, 0, 4);
        TextField kullaniciAdiInput = new TextField();
        GridPane.setConstraints(kullaniciAdiInput, 1, 4);
        Label phone = new Label("Telefon Numaranız:");
        GridPane.setConstraints(phone, 0, 6);
        TextField phonetext = new TextField();
        GridPane.setConstraints(phonetext, 1, 6);
        Label passlbl = new Label("Şifreniz:");
        GridPane.setConstraints(passlbl, 0, 10);
        TextField passtext = new TextField();
        GridPane.setConstraints(passtext, 1, 10);
        Button loginButton = new Button("Kayıt Ol");
        GridPane.setConstraints(loginButton, 1, 14);
        Button loginButton2 = new Button("Geri Dön");
        GridPane.setConstraints(loginButton2, 0, 14);
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.getConnection();
                String sql="INSERT INTO user (kullaniciAdi, sifre, adi,soyadi, telefon) VALUES ('"+kullaniciAdiInput.getText()+"','"+passtext.getText()+"','"+nameInput.getText()+"','"+surtext.getText()+"','"+phonetext.getText()+"')";
                 try {
                     errorLabel.setText("");
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(sql);
                    System.out.println("Kayıt başarılı");
                } catch (SQLException e) {
                    errorLabel.setText(e.toString());
                }

            }
        });
        loginButton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logupStage.hide();
                LoginViewManager manager2 = new LoginViewManager();
                manager2.getMainStage().show();
            }} );
        logupPane.getChildren().addAll(nameLabel, nameInput, surlbl, surtext, loginButton, kullaniciAdiLabel, kullaniciAdiInput,phone,phonetext,passlbl,passtext,loginButton2);
    }
    public Stage getMainStage(){
        return logupStage;
    }
}
