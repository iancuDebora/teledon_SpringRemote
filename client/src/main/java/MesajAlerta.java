import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MesajAlerta {
    static void afiseazaMesaj(Stage proprietar, Alert.AlertType tip, String antet, String continut)
    {
        Alert mesaj=new Alert(tip);
        mesaj.setHeaderText(antet);
        mesaj.setContentText(continut);
        mesaj.initOwner(proprietar);
        mesaj.showAndWait();
    }

    public static void afiseazaMesajEroare(Stage proprietar, String continut)
    {
        Alert mesaj=new Alert(Alert.AlertType.ERROR);
        mesaj.initOwner(proprietar);
        mesaj.setTitle("Mesaj eroare");
        mesaj.setContentText(continut);
        mesaj.showAndWait();
    }
}
