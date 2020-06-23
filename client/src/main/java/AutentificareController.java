import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AutentificareController {

    private IServices server;

    private Voluntar curentVoluntar;

    private TController mainController;

    @FXML
    private TextField idTextField;
    @FXML
    private TextField numeTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField parolaTextField;

    Parent mainParent;

    public AutentificareController() {
    }

    public AutentificareController(IServices server) {
        this.server = server;
    }

    public void setParent(Parent p){
        mainParent=p;
    }

    public void setServer(IServices server)
    {
        this.server=server;
    }

    public void setMainController(TController mainController)
    {
        this.mainController = mainController;
    }
    @FXML
    void handleLogIn(ActionEvent event) {
        int id = Integer.parseInt(idTextField.getText());

        String parola = parolaTextField.getText();


        curentVoluntar = new Voluntar(id, parola);


        try{
            server.login(curentVoluntar, mainController);

            Stage stage=new Stage();
            stage.setTitle("Window for " +curentVoluntar.getId());
            stage.setScene(new Scene(mainParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    //mainController.handleLogOut(event);
                    System.exit(0);
                }
            });

            stage.show();
            mainController.setVoluntar(curentVoluntar);
            ((Node)(event.getSource())).getScene().getWindow().hide();

        }   catch (MyException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("TELEDON");
            alert.setHeaderText("Autentificarea a esuat");
            alert.setContentText("Parola sau id-ul sunt gresite");
            alert.showAndWait();
        }
    }



}
