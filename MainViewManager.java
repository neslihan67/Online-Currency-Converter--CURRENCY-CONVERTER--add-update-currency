package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class MainViewManager {
    private static final int HEIGHT=500;
    private static final int WIDTH=900;
    private GridPane mainPane;
    private Scene mainScene;
    private Stage mainStage;


    public MainViewManager(String kullaniciAdi, String isimSoyisim){
        mainPane = new GridPane();
        mainScene = new Scene(mainPane,WIDTH,HEIGHT);
        mainStage = new Stage();
        mainStage.setTitle("Main");
        mainStage.setScene(mainScene);
        mainPane.setPadding(new Insets(10, 10, 10, 10));
        Label isimLabel = new Label(isimSoyisim);
        isimLabel.setTextFill(Color.web("#000000"));
        GridPane.setConstraints(isimLabel, 0, 0);
        Label kadiLabel = new Label(kullaniciAdi);
        kadiLabel.setTextFill(Color.web("#000000"));
        GridPane.setConstraints(kadiLabel, 0, 1);

        Label moneyLabel= new Label("Para Miktarı");
        GridPane.setConstraints(moneyLabel, 1, 0);
        TextField moneytf = new TextField();
        GridPane.setConstraints(moneytf, 1, 1);

        Label turLabel= new Label("Para Türü");
        GridPane.setConstraints(turLabel, 2, 0);
        ArrayList<String> arraydeneme= turleriCek();
        ComboBox turCb = new ComboBox(FXCollections.observableArrayList(arraydeneme));
        turCb.getSelectionModel().select(0);
        GridPane.setConstraints(turCb, 2, 1);

        Label errorLbl = new Label("");
        errorLbl.setTextFill(Color.RED);
        GridPane.setConstraints(errorLbl, 1, 3);

        ListView listView = new ListView(sahipOlunanParayiCek(kullaniciAdi));
        listView.setPrefSize(200, 150);
        GridPane.setConstraints(listView, 0, 7);

        Button ekleBt= new Button("Hesaba Ekle");
        ekleBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorLbl.setText("");
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection=connectionClass.getConnection();
                String sql="SELECT * from deposit WHERE kod='"+turCb.getSelectionModel().getSelectedItem()+"' AND kullaniciAdi='"+kullaniciAdi+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    if (resultSet.next()){
                        sql="UPDATE deposit SET tutar='"+(resultSet.getFloat("tutar")+Float.parseFloat(moneytf.getText()))+"' WHERE kullaniciAdi='"+kullaniciAdi+"' and kod='"+turCb.getSelectionModel().getSelectedItem()+"' ";
                    }else{
                        sql="INSERT INTO deposit (kod, tutar, kullaniciAdi) VALUES ('"+turCb.getSelectionModel().getSelectedItem()+"','"+moneytf.getText()+"','"+kullaniciAdi+"')";
                    }
                    statement.executeUpdate(sql);
                    listView.setItems(sahipOlunanParayiCek(kullaniciAdi));
                } catch (SQLException e) {
                    System.out.println(e);
                }


            }
        });
        GridPane.setConstraints(ekleBt, 1, 2);
        Button cikarBt= new Button("Hesaptan Çıkar");
        cikarBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorLbl.setText("");
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection=connectionClass.getConnection();
                String sql="SELECT * from deposit WHERE kod='"+turCb.getSelectionModel().getSelectedItem()+"' AND kullaniciAdi='"+kullaniciAdi+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    if (resultSet.next()&&resultSet.getFloat("tutar")>=Float.parseFloat(moneytf.getText())){
                        sql="UPDATE deposit SET tutar='"+(resultSet.getFloat("tutar")-Float.parseFloat(moneytf.getText()))+"' WHERE kullaniciAdi='"+kullaniciAdi+"' and kod='"+turCb.getSelectionModel().getSelectedItem()+"' ";
                        statement.executeUpdate(sql);
                    }else{
                        errorLbl.setText("Fakirsiniz");
                    }
                    listView.setItems(sahipOlunanParayiCek(kullaniciAdi));
                } catch (SQLException e) {
                    System.out.println(e);
                }

            }
        });
        GridPane.setConstraints(cikarBt, 2, 2);


        Label convertMiktarlbl= new Label("Para Miktarı");
        GridPane.setConstraints(convertMiktarlbl, 1, 4);
        TextField convertMoneytf = new TextField();
        GridPane.setConstraints(convertMoneytf, 1, 5);

        Label convertTurLbl= new Label("Para Türü");
        GridPane.setConstraints(convertTurLbl, 2,4 );
        ComboBox convertMoneyCB = new ComboBox(FXCollections.observableArrayList(arraydeneme));
        convertMoneyCB.getSelectionModel().select(0);
        GridPane.setConstraints(convertMoneyCB, 2, 5);

        ListView donusturView = new ListView();
        listView.setPrefSize(200, 150);
        GridPane.setConstraints(donusturView, 2, 7);

        Button donusturBt= new Button("Dönüştür");
        donusturBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList result = FXCollections.observableArrayList();
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection=connectionClass.getConnection();
                String sql="SELECT * from currency where kod='"+convertMoneyCB.getSelectionModel().getSelectedItem()+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()){
                        float tl=(resultSet.getFloat("alisDeger")*Float.parseFloat(convertMoneytf.getText()));
                        sql="SELECT * from currency";
                        resultSet = statement.executeQuery(sql);
                        ObservableList ol = FXCollections.observableArrayList();
                        while (resultSet.next()){
                            float tutar = tl/resultSet.getFloat("alisDeger");
                            ol.add(resultSet.getString("Kod")+": "+tutar);
                        }
                        donusturView.setItems(ol);
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        });
        GridPane.setConstraints(donusturBt, 1, 6);

        Label degerL= new Label("Para Miktarı");
        GridPane.setConstraints(degerL, 0, 8);
        Label ilkPL= new Label("İlk Para Türü");
        GridPane.setConstraints(ilkPL, 1, 8);
        Label ikinciPL= new Label("İkinci Para Türü");
        GridPane.setConstraints(ikinciPL, 2, 8);
        Label errorLb= new Label("");
        errorLb.setTextFill(Color.RED);
        GridPane.setConstraints(errorLb, 0, 10);

        TextField degerTF = new TextField();
        GridPane.setConstraints(degerTF, 0, 9);

        ComboBox ilkCB = new ComboBox(FXCollections.observableArrayList(arraydeneme));
        ilkCB.getSelectionModel().select(0);
        GridPane.setConstraints(ilkCB, 1, 9);
        ComboBox ikinciCB = new ComboBox(FXCollections.observableArrayList(arraydeneme));
        ikinciCB.getSelectionModel().select(0);
        GridPane.setConstraints(ikinciCB, 2, 9);

        Button cevirBt= new Button("Dönüştür");
        cevirBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection=connectionClass.getConnection();
                String sql="SELECT * from deposit WHERE kod='"+ilkCB.getSelectionModel().getSelectedItem()+"' AND kullaniciAdi='"+kullaniciAdi+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    if(resultSet.next()){
                    float ilkPara=resultSet.getFloat("tutar");
                    if (ilkPara>=Float.parseFloat(degerTF.getText())){
                        Float eskiPara= eskiParayiCek(kullaniciAdi, ikinciCB.getSelectionModel().getSelectedItem().toString());
                        sql="UPDATE deposit SET tutar='"+(eskiPara +Float.parseFloat(degerTF.getText())*degerCek(ilkCB.getSelectionModel().getSelectedItem().toString())/degerCek(ikinciCB.getSelectionModel().getSelectedItem().toString()))+"' WHERE kullaniciAdi='"+kullaniciAdi+"' and kod='"+ikinciCB.getSelectionModel().getSelectedItem()+"' ";
                        statement.executeUpdate(sql);

                        sql="UPDATE deposit SET tutar='"+(ilkPara-Float.parseFloat(degerTF.getText()))+"' WHERE kullaniciAdi='"+kullaniciAdi+"' and kod='"+ilkCB.getSelectionModel().getSelectedItem()+"' ";
                        statement.executeUpdate(sql);

                        listView.setItems(sahipOlunanParayiCek(kullaniciAdi));
                    }else{
                        errorLb.setText("Fakirsiniz");
                    }
                    }else{
                        errorLb.setText("Fakirsiniz");
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        });
        GridPane.setConstraints(cevirBt, 1, 10);

        mainPane.getChildren().addAll(isimLabel, kadiLabel, moneyLabel, moneytf, turLabel, turCb, ekleBt, donusturView, donusturBt);
        mainPane.getChildren().addAll(cikarBt, listView, errorLbl,convertMiktarlbl,convertMoneytf,convertTurLbl,convertMoneyCB);
        mainPane.getChildren().addAll(degerL, ilkPL, ikinciPL, ilkCB, ikinciCB, degerTF, cevirBt, errorLb);


    }
    public Stage getMainStage(){
        return mainStage;
    }
    private float degerCek(String kod){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        String sql="SELECT * from currency WHERE kod='"+kod+"'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                return  resultSet.getFloat("alisDeger");
            }else{
                return -1;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return -1;
        }
    }
    private float eskiParayiCek(String kadi, String kod){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        String sql="SELECT * from deposit WHERE kod='"+kod+"' and kullaniciAdi='"+kadi+"'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                return  resultSet.getFloat("tutar");
            }else{
                return -1;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return -1;
        }
    }
    private ArrayList<String> turleriCek(){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        String sql="SELECT * from currency";
        ArrayList<String> result = new ArrayList<String>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                resultSet.getString("Kod");
                result.add(resultSet.getString("Kod"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }
    private ObservableList sahipOlunanParayiCek(String kadi){
        ObservableList result = FXCollections.observableArrayList();
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        String sql="SELECT * from deposit where kullaniciAdi='"+kadi+"'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                result.add(resultSet.getString("Kod")+": "+resultSet.getString("Tutar"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }
}
