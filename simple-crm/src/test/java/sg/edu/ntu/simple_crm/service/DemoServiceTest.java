package sg.edu.ntu.simple_crm.service;

// Imports the JUnit annotation used to mark a method as a test.
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// Imports the assertion method used to compare expected and actual values.
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;

// Groups tests for the DemoService class.
public class DemoServiceTest {
    // Field shared by the test methods; each test receives a fresh instance.
    private DemoService demoService;

    // Runs before every @Test method so each test starts with clean setup.
    @BeforeEach
    public void init() {
        // Arrange the service used by the tests.
        demoService = new DemoService();
    }

    // Tells JUnit to run the method below as a test.
    @Test
    // Test names describe: method_under_test _ scenario _ expected_result.
    public void calculateAge_validYear_returnsCorrectAge() {
        // Arrange: demoService was created by init(), and we define the expected
        // result.
        // This is the result we expect from the method for these input values.
        int expectedAge = 35;

        // Act: call the method being tested with a birth year and current year.
        int actualAge = demoService.calculateAge(1990, 2025);

        // Assert: fail the test if the actual result is different from the expected
        // result.
        assertEquals(expectedAge, actualAge, "Age should be current year minus birth year");
    }

    @ParameterizedTest
    @CsvSource({
            "1990, 2025, 35",
            "1965, 2025, 60",
            "2000, 2025, 25",
            "2025, 2025, 0"
    })

    public void calculateAge_variousYears_returnsCorrageAge(int yearOfBirth, int expectedAge) {
        // 1. ACT
        int actualAge = demoService.calculateAge(yearOfBirth, currentYear);

        // ASSERT
        assertEquals(expectedAge, actualAge);

    }

    @Test
    void formatFullName_validNames_returnsFullNames() {
        //1. ARRANGE
        String expectedFullName = "Clint Barton";


        // 2. ACT
        String actualFullName = demoService.formatFullName(firstName:"Clint", lastName:"Barton");

        //3 ASSERT
       assertEquals(expectedFullName, actualFullName, " Full name should be first and last name joined by a space");

    }
}
