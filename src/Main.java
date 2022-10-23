import BddPackage.UsersOperation;
import Models.Users;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        String FileFolder = System.getenv("APPDATA") + File.separator + "TSP" + File.separator + "data.ml" ;
        File file = new File(FileFolder);

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/SerialView.fxml")));


        if (file.exists()) {
            try {
                Scanner myReader = new Scanner(file);
                if (myReader.hasNextLine()) {

                    // get serial number motherBord
                    String serial = getSerialNumber("C");

                    String code = "moulay + achoura = lalla soltana <3 " + serial;

                    String data = myReader.nextLine();
                    

                    if (Objects.equals(data, String.valueOf(code.hashCode()))){
                        ArrayList<Users> users = new UsersOperation().getAll();
                        if (users.size() != 0)  root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/LoginView.fxml")));
                        else root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/UsersViews/AddView.fxml")));
                    }
                    else root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/SerialView.fxml")));
                }
                myReader.close();
            } catch (Exception e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        }else {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/SerialView.fxml")));
        }
        primaryStage.setTitle("مجمع مزرعة الجنوب");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("Images/logo.png"));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public String getSerialNumber(String drive) {
        StringBuilder result = new StringBuilder();
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    + "Set colDrives = objFSO.Drives\n"
                    + "Set objDrive = colDrives.item(\"" + drive + "\")\n"
                    + "Wscript.Echo objDrive.SerialNumber";  // see note
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input =
                    new BufferedReader
                            (new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result.append(line);
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString().trim();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
