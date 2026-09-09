package sg.edu.ntu.simple_crm.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.ntu.simple_crm.exceptions.CustomerNotFoundException;
import sg.edu.ntu.simple_crm.model.Customer;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    public CustomerController() {
        customers.add(new Customer("Bruce", "Banner"));
        customers.add(new Customer("Peter", "Parker"));
        customers.add(new Customer("Steve", "Rogers"));
    }

    private ArrayList<Customer> customers = new ArrayList<>();

    // Create
    @PostMapping()
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        customers.add(customer);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);

        // Alternate syntax
        // return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    // Read
    @GetMapping()
    public ResponseEntity<ArrayList<Customer>> getAllCustomers() {
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    // GET
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomer(@PathVariable String id) {
        try {
            int index = getCustomerIndex(id);
            return new ResponseEntity<>(customers.get(index), HttpStatus.OK);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // update
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        try {
            int index = getCustomerIndex(id);
            customers.set(index, customer);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable String id) {
        try {
            int index = getCustomerIndex(id);
            return new ResponseEntity<>(customers.remove(index), HttpStatus.OK);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Function that is used repeatedly across different operations to look up
    // particular IDs
    private int getCustomerIndex(String id) {
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                return customers.indexOf(customer);
            }
        }

        // Not found
        throw new CustomerNotFoundException(id);
    }
}