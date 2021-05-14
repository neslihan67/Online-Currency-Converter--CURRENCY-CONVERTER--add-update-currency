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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class LoginViewManager {
    private static final int HEIGHT=500;
    private static final int WIDTH=500;
    private GridPane loginPane;
    private Scene loginScene;
    private Stage loginStage;


    public LoginViewManager(){
        loginPane = new GridPane();
        loginScene = new Scene(loginPane,WIDTH,HEIGHT);
        loginStage = new Stage();
        loginStage.setTitle("Login");
        loginStage.setScene(loginScene);
        loginPane.setPadding(new Insets(10, 10, 10, 10));
        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.web("#FF0000"));
        GridPane.setConstraints(errorLabel, 1, 0);
        Label nameLabel = new Label("Kullanıcı adı:");
        GridPane.setConstraints(nameLabel, 0, 1);
        TextField nameInput = new TextField("");
        GridPane.setConstraints(nameInput, 1, 1);
        Label passLabel = new Label("Şifre:");
        GridPane.setConstraints(passLabel, 0, 2);
        TextField passInput = new TextField();
        passInput.setPromptText("******");
        GridPane.setConstraints(passInput, 1, 2);
        Button loginButton = new Button("Giriş");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection=connectionClass.getConnection();
                String sql="SELECT * from user WHERE kullaniciAdi='"+nameInput.getText()+"' AND sifre='"+passInput.getText()+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    if (resultSet.next()){
                        loginStage.hide();
                        String isimSoyisim = resultSet.getString("adi")+" "+resultSet.getString("soyadi");
                        MainViewManager manager = new MainViewManager(nameInput.getText(),isimSoyisim);
                        manager.getMainStage().show();
                        errorLabel.setText("");
                    }else{
                        errorLabel.setText("Hatalı kullanıcı adı ya da şifre");
                    }

                } catch (SQLException e) {
                    errorLabel.setText(e.toString());
                }
            }
        });
        GridPane.setConstraints(loginButton, 1, 3);
        Button btn2 = new Button("Yeni Kayıt");
        GridPane.setConstraints(btn2, 2, 3);
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loginStage.hide();
                LogUpViewManager manager = new LogUpViewManager();
                manager.getMainStage().show();

            }
        });
        loginPane.getChildren().addAll(nameLabel, nameInput, passLabel, passInput, loginButton,btn2, errorLabel);

    }
    public Stage getMainStage(){
        return loginStage;
    }
}
