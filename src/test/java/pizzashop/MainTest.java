package pizzashop;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import javafx.scene.control.ButtonType;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    @TempDir
    Path tempDir;

    private final String REPORT_PATH = "data/reports/raport_" + DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now()) + ".txt";

    @AfterEach
    void cleanupReportFile() {
        File file = new File(REPORT_PATH);
        if (file.exists()) {
            file.setWritable(true); // just in case
            file.delete();
        }
    }

    private String readFile(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }

    void make_payments(double cash, double card){



    }
    @Test
    void test_CashAndCardExist() throws IOException {
        File report = new File("./data/reports/raport_"+"11-04-2025.txt");
        report.setWritable(true);

        MenuRepository menuRepository = new MenuRepository();
        PaymentRepository paymentRepository = new PaymentRepository();
        PizzaService service = new PizzaService(menuRepository, paymentRepository);
        service.addPayment(1, PaymentType.Cash,100.0);
        service.addPayment(1, PaymentType.Card,200.0);
        Main main = new Main();
        main.writeInFiles(service);
        String content = readFile(report);
        assertTrue(content.contains("100.0,Cash"));
        assertTrue(content.contains("200.0,Card"));


    }

    @Test
    void test_OnlyCashExists() throws IOException {
        File report = new File("./data/reports/raport_"+"11-04-2025.txt");
        report.setWritable(true);

        MenuRepository menuRepository = new MenuRepository();
        PaymentRepository paymentRepository = new PaymentRepository();
        PizzaService service = new PizzaService(menuRepository, paymentRepository);
        service.addPayment(1, PaymentType.Cash,100.0);
        service.addPayment(1, PaymentType.Card,0.0);
        Main main = new Main();
        main.writeInFiles(service);
        String content = readFile(report);
        assertTrue(content.contains("100.0,Cash"));
        assertTrue(content.contains("card intake does not exist today!"));
    }

    @Test
    void test_OnlyCardExists() throws IOException {
        File report = new File("./data/reports/raport_"+"11-04-2025.txt");
        report.setWritable(true);

        MenuRepository menuRepository = new MenuRepository();
        PaymentRepository paymentRepository = new PaymentRepository();
        PizzaService service = new PizzaService(menuRepository, paymentRepository);
        service.addPayment(1, PaymentType.Cash,0.0);
        service.addPayment(1, PaymentType.Card,200.0);

        Main main = new Main();
        main.writeInFiles(service);
        String content = readFile(report);
        assertTrue(content.contains("cash intake does not exist today!"));
        assertTrue(content.contains("200.0,Card"));
    }

    @Test
    void test_NoCashNorCard() throws IOException {
        File report = new File("./data/reports/raport_"+"11-04-2025.txt");
        report.setWritable(true);

        MenuRepository menuRepository = new MenuRepository();
        PaymentRepository paymentRepository = new PaymentRepository();
        PizzaService service = new PizzaService(menuRepository, paymentRepository);
        service.addPayment(1, PaymentType.Cash,0.0);
        service.addPayment(1, PaymentType.Card,0.0);
        Main main = new Main();
        main.writeInFiles(service);
        String content = readFile(report);
        assertTrue(content.contains("cash intake does not exist today!"));
        assertTrue(content.contains("card intake does not exist today!"));
    }

    @Test
    void test_ExceptionDuringWrite() throws IOException {


        String fileName = "data/reports/raport_" + DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now()) + ".txt";
        File file = new File(fileName);

        // Ensure the parent directory exists
        file.getParentFile().mkdirs();

        // Create the file and make it read-only
        file.createNewFile();
        file.setWritable(false);

        // Prepare the test service
        MenuRepository menuRepository = new MenuRepository();
        PaymentRepository paymentRepository = new PaymentRepository();
        PizzaService service = new PizzaService(menuRepository, paymentRepository);
        service.addPayment(1, PaymentType.Cash, 100.0);
        service.addPayment(1, PaymentType.Card, 200.0);

        Main main = new Main();
        main.writeInFiles(service); // This will fail silently inside

        // Check that the file is still empty (nothing written)
        assertEquals(0, file.length());

        // Cleanup
        file.setWritable(true);
        file.delete();
    }


}