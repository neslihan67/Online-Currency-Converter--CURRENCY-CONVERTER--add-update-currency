package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //MainViewManager manager = new MainViewManager("brk","Burak Cenan");
        LoginViewManager manager = new LoginViewManager();
        downloadData();
        primaryStage= manager.getMainStage();
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void downloadData(){
        String[] moneys={"US_DOLLAR","AUSTRALIAN_DOLLAR","DANISH_KRONE","EURO","POUND_STERLING","SWISS_FRANK","SWEDISH_KRONA",
                "CANADIAN_DOLLAR","KUWAITI_DINAR","NORWEGIAN_KRONE","SAUDI_RIYAL","JAPANESE_YEN","NEW_LEU","RUSSIAN_ROUBLE","IRANIAN_RIAL",
                "CHINESE_RENMINBI","PAKISTANI_RUPEE","QATARI_RIAL"};
        for(int i=0;i<moneys.length;i++){
            CurrencyFactory currencyAPI = new CurrencyFactory(Moneys.valueOf(moneys[i]));
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();
            String sql="INSERT INTO currency (kod, alisDeger, satisDeger ) VALUES ('"+moneys[i]+"','"+currencyAPI.getCurrency().getBuyingPrice()+"','"+currencyAPI.getCurrency().getSellingPrice()+"')";
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLIntegrityConstraintViolationException e) {
                try {
                    sql = "update currency set alisDeger='" + currencyAPI.getCurrency().getBuyingPrice() + "', satisDeger='" + currencyAPI.getCurrency().getSellingPrice() + "' where kod='" + moneys[i] + "'";
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(sql);
                }catch (Exception ex){
                    System.out.println(ex);
                }
            }catch (SQLException e){
                System.out.println(e);
            }
        }
    }
}
