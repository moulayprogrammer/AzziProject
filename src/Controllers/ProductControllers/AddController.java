package Controllers.ProductControllers;

import BddPackage.*;
import Models.Medication;
import Models.RawMaterial;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddController implements Initializable {


    @FXML
    TextField tfName,tfReference,tfLimiteQte;
    @FXML
    TextField tfMedId1,tfMedId2,tfMedId3,tfMedId4,tfMedId5,tfMedId6,tfMedId7;
    @FXML
    TextField tfMedName1,tfMedName2,tfMedName3,tfMedName4,tfMedName5,tfMedName6,tfMedName7;
    @FXML
    TextField tfMedRef1,tfMedRef2,tfMedRef3,tfMedRef4,tfMedRef5,tfMedRef6,tfMedRef7;
    @FXML
    TextField tfMedQte1,tfMedQte2,tfMedQte3,tfMedQte4,tfMedQte5,tfMedQte6,tfMedQte7;
    @FXML
    TextField tfRawName1,tfRawName2,tfRawName3,tfRawName4,tfRawName5,tfRawName6,tfRawName7;
    @FXML
    TextField tfRawRef1,tfRawRef2,tfRawRef3,tfRawRef4,tfRawRef5,tfRawRef6,tfRawRef7;
    @FXML
    TextField tfRawQte1,tfRawQte2,tfRawQte3,tfRawQte4,tfRawQte5,tfRawQte6,tfRawQte7;
    @FXML
    Button btnInsert;

    private final ProductOperation operation = new ProductOperation();
    private final RawMaterialOperation materialOperation = new RawMaterialOperation();
    private final MedicationOperation medicationOperation = new MedicationOperation();
    private final ComponentRawMaterialOperation componentMaterialOperation = new ComponentRawMaterialOperation();
    private final ComponentMedicationOperation componentMedicationOperation = new ComponentMedicationOperation();

    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final List<Integer> medicationList = new ArrayList<>();
    private final List<Integer> rawMaterialList = new ArrayList<>();
    private final Medication medication1 = new Medication();
    private final Medication medication2 = new Medication();
    private final Medication medication3 = new Medication();
    private final Medication medication4 = new Medication();
    private final Medication medication5 = new Medication();
    private final Medication medication6 = new Medication();
    private final Medication medication7 = new Medication();
    private final RawMaterial rawMaterial1 = new RawMaterial();
    private final RawMaterial rawMaterial2 = new RawMaterial();
    private final RawMaterial rawMaterial3 = new RawMaterial();
    private final RawMaterial rawMaterial4 = new RawMaterial();
    private final RawMaterial rawMaterial5 = new RawMaterial();
    private final RawMaterial rawMaterial6 = new RawMaterial();
    private final RawMaterial rawMaterial7 = new RawMaterial();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void SelectMed1(){
        SelectMedication(medication1, tfMedName1, tfMedRef1 ,tfMedId1);
    }

    @FXML
    private void SelectMed2(){
        SelectMedication(medication2, tfMedName2, tfMedRef2,tfMedId2);
    }

    @FXML
    private void SelectMed3(){
        SelectMedication(medication3, tfMedName3, tfMedRef3 ,tfMedId3);
    }

    @FXML
    private void SelectMed4(){
        SelectMedication(medication4, tfMedName4, tfMedRef4 ,tfMedId4);
    }

    @FXML
    private void SelectMed5(){
        SelectMedication(medication5, tfMedName5, tfMedRef5 ,tfMedId5);
    }

    @FXML
    private void SelectMed6(){
        SelectMedication(medication6, tfMedName6, tfMedRef6 ,tfMedId6);
    }

    @FXML
    private void SelectMed7(){
        SelectMedication(medication7, tfMedName7, tfMedRef7 ,tfMedId7);
    }

    private void SelectMedication(Medication medication, TextField tfMedName, TextField tfMedRef , TextField tfMedId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ProductViews/SelectMedicationView.fxml"));
            DialogPane temp = loader.load();
            SelectMedicationController controller = loader.getController();
            controller.Init(medication);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.showAndWait();

            if (medication.getName() != null){
                if (!medicationList.contains(medication.getId())) {
                    medicationList.add(medication.getId());
                    tfMedName.setText(medication.getName());
                    tfMedRef.setText(medication.getReference());
                    tfMedId.setText(medication.getId() + "");
                }else {
                    Alert alertWarning = new Alert(Alert.AlertType.INFORMATION);
                    alertWarning.setHeaderText("إعلام ");
                    alertWarning.setContentText("الدواء موجود بالفعل في القائمة");
                    Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("موافق");
                    alertWarning.showAndWait();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void SelectRaw1(){
        SelectRawMaterial(rawMaterial1, tfRawName1, tfRawRef1);
    }

    @FXML
    private void SelectRaw2(){
        SelectRawMaterial(rawMaterial2, tfRawName2, tfRawRef2);
    }

    @FXML
    private void SelectRaw3(){
        SelectRawMaterial(rawMaterial3, tfRawName3, tfRawRef3);
    }

    @FXML
    private void SelectRaw4(){
        SelectRawMaterial(rawMaterial4, tfRawName4, tfRawRef4);
    }

    @FXML
    private void SelectRaw5(){
        SelectRawMaterial(rawMaterial5, tfRawName5, tfRawRef5);
    }

    @FXML
    private void SelectRaw6(){
        SelectRawMaterial(rawMaterial6, tfRawName6, tfRawRef6);
    }

    @FXML
    private void SelectRaw7(){
        SelectRawMaterial(rawMaterial7, tfRawName7, tfRawRef7);
    }

    private void SelectRawMaterial(RawMaterial rawMaterial, TextField tfRawName, TextField tfRawRef) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ProductViews/SelectRawMaterialView.fxml"));
            DialogPane temp = loader.load();
            SelectRawMaterialController controller = loader.getController();
            controller.Init(rawMaterial);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.showAndWait();

            if (rawMaterial.getName() != null){
                if (!rawMaterialList.contains(rawMaterial.getId())) {
                    rawMaterialList.add(rawMaterial.getId());
                    tfRawName.setText(rawMaterial.getName());
                    tfRawRef.setText(rawMaterial.getReference());
                }else {
                    Alert alertWarning = new Alert(Alert.AlertType.INFORMATION);
                    alertWarning.setHeaderText("إعلام ");
                    alertWarning.setContentText("المادة موجودة بالفعل في القائمة");
                    Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("موافق");
                    alertWarning.showAndWait();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionInsert(ActionEvent event) {

        String name = tfName.getText().trim();
        String reference = tfReference.getText().trim();
        String limiteQte = tfLimiteQte.getText().trim();

        if (!name.isEmpty() && !reference.isEmpty() && !limiteQte.isEmpty()){

            Medication medication = new Medication();
            medication.setName(name);
            medication.setReference(reference);
            medication.setLimitQte(Integer.parseInt(limiteQte));

            boolean ins = insert(medication);
            if (ins) closeDialog(btnInsert);
            else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("خطأ غير معروف");
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء ملأ جميع الحقول");
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    private boolean insert(Medication medication) {
        boolean insert = false;
        try {
//            insert = operation.insert(medication);
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
