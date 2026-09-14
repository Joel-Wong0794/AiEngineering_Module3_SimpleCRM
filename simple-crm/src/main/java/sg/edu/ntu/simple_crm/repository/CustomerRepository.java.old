package sg.edu.ntu.simple_crm.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import sg.edu.ntu.simple_crm.model.Customer;

@Repository
public class CustomerRepository {

    private List<Customer> customers = new ArrayList<>();

    public CustomerRepository() {
        customers.add(new Customer("Bruce", "Banner"));
        customers.add(new Customer("Peter", "Parker"));
        customers.add(new Customer("Stephen", "Strange"));
        customers.add(new Customer("Steve", "Rogers"));
    }

    // Create
    public Customer createCustomer(Customer customer) {
        customers.add(customer);
        return customer;
    }

    // Get One
    public Customer getCustomer(int index) {
        if (index >= 0 && index < customers.size()) {
            return customers.get(index);
        }
        return null;
    }

    // Get All
    public List<Customer> getAllCustomers() {
        return customers;
    }

    // Update (full replace of the customer's data)
    public Customer updateCustomer(int index, Customer customer) {
        Customer customerToUpdate = customers.get(index);
        customerToUpdate.setFirstName(customer.getFirstName());
        customerToUpdate.setLastName(customer.getLastName());
        customerToUpdate.setEmail(customer.getEmail());
        customerToUpdate.setContactNo(customer.getContactNo());
        customerToUpdate.setJobTitle(customer.getJobTitle());
        customerToUpdate.setYearOfBirth(customer.getYearOfBirth());
        return customerToUpdate;
    }

    // Delete
    public void deleteCustomer(int index) {
        customers.remove(index);
    }
}
