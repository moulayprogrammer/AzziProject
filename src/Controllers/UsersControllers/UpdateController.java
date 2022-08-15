package Controllers.UsersControllers;

import BddPackage.UsersOperation;
import Models.Users;
import com.jfoenix.controls.JFXCheckBox;
import javafx.event.ActionEvent;
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

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    @FXML
    private Label invalidLoginCredentials;
    @FXML
    TextField tfUsername,tfPassword,tfPasswordConfirm;
    @FXML
    Button btnInsert;

    String errorMessage = "-fx-text-fill: RED;";
    private final UsersOperation operation = new UsersOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionInsert(ActionEvent event) {

        String username = tfUsername.getText().trim();
        String password = tfPassword.getText().trim();
        String passwordConfirm = tfPasswordConfirm.getText().trim();


        if (!username.isEmpty() && !password.isEmpty() && !passwordConfirm.isEmpty() ){

            if (password.equals(passwordConfirm)) {

                Users users = new Users(username, password);

                boolean ins = insert(users);
                if (ins) {
                    closeDialog(btnInsert);

                    try {

                        Stage primaryStage = new Stage();
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/MainView.fxml")));
                        primaryStage.setTitle("مزرعة الجنوب");
                        primaryStage.setScene(new Scene(root));
                        primaryStage.getIcons().add(new Image("Images/logo.png"));

                        primaryStage.setMaximized(true);
                        primaryStage.show();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    invalidLoginCredentials.setText("خطا غير معروف ");
                    invalidLoginCredentials.setStyle(errorMessage);
                }
            }else {
                invalidLoginCredentials.setText("كلمات السر غير متطابقة !");
                invalidLoginCredentials.setStyle(errorMessage);

            }
        }else {
            invalidLoginCredentials.setText("الرجاء ملأ جميع الخانات");
            invalidLoginCredentials.setStyle(errorMessage);
        }
    }

    private boolean insert(Users users) {
        boolean insert = false;
        try {
            insert = operation.insert(users);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }


    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
