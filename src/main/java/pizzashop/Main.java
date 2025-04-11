package pizzashop;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pizzashop.controller.MainGUIController;
import pizzashop.gui.KitchenGUI;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;

import java.io.*;
import java.text.Format;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Formatter;
import java.util.Optional;

public class Main extends Application {


    public void writeInFiles(PizzaService service){
        File file_raport = new File("data/reports/raport_" + DateTimeFormatter.ofPattern("dd-MM-YYYY").format(LocalDate.now()) + ".txt");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file_raport))) {
            int i = 0;

            while (i < 1) {
                if (service.getTotalAmount((PaymentType.Cash)) > 0) {
                    bufferedWriter.write(String.valueOf(service.getTotalAmount(PaymentType.Cash)) + ",Cash");
                    bufferedWriter.newLine();
                } else {
                    bufferedWriter.write("cash intake does not exist today!");
                    bufferedWriter.newLine();
                }

                if (service.getTotalAmount((PaymentType.Card)) > 0) {
                    bufferedWriter.write(String.valueOf(service.getTotalAmount(PaymentType.Card)) + ",Card");
                    bufferedWriter.newLine();
                } else {
                    bufferedWriter.write("card intake does not exist today!");
                    bufferedWriter.newLine();
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        MenuRepository repoMenu = new MenuRepository();
        PaymentRepository payRepo = new PaymentRepository();
        PizzaService service = new PizzaService(repoMenu, payRepo);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainFXML.fxml"));
        //VBox box = loader.load();
        Parent box = loader.load();
        MainGUIController ctrl = loader.getController();
        ctrl.setService(service);
        primaryStage.setTitle("PizzeriaX");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(false);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to exit the Main window?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = exitAlert.showAndWait();
                if (result.get() == ButtonType.YES) {

                    writeInFiles(service);

                } else {
                    event.consume();
                }
                primaryStage.close();
            }
        });
        primaryStage.setScene(new Scene(box));
        primaryStage.show();
        KitchenGUI kitchenGUI = new KitchenGUI();
        kitchenGUI.KitchenGUI();
    }

    public static void main(String[] args) {
        launch(args);
    }
}