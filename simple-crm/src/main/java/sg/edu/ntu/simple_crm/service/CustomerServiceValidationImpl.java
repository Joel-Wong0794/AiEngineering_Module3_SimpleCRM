package sg.edu.ntu.simple_crm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import sg.edu.ntu.simple_crm.exceptions.CustomerNotFoundException;
import sg.edu.ntu.simple_crm.model.Customer;
import sg.edu.ntu.simple_crm.repository.CustomerRepository;
import sg.edu.ntu.simple_crm.exceptions.InvalidCustomerException;

@Service
public class CustomerServiceValidationImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceValidationImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.createCustomer(customer);
    }

    @Override
    public Customer getCustomer(String id) {
        return customerRepository.getCustomer(getCustomerIndex(id));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }

    @Override
    public Customer updateCustomer(String id, Customer customer) {
        return customerRepository.updateCustomer(getCustomerIndex(id), customer);
    }

    @Override
    public void deleteCustomer(String id) {
        customerRepository.deleteCustomer(getCustomerIndex(id));
    }

    private int getCustomerIndex(String id) {
        for (Customer customer : customerRepository.getAllCustomers()) {
            if (customer.getId().equals(id)) {
                return customerRepository.getAllCustomers().indexOf(customer);
            }
        }
        throw new CustomerNotFoundException(id);
    }
}