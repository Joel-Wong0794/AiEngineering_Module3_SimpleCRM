package sg.edu.ntu.simple_crm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
// Connects this test class to Mockito's JUnit 5 support.
import org.junit.jupiter.api.extension.ExtendWith;
// Creates a fake object that we can control during a unit test.
import org.mockito.InjectMocks;
// Tells Mockito to place mock objects into the class being tested.
import org.mockito.Mock;
// Provides Mockito's integration with JUnit 5.
import org.mockito.junit.jupiter.MockitoExtension;

import sg.edu.ntu.simple_crm.exceptions.CustomerNotFoundException;
import sg.edu.ntu.simple_crm.model.Customer;
// The repository is a dependency of CustomerServiceImpl.
import sg.edu.ntu.simple_crm.repository.CustomerRepository;

// MockitoExtension starts and manages Mockito before each test.
@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {
    // Creates a mock repository instead of connecting to a real database.
    @Mock
    private CustomerRepository customerRepository;

    // Creates the real service and injects customerRepository into it.
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    public void createCustomer_validCustomer_returnsSavedCustomer() {

        // 1. ARRANGE: prepare the input customer and configure the mock repository.
        // Customer.builder() creates a Customer step by step using readable method
        // calls.
        Customer customer = Customer.builder()
                .firstName("Clint").lastName("Barton")
                .email("clint@avengers.com").contactNo("12345678")
                .jobTitle("Special Agent").yearOfBirth(1975)
                .build();

        // Stub the mock: when save() receives this customer, return the same customer.
        // The real repository is not called, so this unit test does not touch a
        // database.
        when(customerRepository.save(customer)).thenReturn(customer);

        // 2. ACT: call the real service method being tested.
        Customer savedCustomer = customerService.createCustomer(customer);

        // 3. ASSERT: check that the service returned the expected customer.
        assertEquals(customer, savedCustomer, "The saved customer should match the new customer");

        // Verify the interaction: confirm that save() was called exactly once with this
        // customer.
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void getCustomer_missingId_throwsCustomerNotFoundException() {
        // This test describes the missing-customer scenario.
        // 1. ARRANGE: choose an ID that the mock repository will not find.
        // The L suffix makes this a Long value, matching the method parameter type.
        Long customerId = 1L;

        // Stub findById() to return an empty Optional, meaning no customer exists
        // with this ID. The real database is not queried.
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // 2. ACT + 3. ASSERT: execute the service call and check the exception.
        // The lambda delays the method call until assertThrows is ready to observe it.
        // The test passes only when this call throws CustomerNotFoundException.
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomer(customerId));
    }

}
