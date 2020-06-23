import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartSpringClient extends Application{

    public static void main(String[] args) throws Exception {
        launch(args);}

    @Override
    public void start(Stage stage) throws Exception {

        System.out.println("In start");

        System.out.println("In start");
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
        IServices server=(IServices)factory.getBean("teledonService");
        System.out.println("Obtained a reference to remote server");

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("autentificareView.fxml"));
        Parent root=loader.load();


        AutentificareController ctrl =
                loader.getController();
        ctrl.setServer(server);




        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("tView.fxml"));
        Parent croot=cloader.load();


        TController mainCtrl =
                cloader.getController();
        mainCtrl.setServer(server);

        ctrl.setMainController(mainCtrl);
        ctrl.setParent(croot);

        stage.setTitle("Login");
        stage.setScene(new Scene(root, 300, 230));
        stage.show();
    }




}