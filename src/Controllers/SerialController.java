package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SerialController implements Initializable {

    String errorMessage = String.format("-fx-text-fill: RED;");
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");

    // Import the application's controls
    @FXML
    private Label invalidLoginCredentials;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField loginPasswordPasswordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // Creation of methods which are activated on events in the forms
    @FXML
    protected void onCancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onLoginButtonClick() {
        if ( loginPasswordPasswordField.getText().isEmpty()) {
            invalidLoginCredentials.setText("الرجاء ملأ جميع الخانات");
            invalidLoginCredentials.setStyle(errorMessage);

            if (loginPasswordPasswordField.getText().isEmpty()) {
                loginPasswordPasswordField.setStyle(errorStyle);
            }
        } else if (loginPasswordPasswordField.getText().equals("MOULAYzine<3ACHORAholwa")){

            try {
                String FileFolder = System.getenv("APPDATA") + File.separator + "TSP" ;

                File directory = new File(FileFolder);

                if (!directory.exists()) {
                    FileUtils.forceMkdir(directory);
                }

                File file = new File(directory.getAbsolutePath() + File.separator + "data.ml");

                if (file.createNewFile()){

                    // get serial number motherBord
                    String command = "wmic baseboard get serialnumber";
                    Process SerialNumberProcess = Runtime.getRuntime().exec(command);
                    InputStreamReader ISR = new InputStreamReader(SerialNumberProcess.getInputStream());
                    BufferedReader br = new BufferedReader(ISR);
                    String serialNumber = "";
                    for (String line = br.readLine(); line != null; line = br.readLine()) {
                        if (line.length() < 1 || line.startsWith("SerialNumber")) {
                            continue;
                        }
                        serialNumber = line;
                        break;
                    }
                    SerialNumberProcess.waitFor();
                    br.close();

                    // write the serial and the code to the file
                    FileWriter writer = new FileWriter(file);
                    String code = "moulay + achoura = lalla soltana <3 " + serialNumber;
                    writer.write( String.valueOf(code.hashCode()));
                    writer.close();


                    try {
                        ((Stage)this.cancelButton.getScene().getWindow()).close();

                        Stage primaryStage = new Stage();
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/UsersViews/AddView.fxml")));
                        primaryStage.setTitle("مجمع مزرعة الجنوب");
                        primaryStage.setScene(new Scene(root));
                        primaryStage.getIcons().add(new Image("Images/logo.png"));
                        primaryStage.setResizable(false);
                        primaryStage.show();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            invalidLoginCredentials.setText("رقم التسجيل خطأ ");
            invalidLoginCredentials.setStyle(errorMessage);
        }
    }
}
