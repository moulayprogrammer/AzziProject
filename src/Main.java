import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        String FileFolder = System.getenv("APPDATA") + File.separator + "Production" + File.separator + "data.ml" ;
        File file = new File(FileFolder);

        Parent root = null;

        if (file.exists()) {
            try {
                Scanner myReader = new Scanner(file);
                if (myReader.hasNextLine()) {

                    // get serial number motherBord
                    String command = "wmic baseboard get serialnumber";
                    Process SerialNumberProcess = Runtime.getRuntime().exec(command);
                    InputStreamReader ISR = new InputStreamReader(SerialNumberProcess.getInputStream());
                    BufferedReader br = new BufferedReader(ISR);
                    String serialNumber = br.readLine().trim();
                    SerialNumberProcess.waitFor();
                    br.close();

                    String code = "moulay + achoura = lalla soltana " + serialNumber;

                    String data = myReader.nextLine();

                    System.out.println(data);

                    if (Objects.equals(data, String.valueOf(code.hashCode()))) root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Views/LoginView.fxml")));
                    else root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Views/SerialView.fxml")));
                }
                myReader.close();
            } catch (Exception e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        }else {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Views/SerialView.fxml")));
        }

        primaryStage.setTitle("مزرعة الجنوب");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("Images/logo.png"));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
