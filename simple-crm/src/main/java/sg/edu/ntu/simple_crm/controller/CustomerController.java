package sg.edu.ntu.simple_crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.ntu.simple_crm.exceptions.CustomerNotFoundException;
import sg.edu.ntu.simple_crm.exceptions.InvalidCustomerException;
import sg.edu.ntu.simple_crm.model.Customer;
import sg.edu.ntu.simple_crm.model.Interaction;
import sg.edu.ntu.simple_crm.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    /// Dependency injection constructor
    public CustomerController(
            @Qualifier("customerServiceValidationImpl") CustomerService customerService) {
        this.customerService = customerService;
    }

    // Create
    @PostMapping()
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        try {
            Customer newCustomer = customerService.createCustomer(customer);
            return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
        } catch (InvalidCustomerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Read (GET ALL)
    @GetMapping()
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> allCustomers = customerService.getAllCustomers();
        return new ResponseEntity<>(allCustomers, HttpStatus.OK);
    }

    // READ (GET ONE)
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        try {
            Customer foundCustomer = customerService.getCustomer(id);
            return new ResponseEntity<>(foundCustomer, HttpStatus.OK);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Customer>> searchCustomers(@RequestParam String firstName) {
        List<Customer> foundCustomers = customerService.searchCustomers(firstName);
        return new ResponseEntity<>(foundCustomers, HttpStatus.OK);
    }

    @GetMapping("/search/job")
    public ResponseEntity<List<Customer>> searchCustomersByJobTitle(@RequestParam String jobTitle) {
        List<Customer> foundCustomers = customerService.searchCustomersByJobTitle(jobTitle);
        return new ResponseEntity<>(foundCustomers, HttpStatus.OK);
    }

    // update
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer customer) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(id, customer);
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } catch (InvalidCustomerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/interactions")
    public ResponseEntity<Interaction> addInteractionToCustomer(
            @PathVariable Long id,
            @RequestBody Interaction interaction) {
        Interaction newInteraction = customerService.addInteractionToCustomer(id, interaction);

        return new ResponseEntity<>(newInteraction, HttpStatus.CREATED);
    }

}