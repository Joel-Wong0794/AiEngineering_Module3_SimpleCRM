package sg.edu.ntu.simple_crm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import sg.edu.ntu.simple_crm.exceptions.CustomerNotFoundException;
import sg.edu.ntu.simple_crm.model.Customer;
import sg.edu.ntu.simple_crm.model.Interaction;
import sg.edu.ntu.simple_crm.repository.CustomerRepository;
import sg.edu.ntu.simple_crm.repository.InteractionRepository;

@Service
public class CustomerServiceValidationImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final InteractionRepository interactionRepository;

    public CustomerServiceValidationImpl(
            CustomerRepository customerRepository,
            InteractionRepository interactionRepository) {
        this.customerRepository = customerRepository;
        this.interactionRepository = interactionRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        Customer customerToUpdate = getCustomer(id);
        customerToUpdate.setFirstName(customer.getFirstName());
        customerToUpdate.setLastName(customer.getLastName());
        customerToUpdate.setEmail(customer.getEmail());
        customerToUpdate.setContactNo(customer.getContactNo());
        customerToUpdate.setJobTitle(customer.getJobTitle());
        customerToUpdate.setYearOfBirth(customer.getYearOfBirth());
        return customerRepository.save(customerToUpdate);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.delete(getCustomer(id));
    }

    @Override
    public Interaction addInteractionToCustomer(Long id, Interaction interaction) {
        Customer customer = getCustomer(id);
        interaction.setCustomer(customer);
        return interactionRepository.save(interaction);
    }

    @Override
    public List<Customer> searchCustomersByFirstName(String firstName) {
        return customerRepository.findByFirstNameContaining(firstName);
    }

    @Override
    public List<Customer> searchCustomersByLastName(String lastName) {
        return customerRepository.findByLastNameJPQL(lastName);
    }

    @Override
    public List<Customer> searchCustomersByJobTitle(String jobTitle) {
        return customerRepository.findByJobTitleJPQL(jobTitle);
    }
}