package sg.edu.ntu.simple_crm;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import sg.edu.ntu.simple_crm.controller.CustomerController;
import sg.edu.ntu.simple_crm.exceptions.CustomerNotFoundException;
import sg.edu.ntu.simple_crm.exceptions.GlobalExceptionHandler;
import sg.edu.ntu.simple_crm.exceptions.InvalidCustomerIdException;
import sg.edu.ntu.simple_crm.model.Customer;
import sg.edu.ntu.simple_crm.repository.CustomerRepository;
import sg.edu.ntu.simple_crm.repository.InteractionRepository;
import sg.edu.ntu.simple_crm.service.CustomerServiceImpl;

class CustomerActivityTests {

    private CustomerServiceImpl customerService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        customerService = mock(CustomerServiceImpl.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new CustomerController(customerService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void searchesByPartialFirstName() throws Exception {
        when(customerService.searchCustomersByFirstName("an")).thenReturn(List.of());

        mockMvc.perform(get("/customers/search/name").param("firstName", "an"))
                .andExpect(status().isOk());

        verify(customerService).searchCustomersByFirstName("an");
    }

    @Test
    void searchesByLastName() throws Exception {
        when(customerService.searchCustomersByLastName("Tan")).thenReturn(List.of());

        mockMvc.perform(get("/customers/search/lastname").param("lastName", "Tan"))
                .andExpect(status().isOk());

        verify(customerService).searchCustomersByLastName("Tan");
    }

    @Test
    void rejectsContactNumberThatIsNotEightDigits() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "Jane",
                          "contactNo": "9123abcd"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString(
                        "Contact number must be exactly 8 digits")));

        verify(customerService, never()).createCustomer(any());
    }

    @Test
    void createsCustomerWithValidContactNumber() throws Exception {
        Customer savedCustomer = new Customer("Jane", "Tan");
        savedCustomer.setContactNo("91234567");
        when(customerService.createCustomer(any(Customer.class))).thenReturn(savedCustomer);

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "Jane",
                          "lastName": "Tan",
                          "contactNo": "91234567"
                        }
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    void returnsBadRequestForNegativeId() throws Exception {
        when(customerService.getCustomer(-5L)).thenThrow(new InvalidCustomerIdException(-5L));

        mockMvc.perform(get("/customers/-5"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Customer id must be greater than zero: -5"));
    }

    @Test
    void returnsNotFoundForMissingPositiveId() throws Exception {
        when(customerService.getCustomer(9999L)).thenThrow(new CustomerNotFoundException(9999L));

        mockMvc.perform(get("/customers/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Could not find customer with id: 9999"));
    }

    @Test
    void rejectsInvalidIdBeforeCallingRepository() {
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        InteractionRepository interactionRepository = mock(InteractionRepository.class);
        CustomerServiceImpl service = new CustomerServiceImpl(customerRepository, interactionRepository);

        org.junit.jupiter.api.Assertions.assertThrows(
                InvalidCustomerIdException.class,
                () -> service.getCustomer(0L));

        verify(customerRepository, never()).findById(any());
    }

    @Test
    void positiveMissingIdStillChecksRepository() {
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        InteractionRepository interactionRepository = mock(InteractionRepository.class);
        CustomerServiceImpl service = new CustomerServiceImpl(customerRepository, interactionRepository);
        when(customerRepository.findById(9999L)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                CustomerNotFoundException.class,
                () -> service.getCustomer(9999L));

        verify(customerRepository).findById(9999L);
    }
}