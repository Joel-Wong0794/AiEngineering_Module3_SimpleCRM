package sg.edu.ntu.simple_crm.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.ntu.simple_crm.model.Customer;

// Loads the full Spring application context for this integration-style test.
@SpringBootTest
// Configures MockMvc so requests can be tested without starting a real server.
@AutoConfigureMockMvc
// Rolls back database changes after each test to keep tests isolated.
@Transactional
public class CustomerControllerTest {

    // Spring injects a test HTTP client for calling controller endpoints.
    @Autowired
    private MockMvc mockMvc;

    // Spring injects Jackson's JSON converter for request and response data.
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getCustomerById_existingId_returnsOk() throws Exception {
        // The test name describes the method, scenario, and expected result.
        // Step 1: build a GET request for the customer with ID 1.
        // Building the request does not send it yet.
        RequestBuilder request = MockMvcRequestBuilders.get("/customers/1");

        // Step 2: send the request and check the HTTP response.
        // perform() executes the request; each andExpect() is an assertion.
        mockMvc.perform(request)
                // 200 OK means the customer was found successfully.
                .andExpect(status().isOk())
                // The response body should be JSON.
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // jsonPath("$.id") reads the id property from the JSON response.
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void createCustomer_validCustomer_returnsCreated() throws Exception {
        // Step 1: create valid Java test data using the Customer builder.
        Customer newCustomer = Customer.builder()
                .firstName("Clint")
                .lastName("Barton")
                .email("clint@avengers.com")
                .contactNo("12345678")
                .jobTitle("Special Agent")
                .yearOfBirth(1975)
                .build();

        // Step 2: convert the Java object into a JSON request body.
        // Jackson produces JSON such as {"firstName":"Clint","lastName":"Barton"}.
        String newCustomerAsJson = objectMapper.writeValueAsString(newCustomer);

        // Step 3: build a POST request for the /customers endpoint.
        // contentType tells Spring that the request body is JSON.
        // content adds the serialized customer to the request body.
        RequestBuilder request = MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newCustomerAsJson);

        // Step 4: send the request and verify the created response.
        mockMvc.perform(request)
                // 201 Created means a new customer was successfully created.
                .andExpect(status().isCreated())
                // The response body should be JSON.
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // The database should generate an ID for the new customer.
                .andExpect(jsonPath("$.id").exists())
                // Check that the response contains the submitted customer fields.
                .andExpect(jsonPath("$.firstName").value("Clint"))
                .andExpect(jsonPath("$.lastName").value("Barton"));
    }

    @Test
    public void createCustomer_invalidCustomer_returnsBadRequest() throws Exception {
        // Step 1: create invalid data to test validation failure.
        // Blank names violate @NotBlank, and the malformed email violates @Email.
        Customer invalidCustomer = Customer.builder()
                .firstName("  ")
                .lastName("  ")
                .email("not-a-valid-email")
                .contactNo("12345678")
                .jobTitle("Manager")
                .yearOfBirth(1990)
                .build();

        // Step 2: serialize the invalid Java object into JSON.
        String invalidCustomerAsJson = objectMapper.writeValueAsString(invalidCustomer);

        // Step 3: build the same POST request shape as the valid test,
        // but with data that should be rejected by validation.
        RequestBuilder request = MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidCustomerAsJson);

        // Step 4: assert that validation returns HTTP 400 Bad Request.
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                // Error details should also be returned as JSON.
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}