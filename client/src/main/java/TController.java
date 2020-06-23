
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TController extends UnicastRemoteObject implements IObserver, Serializable {

    private ObservableList<CazCaritabil> modelCazuri = FXCollections.observableArrayList();
    private ObservableList<Donator> modelDonatori = FXCollections.observableArrayList();
    private IServices server;
    private Voluntar curentVoluntar;


    @FXML
    private TableView<CazCaritabil> cazuriTableView;

    @FXML
    private TableColumn<CazCaritabil, String> denumireColumn;

    @FXML
    private TableColumn<CazCaritabil, Double> sumaColumn;

    @FXML
    private TableView<Donator> donatoriTableView;

    @FXML
    private TableColumn<Donator, String> numeDonatorColumn;

    @FXML
    private TableColumn<Donator, String> adresaDonatorColumn;

    @FXML
    private TableColumn<Donator, Long> telefonDonatorColumn;

    @FXML
    private TextField numeDonatorTextField;

    @FXML
    private TextField adresaDonatorTextField;

    @FXML
    private TextField telefonDonatorTextField;

    @FXML
    private TextField sumaDonatieTextField;

    public TController() throws RemoteException {
        //this.server = server;
        System.out.println("Constructor TController");

    }


    public TController(IServices server) throws RemoteException {

        this.server = server;
        System.out.println("constructor TController cu server param");

    }

    public void setServer(IServices s) {
        server = s;

    }

    public void setVoluntar(Voluntar curentVoluntar)
    {
        this.curentVoluntar = curentVoluntar;
        initModel();
        initialize();
    }

    @FXML
    public void initialize()
    {
        denumireColumn.setCellValueFactory(new PropertyValueFactory<CazCaritabil, String>("Denumire"));
        sumaColumn.setCellValueFactory(new PropertyValueFactory<CazCaritabil, Double>("SumaTotala"));
        cazuriTableView.setItems(modelCazuri);

        numeDonatorColumn.setCellValueFactory(new PropertyValueFactory<Donator, String>("Nume"));
        adresaDonatorColumn.setCellValueFactory(new PropertyValueFactory<Donator, String>("Adresa"));
        telefonDonatorColumn.setCellValueFactory(new PropertyValueFactory<Donator, Long>("NrTelefon"));
        donatoriTableView.setItems(modelDonatori);

    }


    public Integer generateDonatorId()
    {
        Donator[] donatori = null;
        try {
            donatori = server.getDonatori();
        } catch (MyException e) {
            e.printStackTrace();
        }

        List<Donator> donL = Arrays.asList(donatori);
        int max = donL.stream()
                .mapToInt(Donator::getId)
                .max()
                .orElse(0);
        return max +1 ;
    }
    public Integer generateDonatieId()
    {
        Donatie[] donatii = null;
        try {
            donatii = server.getDonatii();
        } catch (MyException e) {
            e.printStackTrace();
        }

        List<Donatie> donL = Arrays.asList(donatii);
        int max = donL.stream()
                .mapToInt(Donatie::getId)
                .max()
                .orElse(0);
        return max +1 ;
    }


    private void initModel() {

        CazCaritabil[] cazuri = null;
        try {
            cazuri = server.getCazuri();
        } catch (MyException e) {
            e.printStackTrace();
        }
       // List<CazCaritabil> cazuriList = StreamSupport.stream(cazuri.spliterator(), false)
         //       .collect(Collectors.toList());
        modelCazuri.setAll(cazuri);


        Donator[] donatori = null;
        try {
            donatori = server.getDonatori();
        } catch (MyException e) {
            e.printStackTrace();
        }

        modelDonatori.setAll(donatori);


    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    @FXML
    void handleSelection(MouseEvent event) {
       Donator donator = donatoriTableView.getSelectionModel().getSelectedItem();

        if (donator != null) {
            numeDonatorTextField.setText(donator.getNume());
            adresaDonatorTextField.setText(donator.getAdresa());
            telefonDonatorTextField.setText(donator.getNrTelefon().toString());
        }

    }

    @FXML
    void handleNume(KeyEvent event) {
       if (!numeDonatorTextField.getText().equals("")) {
            Predicate<Donator> filtrareNume = d -> d.getNume().startsWith(numeDonatorTextField.getText());
           try {
               modelDonatori.setAll(Arrays.asList(server.getDonatori()).stream()
                       .filter(filtrareNume)
                       .collect(Collectors.toList()));
           } catch (MyException e) {
               e.printStackTrace();
           }
        }
       else {
           try {
               modelDonatori.setAll(server.getDonatori());
           } catch (MyException e) {
               e.printStackTrace();
           }
       }
    }

    @FXML
    void handleAdaugaDonatie(ActionEvent event) throws MyException {

       CazCaritabil caz = cazuriTableView.getSelectionModel().getSelectedItem();
        Donator d = null;
        d = donatoriTableView.getSelectionModel().getSelectedItem();
        if (caz == null) {
            MesajAlerta.afiseazaMesajEroare(null, "Te rog sa selectezi un caz!");
            return;
        }
        if (!isNumeric(telefonDonatorTextField.getText()) || !isNumeric(sumaDonatieTextField.getText()))
        {
            MesajAlerta.afiseazaMesajEroare(null,"Nr de Telefon/Suma donata trebuie sa fie un numar!");
            return;
        }
        else if (d == null)
        {
            d = new Donator(generateDonatorId(),numeDonatorTextField.getText(),adresaDonatorTextField.getText(),Long.parseLong(telefonDonatorTextField.getText()));

            try{
                server.addDonator(d);
            }catch (ValidationException | MyException ex)
            {
                MesajAlerta.afiseazaMesajEroare(null,ex.getMessage());
            }
        }
       if (caz != null)
        {
               Donatie donatie = new Donatie(generateDonatieId(),d.getId(),caz.getId(),Double.parseDouble(sumaDonatieTextField.getText()));
               server.addDonatie(donatie);
               Double s = caz.getSumaTotala() + Double.parseDouble(sumaDonatieTextField.getText());
               CazCaritabil caz2 = new CazCaritabil(caz.getId(),caz.getDenumire(),s);
            try {
                server.updateCaz(caz2);
            } catch (MyException e) {
                e.printStackTrace();
            }
            MesajAlerta.afiseazaMesaj(null, Alert.AlertType.INFORMATION,"Succes","Donatia a fost inregistrata cu succes! Multumim!");

        }
        else
            MesajAlerta.afiseazaMesajEroare(null,"Te rog sa selectezi un caz!");
        initModel();

    }


    @FXML
    void handleLogOut(ActionEvent event) {
        try {
            server.logout(curentVoluntar,this);
        } catch (MyException e) {
            e.printStackTrace();
        }
        numeDonatorTextField.getScene().getWindow().hide();
        MesajAlerta.afiseazaMesaj(null, Alert.AlertType.INFORMATION,"logged out","you have been logged out");
    }


    @Override
    public void donatorSaved(Donator donator) {

        donatoriTableView.getItems().add(donator);
        System.out.println("Donator adaugat " + donator.getNume());


    }

    @Override
    public void cazUpdated(CazCaritabil caz) {

        for (int idx = 0; idx < cazuriTableView.getItems().size(); idx++) {
            CazCaritabil caz0 = cazuriTableView.getItems().get(idx);
            if (caz0.getId().equals(caz.getId())) {
                cazuriTableView.getItems().set(idx, caz);
                return;
            }
        }
        System.out.println("caz modificat " + caz.toString());
    }
}
