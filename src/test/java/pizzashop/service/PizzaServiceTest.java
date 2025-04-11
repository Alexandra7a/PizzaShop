package pizzashop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

class PizzaServiceTest {
    //private PizzaService pizzaService=new PizzaService(new MenuRepository(),new PaymentRepository());

    // 3 teste valide ECP
    @DisplayName("Testare plăți valide ECP")
    @ParameterizedTest
    @CsvSource({"1, Cash, 50", "2, Card, 100", "3, Cash, 200"})
    void testPlatiValideECP(int masa, PaymentType tipPlata, double valoare) {
        // Arrange
        PizzaService pizzaService = new PizzaService(new MenuRepository(), new PaymentRepository());

        // Act & Assert
        assertDoesNotThrow(() -> pizzaService.addPayment(masa, tipPlata, valoare));
    }

    // 3 teste invalide ECP
    @DisplayName("Testare plăți invalide ECP")
    @ParameterizedTest
    @CsvSource({"1, Cash, -10", "-2, Card, 50"})
    void testPlatiInvalideECP(int masa, PaymentType tipPlata, double valoare) {
        // Arrange
        PizzaService pizzaService = new PizzaService(new MenuRepository(), new PaymentRepository());

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                pizzaService.addPayment(masa, tipPlata, valoare)
        );

        // Assert
        assertNotNull(exception.getMessage());
    }

    // 2 teste valide BVA
    @DisplayName("Testare valori de frontieră valide BVA")
    @ParameterizedTest
    @CsvSource({"1, Cash, 0.01", "2, Card, 9999.99"})
    void testPlatiValideBVA(int masa, PaymentType tipPlata, double valoare) {
        // Arrange
        PizzaService pizzaService = new PizzaService(new MenuRepository(), new PaymentRepository());

        // Act & Assert
        assertDoesNotThrow(() -> pizzaService.addPayment(masa, tipPlata, valoare));
    }

    // 2 teste invalide BVA
    @DisplayName("Testare valori de frontieră invalide BVA")
    @ParameterizedTest
    @CsvSource({"1, Cash, 0", "2, Card, 50000"})
    void testPlatiInvalideBVA(int masa, PaymentType tipPlata, double valoare) {
        // Arrange
        PizzaService pizzaService = new PizzaService(new MenuRepository(), new PaymentRepository());

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                pizzaService.addPayment(masa, tipPlata, valoare));

        // Assert
        assertNotNull(exception.getMessage());
    }


    @Disabled("Test dezactivat temporar")
    @DisplayName("Test dezactivat")
    void testDezactivat() {
        fail("Acest test nu ar trebui să fie rulat!");
    }

    @RepeatedTest(3)
    @DisplayName("Testare repetată pentru plăți valide")
    void testPlatiRepetate() {
        // Arrange
        PizzaService pizzaService = new PizzaService(new MenuRepository(), new PaymentRepository());

        // Act & Assert
        assertDoesNotThrow(() -> pizzaService.addPayment(1, PaymentType.Cash, 20.0));
    }



}