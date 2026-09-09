package sg.edu.ntu.simple_crm.service;

import java.util.List;

import sg.edu.ntu.simple_crm.model.Customer;

public interface CustomerService {
    Customer createCustomer(Customer customer);

    Customer getCustomer(String id);

    List<Customer> getAllCustomers();

    Customer updateCustomer(String id, Customer customer);

    void deleteCustomer(String id);
}
